package org.test;

import cn.hutool.core.io.FileUtil;
import com.spire.doc.Document;
import com.spire.doc.Section;
import com.spire.doc.collections.ParagraphCollection;
import com.spire.doc.collections.SectionCollection;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.formatting.ListFormat;
import org.junit.Before;
import org.junit.Test;
import org.test.design.prototype.Animal;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public void test01() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> {
            int i = 1;
            i = i/0;
            System.out.println(i);
        });

        CompletableFuture<Void> cf2 = cf1.exceptionally(e -> {
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println(cf2.get());
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

    }

    public void doc() {
        BufferedInputStream inputStream = FileUtil.getInputStream("D:\\qq\\qq_data\\704926476\\FileRecv\\PI-FZ-210.01 管理制度、管理细则模板.doc");
        Document document = new Document(inputStream);
        SectionCollection sections = document.getSections();
        for (int i = 0; i < sections.getCount(); i++) {
            Section section = sections.get(i);
            ParagraphCollection paragraphs = section.getParagraphs();
            for (int j = 0; j < paragraphs.getCount(); j++) {
                Paragraph paragraph = paragraphs.get(j);
                ListFormat listFormat = paragraph.getListFormat();
                System.out.println(listFormat.getListType().getValue() + "------" + paragraph.getListText() + "" + paragraph.getText());
                //System.out.println(paragraph.getListText() + paragraph.getText());
//                DocumentObjectCollection childObjects = paragraph.getChildObjects();
//                for (int i1 = 0; i1 < childObjects.getCount(); i1++) {
//                    if (childObjects.get(i1) instanceof TextRange) {
//                        TextRange tr = (TextRange) childObjects.get(i1);
//                        CharacterFormat characterFormat = tr.getCharacterFormat();
//                        System.out.println(characterFormat.getFontName() + "---" + characterFormat.getFontNameBidi()
//                        + "---" + characterFormat.getFontNameFarEast());
//
//                    }
//                }
            }
        }
    }

}

