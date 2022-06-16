package org.test;

import org.junit.Before;
import org.junit.Test;
import org.test.design.prototype.Animal;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private String document_size;
    Animal animal;

    @Before
    public void before() {
        animal = new Animal();
        animal.setName("小明");
        animal.setAge(12);
        animal.setBirth(new Date());
    }

    @Test
    public void test01() {
        a:for (int i = 0; i < 10; i++) {
            System.out.println("i=" + i);
            for (int j = 0; j < 5; j++) {
                System.out.println("       j=" + j);
                if (j == 3) {
                    break a;
                }
            }
        }
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {

        System.out.println(animal);
        Class clazz = Animal.class;

        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                try {
                    Object invoke = method.invoke(animal, null);
                    System.out.println(invoke);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void reflex() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Class<Animal> clazz = Animal.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (!field.getName().equals("serialVersionUID")) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
                Method method = propertyDescriptor.getReadMethod();
                Object o = method.invoke(animal, null);
                System.out.println(o);
            }
        }
    }

    @Test
    public void Test() throws Exception {
        Date date = new Date();
        LocalDateTime dateTime = LocalDateTime.now();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime dateTime1 = instant.atZone(zoneId).toLocalDateTime();
        System.out.println(dateTime);

    }

}

