package org.test.design.command;

/**
 * 命令设计模式:将命令发起者和命令执行者分开,两者通过命令进行沟通
 * @author 肖龙威
 * @date 2021/05/17 18:00
 */
public interface Command {

    void execute();
}
