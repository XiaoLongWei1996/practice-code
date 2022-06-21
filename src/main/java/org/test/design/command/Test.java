package org.test.design.command;

/**
 * @author 肖龙威
 * @date 2022/06/21 16:16
 */
public class Test {

    public static void main(String[] args) {
        Tv tv = new Tv(); //命令的执行者
        Command command = new TVCommand(tv); //命令
        Invoke invoke = new Invoke(); //命令的调用者
        invoke.setCommand(command);
        invoke.invoke();
    }
}
