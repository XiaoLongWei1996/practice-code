package org.test.design.state;

/**
 * @author 肖龙威
 * @date 2021/05/18 13:34
 */
public class Judge {

    private State state;

    public Judge() {
        //默认状态
        this.state = new FlunkState(this);
    }

    public void addScore(Integer score) {
        state.add(score);  //状态不同执行的方法实现也会不同
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
