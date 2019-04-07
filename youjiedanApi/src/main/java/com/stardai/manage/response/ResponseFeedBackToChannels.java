package com.stardai.manage.response;

/**
 * 反馈给云禾的数据模型(暂时不用)
 *
 * @author jokery
 * @create 2018-01-19 9:13
 **/

public class ResponseFeedBackToChannels {
    private String mobile;
    private  String buyTime;
    private Double buyMoney;
    private String callbackKey;
    private Integer valid;
    public ResponseFeedBackToChannels() {
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public Double getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(Double buyMoney) {
        this.buyMoney = buyMoney;
    }

    public String getCallbackKey() {
        return callbackKey;
    }

    public void setCallbackKey(String callbackKey) {
        this.callbackKey = callbackKey;
    }

    public ResponseFeedBackToChannels(String mobile, String buyTime, Double buyMoney, Integer valid) {
        this.mobile = mobile;
        this.buyTime = buyTime;
        this.buyMoney = buyMoney;
        this.valid = valid;
    }
}
