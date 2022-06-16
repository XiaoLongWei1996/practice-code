package org.test.design.prototype;

import java.io.*;

/**
 * 序列化反序列化对象,实现深拷贝
 * @author 肖龙威
 * @date 2021/04/20 9:45
 */
public class Serialize {

    public static Object deepClone(Object object){
        Object o = null;
        try {
            //把对象写到字节数组中
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
            objectOutput.writeObject(object);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            //把直接数组读成对象
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
            o = objectInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return o;
    }
}
