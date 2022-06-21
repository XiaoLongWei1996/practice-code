package org.test.design.command;

/**
 * 命令设计模式:将命令的执行者和命令的调用者分开,他们的中间层就是命令
 * @author 肖龙威
 * @date 2021/05/17 18:00
 */
public interface Command {

    void execute();
}
