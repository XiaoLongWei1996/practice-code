package org.test.util;

import java.util.Arrays;

/**
 * @description: int和byte转换
 * @Title: IntByteConverterUtil
 * @Author xlw
 * @Package org.test.util
 * @Date 2024/4/29 15:52
 */
public class IntByteConverterUtil {

    // 将int转换为byte数组,一个int占4个byte
    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value
        };
    }

    // 将byte数组转换回int
    public static int byteArrayToInt(byte[] bytes) {
        if(bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("The byte array must be non-null and have a length of 4");
        }
        return (bytes[0] << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
    }

    public static void main(String[] args) {
        byte[] data = {78, 89, 73, 77};
        int i = 1314474317;
        System.out.println(byteArrayToInt(data));
        byte[] bytes = intToByteArray(i);
        System.out.println(Arrays.toString(bytes));

        //[2, -1, -82, 2, 98, 22, -128, -119, 15, 0, 11, 104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100]
        //[2, -1, -82, 2, 98, 22, -128, -119, 15, 0, 11]
    }
}
