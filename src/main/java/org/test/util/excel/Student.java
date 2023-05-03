package org.test.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.converters.date.DateStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@AllArgsConstructor
@Builder
public class Student {

    @ExcelProperty("id号码")
    private String id;

    @ExcelProperty("名字")
    private String name;

    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ExcelProperty(value = "生日", converter = DateStringConverter.class)
    private Date birthday;

    @ExcelProperty(value = "年龄")
    private String age;

    @ExcelProperty("班级名称")
    private String className;
}
