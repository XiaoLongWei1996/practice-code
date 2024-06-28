package org.test.study;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description: 抽奖实现方式
 * @Title: Lottery
 * @Author xlw
 * @Package org.test.study
 * @Date 2024/6/24 15:32
 */
public class Lottery {

    /**
     * 盲盒列表
     */
    private List<BlindBox> blindBoxes;

    public Lottery() {
        //创建盲盒
        this.blindBoxes = new ArrayList<>();
        //添加盲盒商品及获得权重
        this.blindBoxes.add(new BlindBox(1, "铅笔", 60));
        this.blindBoxes.add(new BlindBox(2, "笔记本", 20));
        this.blindBoxes.add(new BlindBox(4, "耳机", 8));
        this.blindBoxes.add(new BlindBox(5, "手机", 2));
        this.blindBoxes.add(new BlindBox(3, "书包", 10));
        //根据权重进行排序
        this.blindBoxes.sort((o1, o2) -> o1.getWeight() - o2.getWeight());
        System.out.println("排序后的列表：" + this.blindBoxes);
    }

    /**
     * 获取盲盒
     *
     * @return {@link BlindBox }
     */
    public BlindBox getBlindBox() {
        //1.计算每个盲盒的获取概率
        List<Double> probabilities = new ArrayList<>();
        //获取总权重
        Integer sum = blindBoxes.stream().map(BlindBox::getWeight).reduce((o1, o2) -> o1 + o2).get();
        System.out.println("总权重：" + sum);
        //商品的概率区间
        double probabilityArea = 0;
        //计算商品获取的概率区间
        for (int i = 0; i < blindBoxes.size(); i++) {
            BlindBox blindBox = blindBoxes.get(i);
            double probability = (double) blindBox.getWeight() / sum;
            System.out.println(blindBox.getName() + "的概率：" + probability);
            //前一个概率区间
            double preProbabilityArea = probabilityArea;
            probabilityArea += probability;
            System.out.println(blindBox.getName() + "的概率区间：" + preProbabilityArea + "~" + probabilityArea);
            probabilities.add(probabilityArea);
        }
        System.out.println("所有商品概率区间：" + probabilities);
        //2.随机生成一个随机数，计算该随机数在哪个区间内，返回该区间内对应的盲盒
        ThreadLocalRandom random = ThreadLocalRandom.current();
        //获取随机概率
        double rate = random.nextDouble(0, 1);
        System.out.println("获取随机概率：" + rate);
        //计算随机概率在哪个区间内
        probabilities.add(rate);
        //排序概率集合
        probabilities.sort(Double::compareTo);
        System.out.println("概率区间数组：" + probabilities);
        //获取概率的索引
        int idx = probabilities.indexOf(rate);
        System.out.println("获取概率的索引：" + idx);
        return blindBoxes.get(idx);
    }

    public static void main(String[] args) {
        Lottery lottery = new Lottery();
        System.out.println("抽中商品：" + lottery.getBlindBox());
    }

}
