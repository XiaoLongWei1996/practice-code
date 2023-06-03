package org.test.database.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;

/**
 * excel首页
 * @className: Head
 * @author: xlw
 * @date: 2023/6/2 13:31
 **/
@ColumnWidth(25)
@HeadFontStyle(fontHeightInPoints = 11, fontName = "等线", bold = BooleanEnum.FALSE)
@ContentFontStyle(fontHeightInPoints = 10, fontName = "微软雅黑")
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, borderTop = BorderStyleEnum.NONE ,borderBottom = BorderStyleEnum.NONE, borderRight = BorderStyleEnum.NONE, borderLeft = BorderStyleEnum.NONE, fillForegroundColor = 44)
@Data
public class Head {

    @ExcelProperty("数据表")
    private String table;

    /**
     * 超链接
     */
    @ContentFontStyle(underline = Font.U_SINGLE, color = 12)
    @ExcelProperty("数据表名+详情跳转")
    private WriteCellData<String> tableComment;

    @ExcelProperty("说明")
    private String description;

    @ExcelProperty("问题")
    private String problem;

    @ExcelProperty("调整")
    private String adjust;

}
