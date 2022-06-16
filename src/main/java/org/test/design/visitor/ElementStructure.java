package org.test.design.visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 肖龙威
 * @date 2021/05/20 15:42
 */
public class ElementStructure {

    private List<Element> elements = new ArrayList<>();

    public void add(Element element){
        elements.add(element);
    }

    public void remove(Element e) {
        elements.remove(e);
    }

    public void access(Visitor visitor) {
        for (Element element : elements) {
            element.access(visitor);
        }
    }
}
