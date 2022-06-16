package org.test.design.command;

/**
 * 具体的命令
 * @author 肖龙威
 * @date 2021/05/18 8:47
 */
public class TVCommand implements Command{

    private Tv tv;

    public TVCommand(Tv tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.execute();
    }
}
