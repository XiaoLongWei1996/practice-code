package org.test.util.excel.strategy;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

public class FormatStrategy extends AbstractCellStyleStrategy {

    @Override
    protected void setHeadCellStyle(CellWriteHandlerContext context) {
        if (context.getHead() && context.getFirstCellData() != null) {
            // 头的策略
            WriteCellStyle hStyle = new WriteCellStyle();
            // 背景设置为红色
            hStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setCharset((int) Font.DEFAULT_CHARSET);
            headWriteFont.setFontHeightInPoints((short)14);
            hStyle.setWriteFont(headWriteFont);

            WriteCellData<?> cellData = context.getFirstCellData();
            WriteCellStyle.merge(hStyle, cellData.getOrCreateStyle());

        }
    }

    @Override
    protected void setContentCellStyle(CellWriteHandlerContext context) {
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 背景绿色
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short)14);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle.merge(contentWriteCellStyle, cellData.getOrCreateStyle());
    }
}
