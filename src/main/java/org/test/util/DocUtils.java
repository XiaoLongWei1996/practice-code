package org.test.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import com.spire.doc.Document;
import com.spire.doc.OutlineLevel;
import com.spire.doc.Section;
import com.spire.doc.collections.DocumentObjectCollection;
import com.spire.doc.collections.ParagraphCollection;
import com.spire.doc.collections.SectionCollection;
import com.spire.doc.collections.StyleCollection;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.fields.TextRange;
import com.spire.doc.formatting.CharacterFormat;
import com.spire.doc.interfaces.IStyle;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.Objects;

/**
 * @author 肖龙威
 * @date 2022/10/10 17:03
 */
public class DocUtils {

    public static void formatDoc(File file) {
        Assert.notNull(file, "file为空");
        Assert.isTrue(file.exists(), "文件不存在");
        Assert.isTrue("doc".equals(FileNameUtil.extName(file)) || "docx".equals(FileNameUtil.extName(file)), "文件格式不正确");
        BufferedInputStream inputStream = FileUtil.getInputStream(file);
        Document document = new Document(inputStream);
        parseStyle(new File("D:\\img\\template.doc"), document);
        SectionCollection sections = document.getSections();
        for (int i = 0; i < sections.getCount(); i++) {
            Section section = sections.get(i);
            ParagraphCollection paragraphs = section.getParagraphs();
            //设置页边距
            section.getPageSetup().getMargins().setTop((float) (2.54 * 28.35));
            section.getPageSetup().getMargins().setBottom((float) (2.54 * 28.35));
            section.getPageSetup().getMargins().setLeft((float) (3.17 * 28.35));
            section.getPageSetup().getMargins().setRight((float) (3.17 * 28.35));
            //处理段落
            handleParagraphs(paragraphs);
        }
        document.saveToFile("D:\\img\\a.doc");
        document.close();
    }

    private static void handleParagraphs(ParagraphCollection paragraphCollection) {
        int count = paragraphCollection.getCount();
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            Paragraph paragraph = paragraphCollection.get(i);
            String listText = paragraph.getListText();
            String text = listText + "" + paragraph.getText();
            //处理标题
            handleTitle(paragraph, text);
            //处理小项
            handleItem(paragraph, text);
            //处理正文
            handleText(paragraph);
            //处理文本格式
            handleTextFormat(paragraph);
        }
    }

    private static void handleTitle(Paragraph paragraph, String text) {
        //处理标题
        if (!ReUtil.isMatch("^\\d\\.\\d.+$", text) || paragraph.getStyleName().contains("ARISTITLE")) {
            return;
        }
        //处理题目如:1.0xx
        String sequence = ReUtil.get("^(([\\d\\.]+).+)$", text, 2);
        String[] level = sequence.split("\\.");
        int l = level.length;
        paragraph.getListFormat().removeList();
        if (Objects.equals(level[l - 1], "0")) {
            l -= 1;
            setParagraph(paragraph, 6, OutlineLevel.Level_1, "ARISTITLE" + l);
            setCharacterFormat(paragraph, "宋体", 12, true);
        } else {
            setParagraph(paragraph, 6, OutlineLevel.Level_1, "ARISTITLE" + l);
            setCharacterFormat(paragraph, "宋体", 12, true);
        }
    }

    private static void handleText(Paragraph paragraph) {
        //处理文本
        if (!"程序正文".equals(paragraph.getStyleName())) {
            return;
        }
        setParagraph(paragraph, 6, null, "ARISTEXT");
        setCharacterFormat(paragraph, "宋体", 12, false);
    }

    private static void handleItem(Paragraph paragraph, String text) {
        //处理小项,如1）、（1）
        if (!ReUtil.isMatch("^\\(?\\w\\).*$", text) || paragraph.getFormat().getOutlineLevel().getValue() == OutlineLevel.Body.getValue()) {
            return;
        }
        System.out.println(text);
        setParagraph(paragraph, 6, paragraph.getFormat().getOutlineLevel(), "ARISTITLE" + (paragraph.getFormat().getOutlineLevel().getValue() + 1));
        setCharacterFormat(paragraph, "宋体", 12, false);
    }

    private static void handleTextFormat(Paragraph paragraph) {
        //处理文本格式
        if (!"ARISTEXT".equals(paragraph.getStyleName())) {
            return;
        }
        paragraph.getStyle().getParagraphFormat().setOutlineLevel(OutlineLevel.Body);
    }

    private static void setCharacterFormat(Paragraph paragraph, String fontName, float fontSize, boolean isBold) {
        DocumentObjectCollection childObjects = paragraph.getChildObjects();
        int count = childObjects.getCount();
        if (count == 0) {
            return;
        }
        for (int i = 0; i < count; i++) {
            if (childObjects.get(i) instanceof TextRange) {
                TextRange tr = (TextRange) childObjects.get(i);
                CharacterFormat characterFormat = tr.getCharacterFormat();
                characterFormat.setFontName(fontName);
                characterFormat.setFontSize(fontSize);
                if (isBold) {
                    characterFormat.setBold(true);
                }
            }
        }
    }

    private static void setParagraph(Paragraph paragraph, float line, OutlineLevel outlineLevel, String styleName) {
        //设置段前段后间距
        paragraph.getFormat().setBeforeAutoSpacing(false);
        paragraph.getFormat().setBeforeSpacing(line);
        paragraph.getFormat().setAfterAutoSpacing(false);
        paragraph.getFormat().setAfterSpacing(line);
        if (ObjectUtil.isNotEmpty(outlineLevel)) {
            paragraph.getStyle().getParagraphFormat().setOutlineLevel(outlineLevel);
        }
        //设置样式
        paragraph.applyStyle(styleName);
    }

    //解析模板样式
    private static void parseStyle(File template, Document document) {
        BufferedInputStream inputStream = null;
        Document d = null;
        try {
            inputStream = FileUtil.getInputStream(template);
            d = new Document(inputStream);
            StyleCollection styles = d.getStyles();
            int count = styles.getCount();
            if (count == 0) {
                return;
            }
            for (int i = 0; i < count; i++) {
                IStyle iStyle = styles.get(i);
                if (iStyle.getName().startsWith("ARIS")) {
                    document.getStyles().add(iStyle);
                }
            }
        } finally {
            IoUtil.close(inputStream);
//            if (ObjectUtil.isNotEmpty(d)) {
//                d.close();
//            }
        }
    }

    private static OutlineLevel selectOutlineLevel(int l) {
        switch (l) {
            case 1:
                return OutlineLevel.Level_1;
            case 2:
                return OutlineLevel.Level_2;
            case 3:
                return OutlineLevel.Level_3;
            case 4:
                return OutlineLevel.Level_4;
            case 5:
                return OutlineLevel.Level_5;
            case 6:
                return OutlineLevel.Level_6;
            case 7:
                return OutlineLevel.Level_7;
            case 8:
                return OutlineLevel.Level_8;
            case 9:
                return OutlineLevel.Level_9;
            default:
                return OutlineLevel.Body;
        }
    }
}
