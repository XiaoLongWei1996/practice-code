package org.test.design.state;

/**
 * 不及格
 * @author 肖龙威
 * @date 2021/05/18 13:53
 */
public class FlunkState extends State {

    public FlunkState(Judge judge) {
        this.judge = judge;
        this.name = "不及格";
        this.score = 0;
    }

    public FlunkState(State state) {
        judge = state.judge;
        name = "不及格";
        score = state.score;
    }

    @Override
    public void check() {
        if (score >= 80) {
            judge.setState(new NiceState(this));
        } else if (score < 80 && score >= 60) {
            judge.setState(new PassState(this));
        }
        System.out.println(judge.getState().name);
    }
}
