package org.test.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * word工具类
 *
 * @author Xlw
 * @date 2023/05/18
 */
public class WordUtil {

    private static final String PATTERN = "\\$.*\\$";

    public static void handleWord(String path , String outPath) {
        try(InputStream in =new FileInputStream(path); OutputStream out = new FileOutputStream(outPath)) {
            XWPFDocument document = new XWPFDocument(in);
            Map<String, String> data = new HashMap<>();
            data.put("$dateTime$", "2023年05月");
            data.put("$lastDate$", "2023-01-05");
            data.put("$tba$", "15.26");
            replaceText(document, data);
            document.write(out);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 替换文本
     */
    private static void replaceText(XWPFDocument document, Map<String, String> data) {
        //替换页眉
        replaceHeaderText(document, data);
        //替换段落
        replaceParagraphsText(document, data);
        //替换表
        replaceTableText(document, data);
        //替换页脚
        replaceFooterText(document, data);

    }

    /**
     * 更换标题文本
     *
     * @param document 文档
     * @param data     数据
     */
    private static void replaceHeaderText(XWPFDocument document, Map<String, String> data) {
        //获取页眉
        List<XWPFHeader> headerList = document.getHeaderList();
        if (CollectionUtil.isNotEmpty(headerList)) {
            for (XWPFHeader xwpfHeader : headerList) {
                List<XWPFParagraph> paragraphs = xwpfHeader.getParagraphs();
                handleParagraphs(paragraphs, data);
            }
        }
    }

    /**
     * 取代段落文本
     *
     * @param document 文档
     * @param data     数据
     */
    private static void replaceParagraphsText(XWPFDocument document, Map<String, String> data) {
        //获取段落集合
        List<XWPFParagraph> paragraphList = document.getParagraphs();
        handleParagraphs(paragraphList, data);
    }

    /**
     * 替换表文本
     *
     * @param document 文档
     * @param data     数据
     */
    private static void replaceTableText(XWPFDocument document, Map<String, String> data) {
        //获取表格
        List<XWPFTable> tables = document.getTables();
        if (CollectionUtil.isNotEmpty(tables)) {
            for (XWPFTable table : tables) {
                List<XWPFTableRow> rows = table.getRows();
                if (CollectionUtil.isEmpty(rows)) {
                    continue;
                }
                for (XWPFTableRow row : rows) {
                    List<XWPFTableCell> tableCells = row.getTableCells();
                    for (XWPFTableCell tableCell : tableCells) {
                        List<XWPFParagraph> paragraphs = tableCell.getParagraphs();
                        handleParagraphs(paragraphs, data);
                    }
                }

            }
        }
    }

    /**
     * 替换页脚文本
     *
     * @param document 文档
     * @param data     数据
     */
    private static void replaceFooterText(XWPFDocument document, Map<String, String> data) {
        //获取页脚
        List<XWPFFooter> footerList = document.getFooterList();
        if (CollectionUtil.isNotEmpty(footerList)) {
            for (XWPFFooter xwpfFooter : footerList) {
                List<XWPFParagraph> paragraphs = xwpfFooter.getParagraphs();
                handleParagraphs(paragraphs, data);
            }
        }
    }

    private static void handleParagraphs(List<XWPFParagraph> paragraphs, Map<String, String> data) {
        if (CollectionUtil.isEmpty(paragraphs)) {
            return;
        }
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                //替换文字
                String newText = replaceText(text, data);
                if (StrUtil.isNotBlank(newText)) {
                    run.setText(newText, 0);
                }
            }
        }
    }

    private static String replaceText(String text, Map<String, String> data) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(text);
        boolean flag = false;
        while (matcher.find()) {
            String key = matcher.group(0);
            String value = data.get(key);
            if (StrUtil.isBlank(value)) {
                continue;
            }
            text = text.replace(key, value);
            flag = true;
        }
        return flag ? text : null;
    }

    public static void main(String[] args) {
        String path = "E:\\Xlw\\template.docx";
        String outPath = "E:\\Xlw\\templateBak.docx";
        WordUtil.handleWord(path, outPath);

    }
}
