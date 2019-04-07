package com.stardai.manage.response;

/**
 * @program: ujiedan
 * @Date: 2018/9/25 16:17
 * @Author: Tina
 * @Description:
 */
public class ResponseCouponByGrabOrder {
    //活动累计抢单
    private int orderAmount;
    //累计获得15元券的张数
    private int couponLimit;
    //累计获得50元券的张数
    private int couponNolimit;
    //再抢 XX 单可获得满30-15抢单抵扣券
    private int orderAmountForLimit;
    //再抢 XX 单可获得50无门槛抢单抵扣券
    private int orderAmountFornoLimit;

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getCouponLimit() {
        return couponLimit;
    }

    public void setCouponLimit(int couponLimit) {
        this.couponLimit = couponLimit;
    }

    public int getCouponNolimit() {
        return couponNolimit;
    }

    public void setCouponNolimit(int couponNolimit) {
        this.couponNolimit = couponNolimit;
    }

    public int getOrderAmountForLimit() {
        return orderAmountForLimit;
    }

    public void setOrderAmountForLimit(int orderAmountForLimit) {
        this.orderAmountForLimit = orderAmountForLimit;
    }

    public int getOrderAmountFornoLimit() {
        return orderAmountFornoLimit;
    }

    public void setOrderAmountFornoLimit(int orderAmountFornoLimit) {
        this.orderAmountFornoLimit = orderAmountFornoLimit;
    }

    @Override
    public String toString() {
        return "ResponseCouponByGrabOrder{" +
                "orderAmount=" + orderAmount +
                ", couponLimit=" + couponLimit +
                ", couponNolimit=" + couponNolimit +
                ", orderAmountForLimit=" + orderAmountForLimit +
                ", orderAmountFornoLimit=" + orderAmountFornoLimit +
                '}';
    }
}
