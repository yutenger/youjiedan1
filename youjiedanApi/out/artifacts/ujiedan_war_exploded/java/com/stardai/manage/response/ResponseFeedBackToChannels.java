package com.stardai.manage.response;

/**
 * 反馈给云禾的数据模型(暂时不用)
 *
 * @author jokery
 * @create 2018-01-19 9:13
 **/

public class ResponseFeedBackToChannels {
    private String mobile;
    private  String buy_time;
    private Double buy_money;

    public ResponseFeedBackToChannels() {
    }

    public ResponseFeedBackToChannels(String mobile, String buy_time, Double buy_money) {
        this.mobile = mobile;
        this.buy_time = buy_time;
        this.buy_money = buy_money;
    }

    @Override
    public String toString() {
        return "ResponseFeedBackToChannel{" +
                "mobile='" + mobile + '\'' +
                ", buy_time='" + buy_time + '\'' +
                ", buy_money=" + buy_money +
                '}';
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBuy_time() {
        return buy_time;
    }

    public void setBuy_time(String buy_time) {
        this.buy_time = buy_time;
    }

    public Double getBuy_money() {
        return buy_money;
    }

    public void setBuy_money(Double buy_money) {
        this.buy_money = buy_money;
    }
}
