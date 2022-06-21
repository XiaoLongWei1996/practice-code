package org.test.design.command;

/**
 * 调用者,调用命令
 * @author 肖龙威
 * @date 2021/05/18 9:15
 */
public class Invoke {

    private Command command;

    public void invoke() {
        command.execute();
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
