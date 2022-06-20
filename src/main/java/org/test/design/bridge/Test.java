package org.test.design.bridge;

/**
 * @author 肖龙威
 * @date 2022/06/20 10:07
 */
public class Test {

    public static void main(String[] args) {
        //传统创建方法:手机接口 <---- 功能手机 <----- 手机品牌
        //桥接器模式: 手机接口 <---- 功能手机  手机品牌接口 <---- 具体品牌
        //品牌
        Brand huawei = new HuaWei();
        Phone phone = new IntelligencePhon(huawei);
        phone.call();

        Brand xiaomi = new XiaoMi();
        phone = new IntelligencePhon(xiaomi);
        phone.call();
    }
}
