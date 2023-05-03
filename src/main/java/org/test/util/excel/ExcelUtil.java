package org.test.util.excel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.test.util.excel.strategy.ExcelWidthStyleStrategy;
import org.test.util.excel.strategy.FormatStrategy;

import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static String PATH = "E:\\temp\\";

    public static void exportSingle(List<?> data, String sheetName) {
        Assert.isTrue(CollectionUtil.isNotEmpty(data), "数据为空");
        String fileName = PATH + "test" + RandomUtil.randomNumbers(3) + ".xlsx";
        EasyExcel.write(fileName, data.get(0).getClass()).sheet(0, sheetName).doWrite(data);
    }

    public static void exportMultipart(List<?> data) {
        String fileName = PATH + "test" + RandomUtil.randomNumbers(3) + ".xlsx";
        ExcelWriter ew = EasyExcel.write(fileName).build();
        List<List<String>> heads = createHeads("id号码", "名字", "生日", "年龄", "班级名称");
        WriteSheet ws = EasyExcel.writerSheet(0, "测试1")
                .head(heads)
                .registerWriteHandler(new FormatStrategy())
                .registerWriteHandler(new ExcelWidthStyleStrategy())
                .build();
        ew.write(data, ws);
        ew.close();
    }

    public static List<List<String>> createHeads(String ...head) {
        Assert.notEmpty(head, "参数为空");
        List<List<String>> heads = new ArrayList<>();
        for (String s : head) {
            List<String> h = new ArrayList<>(1);
            h.add(s);
            heads.add(h);
        }
        return heads;
    }

}
