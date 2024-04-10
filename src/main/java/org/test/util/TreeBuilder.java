package org.test.util;

import cn.hutool.core.lang.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: 构建树工具类
 * @Title: BuildTreeUtil
 * @Author xlw
 * @Package com.xlw.test.util
 * @Date 2024/4/10 10:43
 */
public class TreeBuilder<T> {

    /**
     * 父节点分组
     */
    private Map<String, List<T>> parentIdGroup;

    /**
     * id分组
     */
    private Map<String, List<T>> idGroup;

    /**
     * Id字段名
     */
    private String idFieldName;

    /**
     * 父id字段名
     */
    private String parenIdFieldName;

    /**
     * 子字段名
     */
    private String childrenFieldName;

    /**
     * 数据列表
     */
    private List<T> dataList;

    private TreeBuilder() {
    }

    public static <T> TreeBuilder<T> builder() {
        return new TreeBuilder<>();
    }

    public TreeBuilder idFieldName(String idFieldName) {
        Assert.notBlank(idFieldName, "idFieldName字段不能为空");
        this.idFieldName = idFieldName;
        return this;
    }

    public TreeBuilder parenIdFieldName(String parenIdFieldName) {
        Assert.notBlank(parenIdFieldName, "parenIdFieldName字段不能为空");
        this.parenIdFieldName = parenIdFieldName;
        return this;
    }

    public TreeBuilder childrenFieldName(String childrenFieldName) {
        Assert.notBlank(childrenFieldName, "childrenFieldName字段不能为空");
        this.childrenFieldName = childrenFieldName;
        return this;
    }

    public TreeBuilder dataList(List<T> dataList) {
        Assert.notEmpty(dataList, "dataList不能为空");
        this.dataList = dataList;
        return this;
    }

    public List<T> build() {
        Assert.notBlank(idFieldName, "idFieldName字段不能为空");
        Assert.notBlank(parenIdFieldName, "parenIdFieldName字段不能为空");
        Assert.notBlank(childrenFieldName, "childrenFieldName字段不能为空");
        Assert.notEmpty(dataList, "dataList不能为空");
        createParentIdGroup();
        createIdGroup();
        List<T> tree = new ArrayList<>();
        //寻找顶层父节点
        for (Map.Entry<String, List<T>> entry : parentIdGroup.entrySet()) {
            String parentId = entry.getKey();
            //父节点在idGroup组中不存在是顶级父类
            if (!idGroup.containsKey(parentId)) {
                //顶级父类元素list
                List<T> list = entry.getValue();
                for (T t : list) {
                    //添加父类的子节点
                    addChildren(t);
                }
                tree.addAll(list);
            }
        }
        //未找到顶级父节点,平铺返回
        if (tree.isEmpty()) {
            tree = dataList;
        }
        return tree;
    }

    private void addChildren(T t) {
        Class<?> clazz = t.getClass();
        try {
            Field idField = clazz.getDeclaredField(idFieldName);
            idField.setAccessible(true);
            String id = String.valueOf(idField.get(t));
            if (parentIdGroup.containsKey(id)) {
                List<T> childrenList = parentIdGroup.get(id);
                //设置子类
                Field childrenField = clazz.getDeclaredField(childrenFieldName);
                childrenField.setAccessible(true);
                childrenField.set(t, childrenList);
                //递归
                for (T t1 : childrenList) {
                    addChildren(t1);
                }
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建父类组
     */
    private void createParentIdGroup() {
        //将dataList进行parentId进行分组
        parentIdGroup = createGroup(parenIdFieldName);
    }

    /**
     * 创建id组
     */
    private void createIdGroup() {
        idGroup = createGroup(idFieldName);
    }

    /**
     * 创建组
     */
    private Map<String, List<T>> createGroup(String fieldName) {
        return dataList.stream().collect(Collectors.groupingBy(o -> {
            Class clazz = o.getClass();
            try {
                Field field = clazz.getDeclaredField(fieldName);
                Assert.notNull(field, "未找到parenIdFieldName属性");
                //设置私有属性访问
                field.setAccessible(true);
                return String.valueOf(field.get(o));
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }));
    }

}
