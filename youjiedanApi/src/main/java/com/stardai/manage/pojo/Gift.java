package com.stardai.manage.pojo;

/**
 * @program: ujiedan
 * @Date: 2018/8/15 10:16
 * @Author: Tina
 * @Description: 抽奖奖品的实体类
 */
public class Gift {
    //奖品Id
    private int id;
    //奖品名称
    private String name;
    //获奖概率
    private double prob;
    //奖品优惠券需要满足的金额
    private int couponFullreduction;
    //奖品优惠券面额
    private int couponAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public int getCouponFullreduction() {
        return couponFullreduction;
    }

    public void setCouponFullreduction(int couponFullreduction) {
        this.couponFullreduction = couponFullreduction;
    }

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }


    @Override
    public String toString() {
        return "Gift{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prob=" + prob +
                ", couponFullreduction=" + couponFullreduction +
                ", couponAmount=" + couponAmount +
                '}';
    }
}
