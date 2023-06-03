package org.test.database.excel;

import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.List;

/**
 * @Title: HeadStyleHandler
 * @Author xlw
 * @Package org.test.database.excel
 * @Date 2023/6/3 21:16
 * @description: 头样式
 */
public class HeadStyleHandler implements CellWriteHandler {

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (!isHead) {
            return;
        }
        for (WriteCellData<?> writeCellData : cellDataList) {
            if (!"返回目录".equals(writeCellData.getStringValue())) {
                return;
            }
            //设置样式
            WriteCellStyle writeCellStyle = writeCellData.getOrCreateStyle();
            writeCellStyle.setFillPatternType(FillPatternTypeEnum.SOLID_FOREGROUND.getPoiFillPatternType());
            writeCellStyle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
            writeCellStyle.setBorderBottom(BorderStyle.NONE);
            writeCellStyle.setBorderLeft(BorderStyle.NONE);
            writeCellStyle.setBorderRight(BorderStyle.NONE);
            writeCellStyle.setBorderTop(BorderStyle.NONE);
            //设置字体样式
            WriteFont writeFont = new WriteFont();
            writeFont.setUnderline(Font.U_SINGLE);
            writeFont.setFontHeightInPoints((short) 11);
            writeFont.setFontName("等线");
            writeFont.setColor(IndexedColors.VIOLET.getIndex());
            writeFont.setBold(BooleanEnum.FALSE.getBooleanValue());
            writeCellStyle.setWriteFont(writeFont);
        }

    }
}
