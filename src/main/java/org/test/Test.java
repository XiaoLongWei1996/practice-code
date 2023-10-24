package org.test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author 肖龙威
 * @date 2022/04/11 13:23
 */
public class Test {

    Integer i = 1;

    public void getInt() {
        Integer i = 2;
        this.i = i + 1;

    }

    public static void main(String[] args) {
        Test t = new Test();
        System.out.println(t.i);
        t.getInt();
        System.out.println(t.i);

//        String path = "E:\\Xlw\\template.docx";
//        String outPath = "E:\\Xlw\\templateBak.docx";
//        try {
//            InputStream inputStream = new FileInputStream(path);
//            WordDocOperate wordDocOperate = new WordDocOperate(inputStream);
//
//            Map<String, String> data = new HashMap<>();
//            data.put("$dateTime$", "2023年05月");
//            data.put("$lastDate$", "2023-01-05");
//            data.put("$tba$", "15.26");
//            //替换文字
//            wordDocOperate.setPattern("\\$.*\\$");
//            wordDocOperate.replaceText(data);
//            // 加载图表
//            wordDocOperate.loadChart();
//
//            List<String> classis = new ArrayList<>();
//            classis.add("a");
//            classis.add("b");
//            classis.add("c");
//            classis.add("d");
//            classis.add("e");
//            classis.add("f");
//
//            List<String> series = new ArrayList<>();
//            series.add("#1");
//            series.add("#2");
//            series.add("#3");
//
//            List<Map<String, String>> dataItems = new ArrayList<>();
//            Map<String, String> row1 = new HashMap<>();
//            row1.put("#1", "25");
//            row1.put("#2", "13");
//            row1.put("#3", "13");
//            dataItems.add(row1);
//
//            Map<String, String> row2 = new HashMap<>();
//            row2.put("#1", "43");
//            row2.put("#2", "20");
//            row2.put("#3", "20");
//            dataItems.add(row2);
//
//            Map<String, String> row3 = new HashMap<>();
//            row3.put("#1", "16");
//            row3.put("#2", "33");
//            row3.put("#3", "33");
//            dataItems.add(row3);
//
//            Map<String, String> row4 = new HashMap<>();
//            row4.put("#1", "59");
//            row4.put("#2", "20");
//            row4.put("#3", "20");
//            dataItems.add(row4);
//
//            Map<String, String> row5 = new HashMap<>();
//            row5.put("#1", "78");
//            row5.put("#2", "9");
//            row5.put("#3", "9");
//            dataItems.add(row5);
//
//            Map<String, String> row6 = new HashMap<>();
//            row6.put("#1", "92");
//            row6.put("#2", "27");
//            row6.put("#3", "27");
//            dataItems.add(row6);
//
////            wordDocOperate.modifyChartExcel("pie_chart", null, classis, series, dataItems);
////
////            wordDocOperate.modifyChartExcel("bar_chart_1", null, classis, series, dataItems);
////
////            wordDocOperate.modifyChartExcel("bar_chart_2", null, classis, series, dataItems);
//
//            wordDocOperate.modifyChartExcel("mix_chart", "混合统计", classis, series, dataItems);
//
//            wordDocOperate.write(new FileOutputStream(outPath));
//
//            wordDocOperate.close();
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }

    }

    private static LocalDate dateToLocalDate(Date date, long l) {
        LocalDate ld = null;
        Instant instant = date.toInstant();
        ld = LocalDate.from(instant.atZone(ZoneId.of("+8")));
        ld = ld.plusMonths(l);
        return ld;
    }

    public void test() {

    }

}
