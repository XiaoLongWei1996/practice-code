package org.test.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: WordDocOperate
 * @Author xlw
 * @Package org.test.util
 * @Date 2023/5/20 22:02
 * @description: word文档操作类
 */
public class WordDocOperate {

    //操作word文档对象
    private final XWPFDocument DOCUMENT;

    private String pattern;

    private Map<String, XWPFChart> chartMap;

    public WordDocOperate(File word) {
        try (InputStream in = new FileInputStream(word)) {
            DOCUMENT = new XWPFDocument(in);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WordDocOperate(InputStream input) {
        try {
            DOCUMENT = new XWPFDocument(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 替换文本
     *
     * @param data 数据
     */
    public void replaceText(Map<String, String> data) {
        Assert.notBlank(pattern, "请先设置替换文本样式");
        // 替换页眉
        replaceHeaderText(DOCUMENT, data);
        // 替换段落
        replaceParagraphsText(DOCUMENT, data);
        // 替换表
        replaceTableText(DOCUMENT, data);
        // 替换页脚
        replaceFooterText(DOCUMENT, data);
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * 加载图表
     */
    public void loadChart() {
        chartMap = new HashMap<>();
        List<XWPFChart> charts = DOCUMENT.getCharts();
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

    public void modifyChartExcel(String key,
                                 String title,
                                 List<String> classes,
                                 List<String> series,
                                 List<Map<String, String>> dataItems) {
        Assert.notEmpty(this.chartMap, "请先执行loadChart()");
        XWPFChart chart = chartMap.get(key);
        if (ObjectUtil.isNull(chart)) {
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
        refreshChartInnerExcel(chart, classes, series, dataItems);

        if (key.contains("pie")) {
            CTPieChart pieChart = plotArea.getPieChartArray(0);
            List<CTPieSer> serList = pieChart.getSerList();// 获取柱状图单位
            // 刷新word显示的数据
            refreshPieChart(pieChart, serList, dataItems, series, classes, 1);
        } else if (key.contains("bar")) {
            CTBarChart barChart = plotArea.getBarChartArray(0);
            List<CTBarSer> BarSerList = barChart.getSerList();  // 获取柱状图单位
            // 刷新word显示的数据
            refreshBarChart(barChart, BarSerList, dataItems, series, classes, 1);
        } else if (key.contains("mix")) {
            CTBarChart barChart = plotArea.getBarChartArray(0); // 获取柱状图单位
            List<CTBarSer> BarSerList = barChart.getSerList();
            refreshBarChart(barChart, BarSerList, dataItems, series, classes, 1);
            CTLineChart lineChart = plotArea.getLineChartArray(0); //获取折线图单位
            List<CTLineSer> lineSerList = lineChart.getSerList();
            refreshLineChart(lineChart, lineSerList, dataItems, series, classes, 3);
        }

    }

    public void write(OutputStream output) {
        try {
            DOCUMENT.write(output);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            close();
        }
    }

    public void close() {
        if (DOCUMENT != null) {
            try {
                DOCUMENT.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 更换标题文本
     *
     * @param document 文档
     * @param data     数据
     */
    private void replaceHeaderText(XWPFDocument document, Map<String, String> data) {
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
    private void replaceParagraphsText(XWPFDocument document, Map<String, String> data) {
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
    private void replaceTableText(XWPFDocument document, Map<String, String> data) {
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
    private void replaceFooterText(XWPFDocument document, Map<String, String> data) {
        // 获取页脚
        List<XWPFFooter> footerList = document.getFooterList();
        if (CollectionUtil.isNotEmpty(footerList)) {
            for (XWPFFooter xwpfFooter : footerList) {
                List<XWPFParagraph> paragraphs = xwpfFooter.getParagraphs();
                handleParagraphs(paragraphs, data);
            }
        }
    }

    /**
     * 处理段落
     *
     * @param paragraphs 段落
     * @param data       数据
     */
    private void handleParagraphs(List<XWPFParagraph> paragraphs, Map<String, String> data) {
        if (CollectionUtil.isEmpty(paragraphs)) {
            return;
        }
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                // 替换文字
                String newText = replaceWords(text, data);
                if (StrUtil.isNotBlank(newText)) {
                    run.setText(newText, 0);
                }
            }
        }
    }

    private String replaceWords(String text, Map<String, String> data) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        Pattern pattern = Pattern.compile(this.pattern);
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

    private String extractChartKey(XWPFChart chart) {
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
        }
        return key;
    }

    /**
     * 设置图表标题
     *
     * @param ctChart ct图
     * @param title   标题
     */
    private void setChartTitle(CTChart ctChart, String title) {
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
     * 刷新图表内部的excel
     *
     * @param chart     图
     * @param classes   类
     * @param series    系列
     * @param dataItems 数据项
     */
    private void refreshChartInnerExcel(
            XWPFChart chart,
            List<String> classes,
            List<String> series,
            List<Map<String, String>> dataItems) {
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
                        if ("".equals(data.get(cell))) {
                            sheet.createRow(i + 1).createCell(j).setCellValue("");
                        } else {
                            sheet.createRow(i + 1).createCell(j).setCellValue(data.get(cell) == null ? 0.00 : Double.valueOf(data.get(cell)));
                        }
                    }
                } else {
                    if (j == 0) {
                        sheet.getRow(i + 1).createCell(j).setCellValue(classes.get(i) == null ? "" : classes.get(i));
                    } else {
                        if ("".equals(data.get(cell))) {
                            sheet.getRow(i + 1).createCell(j).setCellValue("");
                        } else {
                            sheet.getRow(i + 1).createCell(j).setCellValue(data.get(cell) == null ? 0.00 : Double.valueOf(data.get(cell)));
                        }
                    }
                }
            }
        }
        chart.setWorkbook(wb);
    }

    /**
     * 刷新饼图
     *
     * @param typeChart 类型图
     * @param serList   ser列表
     * @param dataList  数据列表
     * @param series    系列
     * @param classes   类
     * @param position  位置,系列的第几列
     * @return boolean
     */
    private void refreshPieChart(Object typeChart, List<?> serList, List<Map<String, String>> dataList, List<String> series, List<String> classes, int position) {

        boolean result = true;
        //更新数据区域
        for (int i = 0; i < serList.size(); i++) {
            CTAxDataSource cat = null;
            CTNumDataSource val = null;
            CTPieSer ser = ((CTPieChart) typeChart).getSerArray(i);

            // 设置系列（excel第一行数据） 用以下这个方式，可以兼容office和wps
            CTSerTx tx = ser.getTx();
            tx.getStrRef().getStrCache().getPtList().get(0).setV(series.get(position - 1));

            // 类别(excel第一列数据)
            cat = ser.getCat();
            // 获取图表的值
            val = ser.getVal();

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
    }

    /**
     * 刷新柱形图
     *
     * @param typeChart 类型图
     * @param serList   ser列表
     * @param dataList  数据列表
     * @param series    系列
     * @param classes   类
     * @param position  位置,系列的第几列
     * @return boolean
     */
    private void refreshBarChart(Object typeChart, List<?> serList, List<Map<String, String>> dataList, List<String> series, List<String> classes, int position) {
        //更新数据区域
        for (int i = 0; i < serList.size(); i++) {
            CTAxDataSource cat = null;
            CTNumDataSource val = null;
            CTBarSer ser = ((CTBarChart) typeChart).getSerArray(i);

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
    }

    /**
     * 刷新折线图
     *
     * @param typeChart 类型图
     * @param serList   ser列表
     * @param dataList  数据列表
     * @param series    系列
     * @param classes   类
     * @param position  位置
     * @return boolean
     */
    public void refreshLineChart(Object typeChart, List<?> serList, List<Map<String, String>> dataList, List<String> series, List<String> classes, int position) {
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
    }

}
