package org.test.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className: WordUtil
 * @author: xlw
 * @date: 2023/5/19 8:51
 */
public class WordUtil {

    private static final String PATTERN = "\\$.*\\$";

    private static final Map<String, XWPFChart> chartMap = new HashMap<>();

    public static void handleWord(String path, String outPath) {
        try (InputStream in = new FileInputStream(path);
             OutputStream out = new FileOutputStream(outPath)) {
            XWPFDocument document = new XWPFDocument(in);
            Map<String, String> data = new HashMap<>();
            data.put("$dateTime$", "2023年05月");
            data.put("$lastDate$", "2023-01-05");
            data.put("$tba$", "15.26");
            replaceText(document, data);
            // 加载图表
            loadChart(document);

            List<String> classis = new ArrayList<>();
            classis.add("a");
            classis.add("b");
            classis.add("c");
            classis.add("d");
            classis.add("e");
            classis.add("f");

            List<String> series = new ArrayList<>();
            series.add("#1");
            series.add("#2");
            series.add("#3");

            List<Map<String, String>> dataItems = new ArrayList<>();
            Map<String, String> row1 = new HashMap<>();
            row1.put("#1", "123");
//            row1.put("#2", "0");
//            row1.put("#3", "13");
            dataItems.add(row1);

            Map<String, String> row2 = new HashMap<>();
            row2.put("#1", "852");
//            row2.put("#2", "0");
//            row2.put("#3", "16");
            dataItems.add(row2);

            Map<String, String> row3 = new HashMap<>();
            row3.put("#1", "753");
//            row3.put("#2", "0");
//            row3.put("#3", "45");
            dataItems.add(row3);

            Map<String, String> row4 = new HashMap<>();
            row4.put("#1", "564");
//            row4.put("#2", "0");
//            row4.put("#3", "5");
            dataItems.add(row4);

            Map<String, String> row5 = new HashMap<>();
            row5.put("#1", "236");
//            row5.put("#2", "0");
//            row5.put("#3", "52");
            dataItems.add(row5);

            Map<String, String> row6 = new HashMap<>();
            row6.put("#1", "452");
//            row6.put("#2", "0");
//            row6.put("#3", "7");
            dataItems.add(row6);

            modifyChartExcel("mix", "测试", classis, series, dataItems);
            document.write(out);
            out.flush();
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 替换文本
     */
    private static void replaceText(XWPFDocument document, Map<String, String> data) {
        // 替换页眉
        replaceHeaderText(document, data);
        // 替换段落
        replaceParagraphsText(document, data);
        // 替换表
        replaceTableText(document, data);
        // 替换页脚
        replaceFooterText(document, data);
    }

    private static void loadChart(XWPFDocument document) {
        List<XWPFChart> charts = document.getCharts();
        if (CollectionUtil.isEmpty(charts)) {
            return;
        }
        for (XWPFChart chart : charts) {
            // 判断是否图表元素
            // 将图表内置的excel第一行,第一列作为改图表的key,便于后面操作
            String key = extractChartKey(chart);
            chartMap.put(key, chart);
        }
    }

    /**
     * 更换标题文本
     *
     * @param document 文档
     * @param data     数据
     */
    private static void replaceHeaderText(XWPFDocument document, Map<String, String> data) {
        // 获取页眉
        List<XWPFHeader> headerList = document.getHeaderList();
        if (CollectionUtil.isNotEmpty(headerList)) {
            for (XWPFHeader xwpfHeader : headerList) {
                List<XWPFParagraph> paragraphs = xwpfHeader.getParagraphs();
                handleParagraphs(paragraphs, data);
            }
        }
    }

    /**
     * 取代段落文本
     *
     * @param document 文档
     * @param data     数据
     */
    private static void replaceParagraphsText(XWPFDocument document, Map<String, String> data) {
        // 获取段落集合
        List<XWPFParagraph> paragraphList = document.getParagraphs();
        handleParagraphs(paragraphList, data);
    }

    /**
     * 替换表文本
     *
     * @param document 文档
     * @param data     数据
     */
    private static void replaceTableText(XWPFDocument document, Map<String, String> data) {
        // 获取表格
        List<XWPFTable> tables = document.getTables();
        if (CollectionUtil.isNotEmpty(tables)) {
            for (XWPFTable table : tables) {
                List<XWPFTableRow> rows = table.getRows();
                if (CollectionUtil.isEmpty(rows)) {
                    continue;
                }
                for (XWPFTableRow row : rows) {
                    List<XWPFTableCell> tableCells = row.getTableCells();
                    for (XWPFTableCell tableCell : tableCells) {
                        List<XWPFParagraph> paragraphs = tableCell.getParagraphs();
                        handleParagraphs(paragraphs, data);
                    }
                }
            }
        }
    }

    /**
     * 替换页脚文本
     *
     * @param document 文档
     * @param data     数据
     */
    private static void replaceFooterText(XWPFDocument document, Map<String, String> data) {
        // 获取页脚
        List<XWPFFooter> footerList = document.getFooterList();
        if (CollectionUtil.isNotEmpty(footerList)) {
            for (XWPFFooter xwpfFooter : footerList) {
                List<XWPFParagraph> paragraphs = xwpfFooter.getParagraphs();
                handleParagraphs(paragraphs, data);
            }
        }
    }

    private static void handleParagraphs(List<XWPFParagraph> paragraphs, Map<String, String> data) {
        if (CollectionUtil.isEmpty(paragraphs)) {
            return;
        }
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                // 替换文字
                String newText = replaceText(text, data);
                if (StrUtil.isNotBlank(newText)) {
                    run.setText(newText, 0);
                }
            }
        }
    }

