package org.test.util.excel.strategy;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

public class ExcelWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {


    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        //boolean needSetWidth = relativeRowIndex != null && (isHead || relativeRowIndex == 0);
        boolean needSetWidth = isHead || CollectionUtil.isNotEmpty(cellDataList);
        if (!needSetWidth) {
            return;
        }
        String value = cellDataList.get(0).getStringValue();
        Integer width = value.length() * 3;
        System.out.println("value:" + value + "/" + "row:" + cell.getRowIndex() + "/cell:" + cell.getColumnIndex());
        if (width != null) {
            width = width * 256;
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), width);
        }
    }
}
