package org.test.design.state;

/**
 * 状态设计模式:把有状态的对象中的复杂状态逻辑提取出来,允许状态对象在发生改变时改变状态
 * @author 肖龙威
 * @date 2021/05/18 13:34
 */
public abstract class State {

    protected String name;

    protected Judge judge;

    protected Integer score;

    public abstract void check();

    public void add(Integer score) {
        this.score += score;
        System.out.println("添加:" + score + "/总分:" + this.score);
        check();
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
