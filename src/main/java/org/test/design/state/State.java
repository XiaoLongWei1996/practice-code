package org.test.design.state;

/**
 * 状态设计模式:把有状态的对象中的复杂状态逻辑提取出来,允许状态对象在发生改变时改变状态
 * 把对象的改变跟状态的改变解耦,原本是对象改变,状态也会改变;现在是对象改变,状态在状态之间改变
 * 状态不同对象执行的方法实现也会不同
 *
 * @author 肖龙威
 * @date 2021/05/18 13:34
 */
public abstract class State {

    protected String name;

    protected Judge judge;

    protected Integer score;

    //检查judge对象该处于什么状态
    public abstract void check();

    public void add(Integer score) {
        this.score += score;
        System.out.println("添加:" + score + "/总分:" + this.score);
        check(); //检查judge该处于什么状态
    }


    public Judge getJudge() {
        return judge;
    }

    public void setJudge(Judge judge) {
        this.judge = judge;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
