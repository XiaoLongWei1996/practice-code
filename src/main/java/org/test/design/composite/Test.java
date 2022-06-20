package org.test.design.composite;

/**
 * @author 肖龙威
 * @date 2022/06/20 15:35
 */
public class Test {

    public static void main(String[] args) {
        Tree tree = new Tree();
        tree.add(new Leaf("叶子1"));
        tree.add(new Leaf("叶子2"));
        tree.opration();
    }
}
