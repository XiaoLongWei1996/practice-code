package org.test.util;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 构建树结构工具类
 * @Title: BuildTreeUtil
 * @Author xlw
 * @Package org.test.util
 * @Date 2024/3/28 20:31
 */
public class BuildTreeUtil<T> {

    /**
     * 包含父子关系的节点数据(id,parentId)
     */
    List<T> nodeList;

    /**
     * 对象主键(对象 id)
     */
    private String id = "id";

    /**
     * 对象的父id
     */
    private String parentId = "parentId";

    /**
     * 孩子名字
     */
    private String childrenName = "children";

    private BuildTreeUtil() {

    }

    /**
     * 子类字段名 (List<this> children)
     */
    public BuildTreeUtil<T> setNodeList(List<T> nodeList) {
        this.nodeList = nodeList;
        return this;
    }

    public BuildTreeUtil<T> setId(String id) {
        this.id = id;
        return this;
    }

    public BuildTreeUtil<T> setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }


    public BuildTreeUtil<T> setChildrenName(String childrenName) {
        this.childrenName = childrenName;
        return this;
    }

    public static <T> BuildTreeUtil builder() {
        return new BuildTreeUtil<T>();
    }

    /**
     * 构建前端所需要树结构
     *
     * @return 树结构列表
     */
    public List<T> build() throws NoSuchFieldException, IllegalAccessException {
        List<T> returnList = new ArrayList<T>();
        //所有对象的 id 列表
        List<String> idList = nodeList.stream().map(data -> {
            try {
                Field declaredField = data.getClass().getDeclaredField(id);
                declaredField.setAccessible(true);
                return String.valueOf(declaredField.get(data));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        for (T node : nodeList) {
            String parentId;
            Field declaredField = node.getClass().getDeclaredField(this.parentId);
            declaredField.setAccessible(true);
            parentId = String.valueOf(declaredField.get(node));
            //查找顶级父类
            if (parentId != null && !idList.contains(parentId)) {
                //查找顶级父类的子类
                recursionFn(nodeList, node);
                //将设置好的子类加入待返回列表
                returnList.add(node);
            }
        }
        //如果没有顶级父类（循环链表），平铺返回
        if (returnList.isEmpty()) {
            returnList = nodeList;
        }
        //最后返回树状列表
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<T> nodeList, T node) throws NoSuchFieldException, IllegalAccessException {
        // 得到子节点列表
        List<T> childList = getChildList(nodeList, node);
        Field declaredField = node.getClass().getDeclaredField(childrenName);
        declaredField.setAccessible(true);
        declaredField.set(node, childList);
        for (T tChild : childList) {
            if (hasChild(nodeList, tChild)) {
                recursionFn(nodeList, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<T> getChildList(List<T> nodeList, T node) throws NoSuchFieldException, IllegalAccessException {
        List<T> childList = new ArrayList<T>();
        for (T n : nodeList) {
            Field nParentIdField = n.getClass().getDeclaredField(this.parentId);
            Field nodeIdField = node.getClass().getDeclaredField(this.id);
            nParentIdField.setAccessible(true);
            nodeIdField.setAccessible(true);
            String nParentId = String.valueOf(nParentIdField.get(n));
            String nodeId = String.valueOf(nodeIdField.get(node));
            //如果列表中有属于顶级节点的子节点则将该节点加入待返回列表中
            if (StrUtil.isNotBlank(nParentId) && nParentId.equals(nodeId)) {
                childList.add(n);
            }
        }
        return childList;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<T> nodeList, T node) throws NoSuchFieldException, IllegalAccessException {
        return getChildList(nodeList, node).size() > 0;

    }


}
