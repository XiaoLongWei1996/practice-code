package com.xlw.mapstruct_demo.util;


import cn.idev.excel.FastExcel;
import cn.idev.excel.read.listener.ReadListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author xlw
 * @description: fast excel 工具类
 * @title: FastExcelUtil
 * @package com.xlw.mapstruct_demo.util
 * @date 2025/3/25 20:02
 */
public class FastExcelUtil {

    /**
     * 一次读取Excel
     *
     * @param filePath    文件路径
     * @param headerClass header 类
     * @param sheetIndex  Sheet Index （工作表索引）
     * @return {@link List }<{@link T }>
     */
    public static <T> List<T> readExcel(String filePath, Class<T> headerClass, int sheetIndex, int headerIndex) {
        return FastExcel.read(filePath).head(headerClass).sheet(sheetIndex).headRowNumber(headerIndex).doReadSync();
    }

    /**
     * 阅读 Excel
     *
     * @param inputStream 输入流
     * @param headerClass header 类
     * @param sheetIndex  Sheet Index （工作表索引）
     * @param headerIndex 头部索引
     * @return {@link List }<{@link T }>
     */
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> headerClass, int sheetIndex, int headerIndex) {
        return FastExcel.read(inputStream).head(headerClass).sheet(sheetIndex).headRowNumber(headerIndex).doReadSync();
    }

    /**
     * 阅读 Excel
     *
     * @param filePath    文件路径
     * @param sheetIndex  Sheet Index （工作表索引）
     * @param headerIndex 标头索引
     * @return {@link List }<{@link Map }<{@link String }, {@link Object }>>
     */
    public static List<Map<String, Object>> readExcel(String filePath, int sheetIndex, int headerIndex) {
        return FastExcel.read(filePath).sheet(sheetIndex).headRowNumber(headerIndex).doReadSync();
    }

    /**
     * 阅读 Excel
     *
     * @param filePath     文件路径
     * @param sheetIndex   Sheet Index （工作表索引）
     * @param headerIndex  标头索引
     * @param readListener 读取侦听器
     */
    public static <T> void readExcel(String filePath, int sheetIndex, int headerIndex, ReadListener<T> readListener) {
        FastExcel.read(filePath, readListener).sheet(sheetIndex).headRowNumber(headerIndex).doRead();
    }

    /**
     * 编写 Excel
     *
     * @param filePath    文件路径
     * @param sheetName   工作表名称
     * @param data        数据
     * @param headerClass header 类
     */
    public static <T> void writeExcel(String filePath, String sheetName, List<T> data, Class<T> headerClass) {
        FastExcel.write(filePath).head(headerClass).sheet(sheetName).doWrite(data);
    }

    /**
     * 编写 Excel
     *
     * @param outputStream 输出流
     * @param sheetName    工作表名称
     * @param data         数据
     */
    public static <T> void writeExcel(OutputStream outputStream, String sheetName, List<T> data) {
        FastExcel.write(outputStream).sheet(sheetName).doWrite(data);
    }

    /**
     * 填写 Excel
     *
     * @param templateFilePath 模板文件路径
     * @param filePath         文件路径
     * @param data             数据
     */
    public static <T> void fillExcel(String templateFilePath, String filePath, List<T> data) {
        FastExcel.write(filePath).withTemplate(templateFilePath).sheet().doFill(data);
    }

    /**
     * 填写 Excel
     *
     * @param templateFilePath 模板文件路径
     * @param outputStream     输出
     * @param data             数据
     * @param sheetName        工作表名称
     */
    public static <T> void fillExcel(String templateFilePath, OutputStream outputStream, List<T> data, String sheetName) {
        FastExcel.write(outputStream).withTemplate(templateFilePath).sheet(sheetName).doFill(data);
    }

    public static void main(String[] args) {

    }

}
