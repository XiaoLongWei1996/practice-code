package com.xlw.test.template_demo.util;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.xlw.test.template_demo.config.annotation.ExcelAlias;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xlw
 * @description: hutool Excel工具类
 * @title: HuToolExcelUtil
 * @package com.piaoshui.ncp.util
 * @date 2025/3/12 11:21
 */
public class HuToolExcelUtil {

    /**
     * 读取Excel并转化为list
     * 需要指定返回时元素的类
     *
     * @param path     需要读取的Excel 的路径aa
     * @param beanType 指定的类，
     * @return list
     */
    public static <T> List<T> redaExcel(String path, Class<T> beanType) {
        File file = new File(path);
        try (ExcelReader reader = ExcelUtil.getReader(file)) {
            reader.setHeaderAlias(getHeaderAlias(beanType, true));
            return reader.readAll(beanType);
        }
    }

    /**
     * 阅读 Excel
     *
     * @param inputStream 输入流
     * @param beanType    Bean 类型
     * @return {@link List }<{@link T }>
     */
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> beanType) {
        try (ExcelReader reader = ExcelUtil.getReader(inputStream)) {
            reader.setHeaderAlias(getHeaderAlias(beanType, true));
            return reader.readAll(beanType);
        } finally {
            IoUtil.close(inputStream);
        }
    }

    /**
     * 阅读 Excel
     *
     * @param inputStream 输入流
     * @param headerIndex 标头索引
     * @param startIndex  读取起始索引
     * @param beanType    Bean 类型
     * @return {@link List }<{@link T }>
     */
    public static <T> List<T> readExcel(InputStream inputStream, int headerIndex, int startIndex, Class<T> beanType) {
        try (ExcelReader reader = ExcelUtil.getReader(inputStream)) {
            reader.setHeaderAlias(getHeaderAlias(beanType, true));
            return reader.read(headerIndex, startIndex, beanType);
        } finally {
            IoUtil.close(inputStream);
        }
    }


    /**
     * 将list写入Excel中
     *
     * @param filePath 需要写入的Excel 的路径
     * @param list     写入的内容
     * @param beanType Bean 类型
     */
    public static <T> void writeExcel(String filePath, List<T> list, Class<T> beanType) {
        Map<String, String> headerAlias = getHeaderAlias(beanType, false);
        try (ExcelWriter writer = ExcelUtil.getWriter(filePath)) {
            writer.setHeaderAlias(headerAlias);
            // 写入并刷盘
            writer.write(list).flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void writeExcel(String filePath, List<T> list, int sheetNum, int rowNum, Class<T> beanType) {
        Map<String, String> headerAlias = getHeaderAlias(beanType, false);
        try (ExcelWriter writer = ExcelUtil.getWriter(filePath)) {
            writer.setHeaderAlias(headerAlias);
            // 写入并刷盘
            writer.setSheet(sheetNum).setCurrentRow(rowNum).write(list).flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 追加写入Excel中
     *
     * @param filePath 文件路径
     * @param list     列表
     * @param sheetNum 图纸编号
     * @param rowNum   行号
     * @param beanType Bean 类型
     */
    public static <T> void appendWriteExcel(String filePath, List<T> list, int sheetNum, int rowNum, Class<T> beanType) {
        Map<String, String> headerAlias = getHeaderAlias(beanType, true);
        try (ExcelWriter writer = ExcelUtil.getWriter(filePath)) {
            writer.setHeaderAlias(headerAlias);
            // 写入并刷盘
            writer.setSheet(sheetNum).setCurrentRow(rowNum).write(list).flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得一个类所有 有别名的 字段 和 别名
     *
     * @param beanType 类
     * @param isRead   每个键值对格式为 ("已有的","想改的")
     * @return {@link Map }<{@link String }, {@link String }>
     */
    static <T> Map<String, String> getHeaderAlias(Class<T> beanType, boolean isRead) {
        Map<String, String> headerAlias = new HashMap<String, String>();
        // Hutool 获取字段集合的方法，无需try/catch
        List<Field> fields = Arrays.asList(ReflectUtil.getFields(beanType));
        if (fields.isEmpty()) {
            return headerAlias;
        }
        for (Field field : fields) {
            // 获得每个字段的 @ExcelAlias注解
            com.xlw.test.template_demo.config.annotation.ExcelAlias anno = field.getAnnotation(ExcelAlias.class);
            if (anno == null || "".equals(anno.value())) {
                continue;
            }
            if (isRead) {
                // 读取时 每个键值对格式为:(“别名”, “字段名”)
                headerAlias.put(anno.value(), field.getName());
            } else {
                // 写入时 每个键值对格式为:(“字段名”, “别名”)
                headerAlias.put(field.getName(), anno.value());
            }
        }
        return headerAlias;
    }
}
