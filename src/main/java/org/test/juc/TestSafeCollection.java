package org.test.juc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 测试安全集合
 * @author 肖龙威
 * @date 2022/06/23 15:28
 */
public class TestSafeCollection {

    public static void main(String[] args) {
        //不安全的数组list
        List<String> list = new ArrayList<>();
        //安全的数组list
        List<String> vector = new Vector();
        //将不安全的list转为安全的list
        List<String> safeList = Collections.synchronizedList(list);
        //juc里面的安全list,写复制;读写分离
        List<String> list1 = new CopyOnWriteArrayList<>();

        //不安全的set
        Set<String> set = new HashSet<>();
        //安全的set
        Set<String> safeSet = new CopyOnWriteArraySet<>();
        //不安全set转化成安全set
        Set<String> set1 = Collections.synchronizedSet(set);

        //不安全map
        Map<String, Object> map = new HashMap<>();
        //安全的map
        Map<String, Object> table = new Hashtable<>();
        Map<String, Object> safeMap = new ConcurrentHashMap<>();
        //不安全map转安全map
        Map<String, Object> map1 = Collections.synchronizedMap(map);

    }
}
