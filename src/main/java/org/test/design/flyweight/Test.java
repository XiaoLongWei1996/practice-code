package org.test.design.flyweight;

/**
 * @author 肖龙威
 * @date 2022/06/20 16:48
 */
public class Test {

    public static void main(String[] args) {
        SharePlatform sharePlatform = new SharePlatform();
        sharePlatform.getTransportation().run();
        sharePlatform.getTransportation().run();
        sharePlatform.getTransportation().run();
        sharePlatform.getTransportation().run();
        sharePlatform.getTransportation().run();
    }
}
