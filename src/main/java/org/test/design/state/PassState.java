package org.test.design.state;

/**
 * @author 肖龙威
 * @date 2021/05/18 13:55
 */
public class PassState extends State {

    public PassState(State state) {
        judge = state.judge;
        name = "及格";
        score = state.score;
    }

    @Override
    public void check() {
        if (score >= 80) {
            judge.setState(new NiceState(this));
        }else if (score < 60) {
            judge.setState(new FlunkState(this));
        }
        System.out.println(judge.getState().name);
    }
}
