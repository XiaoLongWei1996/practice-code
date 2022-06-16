package org.test.design.state;

/**
 * @author 肖龙威
 * @date 2021/05/18 13:56
 */
public class NiceState extends State {

    public NiceState(State state) {
        judge = state.judge;
        name = "优秀";
        score = state.score;
    }

    @Override
    public void check() {
        if (score < 80 && score >= 60) {
            judge.setState(new PassState(this));
        }else if (score < 60) {
            judge.setState(new FlunkState(this));
        }
        System.out.println(judge.getState().name);
    }
}
