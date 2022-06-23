package org.test.design.state;

/**
 * @author 肖龙威
 * @date 2022/06/23 10:06
 */
public class Test {

    public static void main(String[] args) {
        Judge judge = new Judge();
        judge.addScore(70);
        judge.addScore(50);
        judge.addScore(100);
    }
}
