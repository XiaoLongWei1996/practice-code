package org.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 肖龙威
 * @date 2022/04/11 13:23
 */
public class Test {

    /**
     * 将json数组转成json Linked
     *
     * @param jsonArray json数组
     * @return {@link JSONObject}
     */
    public static String toJSONLinked(String jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            throw new IllegalArgumentException("jsonArray is null");
        }
        //解析json数组
        List<Node> nodes = JSONArray.parseArray(jsonArray, Node.class);
        //根据key的长度分组，上一层是下一层的父类
        Map<Integer, List<Node>> group = nodes.stream().collect(Collectors.groupingBy(n -> String.valueOf(n.getKey()).length()));
        //排序group中的key
        Integer[] keys = group.keySet().stream().sorted().toArray(Integer[]::new);
        List<Node> result = null;
        for (int i = keys.length - 2, j = keys.length - 1; i >= 0; i--, j--) {
            //父类节点
            List<Node> parent = group.get(keys[i]);
            //子类节点
            List<Node> children= group.get(keys[j]);
            addNodeChildren(parent, children);
            result = parent;
        }

        return result.size() > 1 ? JSONObject.toJSONString(result) : JSONObject.toJSONString(result.get(0));
    }

    /**
     * 添加节点子节点
     *
     * @param parent   父类节点
     * @param children 子类节点
     */
    private static void addNodeChildren(List<Node> parent, List<Node> children) {
        parent.stream().forEach(p -> {
            Iterator<Node> iterator = children.iterator();
            while (iterator.hasNext()) {
                Node child = iterator.next();
                if (String.valueOf(child.getKey()).startsWith(String.valueOf(p.getKey()))) {
                    //添加子类
                    p.getChildren().add(child);
                    //移除已被添加的子类
                    iterator.remove();
                }
            }
        });
    }

    /**
     * 节点
     *
     * @author Xlw
     * @date 2024/02/19
     */
    @Data
    @AllArgsConstructor
    public static class Node {

        /**
         * key
         */
        private Integer key;

        /**
         * 值
         */
        private String value;

        /**
         * 子类
         */
        private List<Node> children;

        public Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public List<Node> getChildren() {
            return children == null ? children = new ArrayList<>() : children;
        }
    }

    @Builder
    @Data
    static class MyNode {
        private Integer id;
        private Integer parentId;
        private String value;
        private List<Node> children;
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

    }


}
