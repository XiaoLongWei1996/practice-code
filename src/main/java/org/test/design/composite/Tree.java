package org.test.design.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 肖龙威
 * @date 2021/05/17 9:04
 */
public class Tree implements Component {

    List<Component> leafs = new ArrayList<>();

    public void add(Component component) {
        leafs.add(component);
    }

    public void remove(Integer index) {
        leafs.remove(index);
    }

    @Override
    public void opration() {
        leafs.forEach((e) -> {
            e.opration();
        });
    }
}

