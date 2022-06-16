package org.test.load;

/**
 * @author 肖龙威
 * @date 2022/04/10 16:16
 */
public class MyClassLoad extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        return super.findClass(name);
    }
}
