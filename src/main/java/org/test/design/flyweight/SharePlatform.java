package org.test.design.flyweight;

import java.util.ArrayList;
import java.util.List;

/**
 * 享元设计模式:将公共的部门共享,实现代码的复用和内存开支,
 * 所有从该对象获取的数据都是共享的,只存在一份
 * @author 肖龙威
 * @date 2021/05/08 16:39
 */
public class SharePlatform {
    /**
     * 共享集合,在同一个对象内,这个LIST是共享的
     */
    private final List<Transportation> LIST;

    public SharePlatform(){
        LIST = new ArrayList<>();
        LIST.add(new Bicycle("摩拜"));
        LIST.add(new Bicycle("OFO"));
        LIST.add(new Bicycle("青桔"));
    }

    public Transportation getTransportation(){
        return LIST.parallelStream().findAny().get();
    }

    public Integer size(){
        return LIST.size();
    }

}
