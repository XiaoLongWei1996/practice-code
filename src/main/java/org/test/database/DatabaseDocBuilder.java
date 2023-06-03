package org.test.database;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.data.HyperlinkData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.mysql.jdbc.Driver;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.test.database.excel.Head;
import org.test.database.excel.HeadHyperlinkHandler;
import org.test.database.excel.HeadStyleHandler;
import org.test.database.excel.Info;
import org.test.util.excel.strategy.MergeStrategy;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className: ExcelBuilder
 * @author: xlw
 * @date: 2023/6/2 10:03
 **/
public class DatabaseDocBuilder {

    /**
     * 表字段描述识别正则表达式
     */
    private static final String FIELD_COMMENT_REGEX = "[/\\。，！？?.,]";

    /**
     * 构建数据库设计文档
     *
     * @param url         url
     * @param userName    用户名
     * @param password    密码
     * @param tablePrefix 表前缀
     * @param outPath     出路径
     */
    public static void build(String url, String userName, String password, String tablePrefix, String outPath) {
        System.out.println("生成数据库设计文档......");
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();
            //查询所有的表
            result = statement.executeQuery("SHOW TABLES");
            List<String> tableList = new ArrayList<>();
            String tableName;
            while (result.next()) {
                //获取表名
                tableName = result.getString(1);
                if (tablePrefix != null && !tableName.startsWith(tablePrefix)) {
                    continue;
                }
                tableList.add(tableName);
            }
            result.close();
            //处理数据，并写入excel
            handle(tableList, statement, outPath);
        } catch (SQLException e) {
            System.out.println("生成数据库设计文档失败。");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("生成数据库设计文档完成。");
    }

    /**
     * 处理数据，写入到excel
     *
     * @param tableList 表列表
     * @param statement 声明
     * @param outPath   输出路径
     * @throws SQLException sqlexception异常
     */
    private static void handle(List<String> tableList, Statement statement, String outPath) throws SQLException {
        if (tableList == null || tableList.size() == 0) {
            return;
        }
        ResultSet resultSet = null;
        try {
            List<Head> datas = new ArrayList<>();
            Map<String, List<Info>> infoMap = new HashMap<>(tableList.size());
            for (String table : tableList) {
                //获取数据库表的建表sql
                resultSet = statement.executeQuery("SHOW CREATE TABLE " + table);
                while (resultSet.next()) {
                    String tableInfo = resultSet.getString(2);
                    //生成数据库设计文档目录
                    Head head = getHead(table, tableInfo);
                    datas.add(head);
                    //生成数据库设计文档具体内容
                    List<Info> infos = getTableInfo(table, tableInfo);
                    infoMap.put(table, infos);
                }
                resultSet.close();
            }
            writeExcle(datas, infoMap, outPath);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }

    /**
     * 得到excel首sheet信息
     *
     * @param tableName 表名
     * @param tableInfo 表信息
     * @return {@link Head}
     */
    private static Head getHead(String tableName, String tableInfo) {
        String regex = "COMMENT='(.*)'";
        Head head = new Head();
        head.setTable(tableName);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tableInfo);
        while (matcher.find()) {
            String comment = matcher.group(1);
            comment = comment.replaceAll(FIELD_COMMENT_REGEX, " ");
            //设置超链接
            WriteCellData<String> hyperlink = new WriteCellData<>(comment);
            HyperlinkData hyperlinkData = new HyperlinkData();
            hyperlinkData.setAddress("#'" + comment + "'!A1");
            hyperlinkData.setHyperlinkType(HyperlinkData.HyperlinkType.DOCUMENT);
            hyperlink.setHyperlinkData(hyperlinkData);
            head.setTableComment(hyperlink);
        }
        return head;
    }

    /**
     * 得到表信息，excel其他sheet页数据
     *
     * @param tableName 表名
     * @param tableInfo 表信息
     * @return {@link List}<{@link Info}>
     */
    private static List<Info> getTableInfo(String tableName, String tableInfo) {
        List<Info> data = new ArrayList<>();
        String[] rows = tableInfo.split("\\r?\\n");
        for (String row : rows) {
            Info info = new Info();
            info.setTable(tableName);
            row = row.trim();
            if (!row.startsWith("`")) {
                continue;
            }
            String[] segments = row.split("\\s");
            //设置字段
            info.setField(segments[0].replace("`", ""));
            //设置类型
            info.setFieldType(segments[1]);
            //设置注释
            boolean b = false;
            StringJoiner joiner = new StringJoiner(" ");
            for (int i = 2; i < segments.length; i++) {
                String segment = segments[i];
                if (b) {
                    joiner.add(segment.replaceAll("[',]", ""));
                }
                if (segment.equals("COMMENT")) {
                    b = true;
                }
            }
            info.setComment(joiner.toString());
            data.add(info);
        }
        return data;
    }

    /**
     * 写出到excle
     *
     * @param datas   数据
     * @param infoMap 信息图
     * @param outPath 输出路径
     */
    private static void writeExcle(List<Head> datas, Map<String, List<Info>> infoMap, String outPath) {
        try (ExcelWriter writer = EasyExcel.write(outPath).build()) {
            //写下目录
            WriteSheet oneSheet = EasyExcel.writerSheet(0, "目录").head(Head.class).build();
            writer.write(datas, oneSheet);
            //写下具体文档
            HeadHyperlinkHandler headHyperlinkHandler = new HeadHyperlinkHandler(HyperlinkType.DOCUMENT);
            headHyperlinkHandler.addColumnHyperlink(6, "#'目录'!A1");
            for (int i = 0; i < datas.size(); i++) {
                Head head = datas.get(i);
                List<Info> infos = infoMap.get(head.getTable());
                WriteSheet otherSheet = EasyExcel
                        .writerSheet(i + 1, head.getTableComment().getStringValue())
                        .head(Info.class)
                        .registerWriteHandler(new MergeStrategy(infos.size(), 0))
                        .registerWriteHandler(headHyperlinkHandler)
                        .registerWriteHandler(new HeadStyleHandler())
                        .build();
                writer.write(infos, otherSheet);
            }
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://xlw.asia:3306/study?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&serverTimezone=Asia/Shanghai";
        String userName = "xlw";
        String password = "xlw1996XLW.";
        String outPath = "E:\\temp\\test.xlsx";
        DatabaseDocBuilder.build(url, userName, password, "p_", outPath);
    }
}
