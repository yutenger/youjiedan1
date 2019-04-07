package com.stardai.manage.response;

/**
 * @program: ujiedan
 * @Date: 2018/8/29 10:07
 * @Author: Tina
 * @Description:
 */
public class ResponseGrabLimit {
    //用户抢单异常时，限制用户N小时内不能抢单
    private int limitHour;
    //用户一小时抢n单后被拉入抢单异常名单
    private int limitHourCount;
    //抢单间隔，单位秒
    private int limitSecond;
    //连续抢单的数量
    private int limitSecondCount;
    //限制当日抢单城市数量
    private int limitCity;

    public int getLimitRefund() {
        return limitRefund;
    }

    public void setLimitRefund(int limitRefund) {
        this.limitRefund = limitRefund;
    }

    //用户退单自动失败时间
    private int limitRefund;

    public int getLimitHour() {
        return limitHour;
    }

    public void setLimitHour(int limitHour) {
        this.limitHour = limitHour;
    }

    public int getLimitHourCount() {
        return limitHourCount;
    }

    public void setLimitHourCount(int limitHourCount) {
        this.limitHourCount = limitHourCount;
    }

    public int getLimitSecond() {
        return limitSecond;
    }

    public void setLimitSecond(int limitSecond) {
        this.limitSecond = limitSecond;
    }

    public int getLimitSecondCount() {
        return limitSecondCount;
    }

    public void setLimitSecondCount(int limitSecondCount) {
        this.limitSecondCount = limitSecondCount;
    }

    public int getLimitCity() {
        return limitCity;
    }

    public void setLimitCity(int limitCity) {
        this.limitCity = limitCity;
    }

    @Override
    public String toString() {
        return "ResponseGrabLimit{" +
                "limitHour=" + limitHour +
                ", limitHourCount=" + limitHourCount +
                ", limitSecond=" + limitSecond +
                ", limitSecondCount=" + limitSecondCount +
                ", limitCity=" + limitCity +
                '}';
    }
}
