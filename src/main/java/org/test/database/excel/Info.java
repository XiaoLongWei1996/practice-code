package org.test.database.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import lombok.Data;


/**
 * 数据库设计文档内容
 * @className: Info
 * @author: xlw
 * @date: 2023/6/2 14:22
 **/
@ColumnWidth(25)
@HeadFontStyle(fontHeightInPoints = 11, fontName = "等线", bold = BooleanEnum.FALSE)
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, borderTop = BorderStyleEnum.NONE ,borderBottom = BorderStyleEnum.NONE, borderRight = BorderStyleEnum.NONE, borderLeft = BorderStyleEnum.NONE, fillForegroundColor = 5)
@ContentFontStyle(fontHeightInPoints = 10, fontName = "微软雅黑")
@Data
public class Info {

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ExcelProperty("表名")
    private String table;

    @ExcelProperty("字段")
    private String field;

    @ExcelProperty("类型")
    private String fieldType;

    @ExcelProperty("非空")
    private Boolean notNull;

    @ExcelProperty("注释")
    private String comment;

    @ExcelProperty("数据来源")
    private String source;

    @ExcelProperty("疑问")
    private String problem;

//    @HeadStyle(fillForegroundColor = 1, borderTop = BorderStyleEnum.NONE, borderLeft = BorderStyleEnum.NONE, borderRight = BorderStyleEnum.NONE, borderBottom = BorderStyleEnum.NONE)
//    @HeadFontStyle(bold = BooleanEnum.FALSE, underline = Font.U_SINGLE, color = 20, fontHeightInPoints = 11, fontName = "等线")
    @ExcelProperty("返回目录")
    private WriteCellData<String> returnHead;
}
