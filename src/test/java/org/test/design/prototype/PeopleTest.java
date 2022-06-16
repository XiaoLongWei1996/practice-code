package org.test.design.prototype;

import junit.framework.TestCase;

import java.util.Date;

/**
 * @author 肖龙威
 * @date 2021/04/29 10:35
 */
public class PeopleTest extends TestCase {

    public void testTestClone() throws CloneNotSupportedException {
        //浅拷贝
        People people = new People();
        people.setAge(12);
        people.setName("小明");
        people.setBirth(new Date());
        Animal animal = new Animal();
        animal.setName("狗");
        animal.setAge(2);
        animal.setBirth(new Date());
        people.setAnimal(animal);
        System.out.println("people:"+people);
        People people1 = (People) people.clone();
        System.out.println("people1:"+people1);
        System.out.println(people == people1); //false
        System.out.println(people.getAnimal() == people1.getAnimal()); //true

        //深拷贝
        People people2 = (People) Serialize.deepClone(people);
        System.out.println(people2 == people);  //false
        System.out.println(people2.getAnimal() == people.getAnimal()); //false
    }

}