    private static String replaceText(String text, Map<String, String> data) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(text);
        boolean flag = false;
        while (matcher.find()) {
            String key = matcher.group(0);
            String value = data.get(key);
            if (StrUtil.isBlank(value)) {
                continue;
            }
            text = text.replace(key, value);
            flag = true;
        }
        return flag ? text : null;
    }

    private static String extractChartKey(XWPFChart chart) {
        String key = null;
        XSSFWorkbook workbook = null;
        try {
            workbook = chart.getWorkbook();
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 第一行
            Row row = sheet.getRow(0);
            // 第一列
            Cell cell = row.getCell(0);
            cell.setCellType(CellType.STRING);  // 设置一下格子类型为字符串，不然如果是数字或者时间的话，获取很麻烦
            key = cell.getStringCellValue().trim(); // 获取格子内容
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (workbook != null) {
//                try {
//                    workbook.close();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            }
        }

        return key;
    }

    /**
     * 修改图表excel
     */
    private static void modifyChartExcel(String key,
                                         String title,
                                         List<String> classes,
                                         List<String> series,
                                         List<Map<String, String>> dataItems) {
        XWPFChart chart = chartMap.get(key);
        if (chartMap.isEmpty() || ObjectUtil.isNull(chart)) {
            return;
        }
        // 根据属性第一列名称切换数据类型
        CTChart ctChart = chart.getCTChart();
        CTPlotArea plotArea = ctChart.getPlotArea();

        // 设置标题
        if (StrUtil.isNotBlank(title)) {
            setChartTitle(ctChart, title);
        }

        // 刷新chart内置的excel数据
        refreshInnerExcel(chart, classes, series, dataItems);

        if (key.contains("pie_chart")) {
            CTPieChart pieChart = plotArea.getPieChartArray(0);
            List<CTPieSer> serList = pieChart.getSerList();// 获取柱状图单位
            // 刷新word显示的数据
            refreshPieChart(pieChart, serList, dataItems, series, classes, 1);
        } else if (key.contains("colum_chart")) {
            CTBarChart barChart = plotArea.getBarChartArray(0);
            List<CTBarSer> BarSerList = barChart.getSerList();  // 获取柱状图单位
            // 刷新word显示的数据
            refreshColumChart(barChart, BarSerList, dataItems, series, classes, 1);
        } else if (key.contains("mix")) {
            CTBarChart barChart = plotArea.getBarChartArray(0);
            List<CTBarSer> BarSerList = barChart.getSerList();  // 获取柱状图单位
            refreshColumChart(barChart, BarSerList, dataItems, series, classes, 1);

            CTLineChart lineChart = plotArea.getLineChartArray(0);
            List<CTLineSer> lineSerList = lineChart.getSerList();
            refreshLineChart(lineChart, lineSerList, dataItems, series, classes, 3);
        }

    }

    /**
     * 设置图表标题
     *
     * @param ctChart ct图
     * @param title   标题
     */
    private static void setChartTitle(CTChart ctChart, String title) {
        CTTitle ct = ctChart.getTitle();
        if (ObjectUtil.isEmpty(ct)) {
            return;
        }
        CTTx tx = ct.getTx();
        if (tx != null) {
            CTTextBody ctTextBody = tx.getRich();
            List<CTTextParagraph> ctTextParagraphslist = ctTextBody.getPList();
            CTTextParagraph ctTextParagraph = ctTextParagraphslist.get(0);
            List<CTRegularTextRun> ctRegularTextRunslist = ctTextParagraph.getRList();
            // 设置标题
            ctRegularTextRunslist.get(0).setT(title);
        }
    }

    /**
     * 刷新excel内部
     *
     * @param chart     图
     * @param classes   类
     * @param series    系列
     * @param dataItems 数据项
     */
    private static boolean refreshInnerExcel(
            XWPFChart chart,
            List<String> classes,
            List<String> series,
            List<Map<String, String>> dataItems) {
        boolean flag = true;
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        //根据数据创建excel第一行系列
        for (int i = 0; i < series.size(); i++) {
            if (sheet.getRow(0) == null) {
                sheet.createRow(0).createCell(i + 1).setCellValue(series.get(i) == null ? "" : series.get(i));
            } else {
                sheet.getRow(0).createCell(i + 1).setCellValue(series.get(i) == null ? "" : series.get(i));
            }
        }

        //遍历数据行
        for (int i = 0; i < dataItems.size(); i++) {
            Map<String, String> data = dataItems.get(i);//数据行
            // 遍历列
            for (int j = 0; j <= series.size(); j++) {
                String cell = null;
                if (j > 0) {
                    cell = series.get(j - 1);
                }
                if (sheet.getRow(i + 1) == null) {
                    //处理第二行开始的第一列
                    if (j == 0) {
                        sheet.createRow(i + 1).createCell(j).setCellValue(classes.get(i) == null ? "" : classes.get(i));
                    } else {
                        sheet.createRow(i + 1).createCell(j).setCellValue(data.get(cell) == null ? 0.00 : Double.valueOf(data.get(cell)));
                    }
                } else {
                    if (j == 0) {
                        sheet.getRow(i + 1).createCell(j).setCellValue(classes.get(i) == null ? "" : classes.get(i));
                    } else {
                        System.out.println(cell);
                        sheet.getRow(i + 1).createCell(j).setCellValue(data.get(cell) == null ? 0.00 : Double.valueOf(data.get(cell)));
                    }
                }
            }
        }
        OutputStream xlsOut = null;

        try {
            // 更新嵌入的workbook
            List<POIXMLDocumentPart> parts = chart.getRelations();
            if (CollectionUtil.isNotEmpty(parts)) {
                for (POIXMLDocumentPart part : parts) {
                    if (part.toString().contains("sheet")) {
                        System.out.println(part.toString());
                        xlsOut = part.getPackagePart().getOutputStream();
                        break;
                    }
                }
            }

            //xlsOut = xlsPart.getPackagePart().getOutputStream();
            //xlsOut = new FileOutputStream("E:\\Xlw\\1.xlsx");
            chart.setWorkbook(wb);
            //wb.write(xlsOut);
            xlsOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (xlsOut != null) {
                try {
                    xlsOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    private static boolean refreshPieChart(Object typeChart, List<?> serList, List<Map<String, String>> dataList, List<String> series, List<String> classes, int position) {

        boolean result = true;
        //更新数据区域
        for (int i = 0; i < serList.size(); i++) {
            CTAxDataSource cat = null;
            CTNumDataSource val = null;
            CTPieSer ser = ((CTPieChart) typeChart).getSerArray(i);

            // 设置系列（excel第一行数据） 用以下这个方式，可以兼容office和wps
            CTSerTx tx = ser.getTx();
            tx.getStrRef().getStrCache().getPtList().get(0).setV(series.get(0));

            // 类别(excel第一列数据)
            cat = ser.getCat();
            // 获取图表的值
            val = ser.getVal();
            // strData.set
            CTStrData strData = cat.getStrRef().getStrCache();
            CTNumData numData = val.getNumRef().getNumCache();
            strData.setPtArray((CTStrVal[]) null); // unset old axis text
            numData.setPtArray((CTNumVal[]) null); // unset old values

            // set model
            long idx = 0;
            for (int j = 0; j < classes.size(); j++) {
                //判断获取的值是否为空
                String value = "0";
                if (new BigDecimal(dataList.get(j).get(series.get(0))) != null) {
                    value = new BigDecimal(dataList.get(j).get(series.get(0))).toString();
                }
                if (!"0".equals(value)) {
                    CTNumVal numVal = numData.addNewPt();//序列值
                    numVal.setIdx(idx);
                    numVal.setV(value);
                }
                //设置类别（）
                CTStrVal sVal = strData.addNewPt();
                sVal.setIdx(idx);
                sVal.setV(classes.get(j));
                idx++;
            }
            numData.getPtCount().setVal(idx);
            strData.getPtCount().setVal(idx);

            //赋值横坐标数据区域
            String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0)
                    .formatAsString("Sheet1", true);
            cat.getStrRef().setF(axisDataRange);

            //数据区域
            String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
                    .formatAsString("Sheet1", true);
            val.getNumRef().setF(numDataRange);
        }
        return result;
    }

    private static boolean refreshColumChart(Object typeChart, List<?> serList, List<Map<String, String>> dataList, List<String> series, List<String> classes, int position) {
        boolean result = true;
        //更新数据区域
        for (int i = 0; i < serList.size(); i++) {
            CTAxDataSource cat = null;
            CTNumDataSource val = null;
            CTBarSer ser = ((CTBarChart) typeChart).getSerArray(i);

            // 设置系列（excel第一行数据） 用以下这个方式，可以兼容office和wps
            CTSerTx tx = ser.getTx();
            tx.getStrRef().getStrCache().getPtList().get(0).setV(series.get(0));

            // 类别(excel第一列数据)
            cat = ser.getCat();
            // 获取图表的值
            val = ser.getVal();
            // strData.set
            CTStrData strData = cat.getStrRef().getStrCache();
            CTNumData numData = val.getNumRef().getNumCache();
            strData.setPtArray((CTStrVal[]) null); // unset old axis text
            numData.setPtArray((CTNumVal[]) null); // unset old values

            // set model
            long idx = 0;
            for (int j = 0; j < classes.size(); j++) {
                //判断获取的值是否为空
                String value = "0";
                if (new BigDecimal(dataList.get(j).get(series.get(i))) != null) {
                    value = new BigDecimal(dataList.get(j).get(series.get(i))).toString();
                }
                if (!"0".equals(value)) {
                    CTNumVal numVal = numData.addNewPt();//序列值
                    numVal.setIdx(idx);
                    numVal.setV(value);
                }
                //设置类别（）
                CTStrVal sVal = strData.addNewPt();
                sVal.setIdx(idx);
                sVal.setV(classes.get(j));
                idx++;
            }
            numData.getPtCount().setVal(idx);
            strData.getPtCount().setVal(idx);

            //赋值横坐标数据区域
            String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0)
                    .formatAsString("Sheet1", true);
            cat.getStrRef().setF(axisDataRange);

            //数据区域
            String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
                    .formatAsString("Sheet1", true);
            val.getNumRef().setF(numDataRange);
        }
        return result;
    }

    public static boolean refreshLineChart(Object typeChart, List<?> serList, List<Map<String, String>> dataList, List<String> series, List<String> classes, int position) {
        boolean result = true;
        //更新数据区域
        for (int i = 0; i < serList.size(); i++) {
            CTAxDataSource cat = null;
            CTNumDataSource val = null;
            CTLineSer ser = ((CTLineChart) typeChart).getSerArray(i);

            // 设置系列（excel第一行数据） 用以下这个方式，可以兼容office和wps
            CTSerTx tx = ser.getTx();
            tx.getStrRef().getStrCache().getPtList().get(0).setV(series.get(position - 1));

            // 类别(excel第一列数据)
            cat = ser.getCat();
            // 获取图表的值
            val = ser.getVal();
            // strData.set
            CTStrData strData = cat.getStrRef().getStrCache();
            CTNumData numData = val.getNumRef().getNumCache();
            strData.setPtArray((CTStrVal[]) null); // unset old axis text
            numData.setPtArray((CTNumVal[]) null); // unset old values

            // set model
            long idx = 0;
            for (int j = 0; j < classes.size(); j++) {
                //判断获取的值是否为空
                String value = "0";
                if (new BigDecimal(dataList.get(j).get(series.get(position - 1))) != null) {
                    value = new BigDecimal(dataList.get(j).get(series.get(position - 1))).toString();
                }
                if (!"0".equals(value)) {
                    CTNumVal numVal = numData.addNewPt();//序列值
                    numVal.setIdx(idx);
                    numVal.setV(value);
                }
                //设置类别（）
                CTStrVal sVal = strData.addNewPt();
                sVal.setIdx(idx);
                sVal.setV(classes.get(j));
                idx++;
            }
            numData.getPtCount().setVal(idx);
            strData.getPtCount().setVal(idx);

            //赋值横坐标数据区域
            String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0)
                    .formatAsString("Sheet1", true);
            cat.getStrRef().setF(axisDataRange);

            //数据区域
            String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
                    .formatAsString("Sheet1", true);
            val.getNumRef().setF(numDataRange);
        }
        return result;
    }




    public static void main(String[] args) {
        String path = "E:\\Xlw\\template.docx";
        String outPath = "E:\\Xlw\\templateBak.docx";
        WordUtil.handleWord(path, outPath);
    }
}
