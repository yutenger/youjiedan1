package com.stardai.manage.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @program: ujiedan
 * @Date: 2018/8/16 10:15
 * @Author: Tina
 * @Description: 抽奖活动用户获取的奖品列表
 */
public class ResponseLotteryList {

    private String userId;
    //获取的奖品名称
    private String lotteryName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private int couponAmount;


    private int couponFullreduction;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getCouponFullreduction() {
        return couponFullreduction;
    }

    public void setCouponFullreduction(int couponFullreduction) {
        this.couponFullreduction = couponFullreduction;
    }

    @Override
    public String toString() {
        return "ResponseLotteryList{" +
                "userId='" + userId + '\'' +
                ", lotteryName='" + lotteryName + '\'' +
                ", createTime=" + createTime +
                ", couponAmount=" + couponAmount +
                ", couponFullreduction=" + couponFullreduction +
                '}';
    }
}
