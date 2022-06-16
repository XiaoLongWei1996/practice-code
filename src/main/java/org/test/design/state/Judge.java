package org.test.design.state;

/**
 * @author 肖龙威
 * @date 2021/05/18 13:34
 */
public class Judge {

    private State state;

    public Judge() {
        this.state = new FlunkState(this);
    }

    public void addScore(Integer score) {
        state.add(score);
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
