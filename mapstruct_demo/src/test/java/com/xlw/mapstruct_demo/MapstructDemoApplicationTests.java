package com.xlw.mapstruct_demo;

import cn.idev.excel.FastExcel;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.listener.ReadListener;
import com.xlw.mapstruct_demo.entity.SaveZhxxBO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MapstructDemoApplicationTests {

    @Test
    void contextLoads() {
        String path = "D:\\qyzh.xlsx";
        FastExcel.read(path, new ReadListener<SaveZhxxBO>() {
            @Override
            public void invoke(SaveZhxxBO data, AnalysisContext context) {
                System.out.println(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }).sheet(0).head(SaveZhxxBO.class).headRowNumber(3).doRead();
    }

}
