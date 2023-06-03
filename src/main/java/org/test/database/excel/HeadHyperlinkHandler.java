package org.test.database.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: HeadHyperlinkHandler
 * @Author xlw
 * @Package org.test.database.excel
 * @Date 2023/6/3 16:47
 * @description: excel头部超链接处理
 */
public class HeadHyperlinkHandler implements CellWriteHandler {

    /**
     * 列链接MAP
     */
    private final Map<Integer, String> COLUMN_HYPERLINK_MAP;

    private final HyperlinkType HYPERLINKTYPE;

    public HeadHyperlinkHandler(HyperlinkType hyperlinktype) {
        COLUMN_HYPERLINK_MAP = new HashMap<>();
        HYPERLINKTYPE = hyperlinktype;
    }

    /**
     * 列添加超链接
     *
     * @param column    列
     * @param hyperlink 超链接
     */
    public void addColumnHyperlink(Integer column, String hyperlink) {
        COLUMN_HYPERLINK_MAP.put(column, hyperlink);
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (!isHead) {
            return;
        }
        //当前列
        int currentColumn = cell.getColumnIndex();
        if (!COLUMN_HYPERLINK_MAP.containsKey(currentColumn)) {
            return;
        }
        String hl = COLUMN_HYPERLINK_MAP.get(currentColumn);
        CreationHelper createHelper = writeSheetHolder.getSheet().getWorkbook().getCreationHelper();
        Hyperlink hyperlink = createHelper.createHyperlink(HYPERLINKTYPE);
        hyperlink.setAddress(hl);
        cell.setHyperlink(hyperlink);
    }

}
