package com.stardai.manage.response;

/**
 * 邀请返利活动的模型
 *
 * @author jokery
 * @create 2018-01-24 10:35
 **/

public class ResponseShareEvent {

    private Integer shareCount;

    private Integer shareMoney;

    private String shareUrl;

    private Integer shareGive;

    private Integer rebate;

    public Integer getRebate() {
        return rebate;
    }

    public void setRebate(Integer rebate) {
        this.rebate = rebate;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getShareMoney() {
        return shareMoney;
    }

    public void setShareMoney(Integer shareMoney) {
        this.shareMoney = shareMoney;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Integer getShareGive() {
        return shareGive;
    }

    public void setShareGive(Integer shareGive) {
        this.shareGive = shareGive;
    }

    @Override
    public String toString() {
        return "ResponseShareUser{" +
                "shareCount=" + shareCount +
                ", shareMoney=" + shareMoney +
                ", shareUrl='" + shareUrl + '\'' +
                ", shareGive=" + shareGive +
                ", rebate=" + rebate +
                '}';
    }

    public ResponseShareEvent() {
    }

    public ResponseShareEvent(Integer shareCount, Integer shareMoney, String shareUrl, Integer shareGive, Integer rebate) {
        this.shareCount = shareCount;
        this.shareMoney = shareMoney;
        this.shareUrl = shareUrl;
        this.shareGive = shareGive;
        this.rebate = rebate;
    }

    public ResponseShareEvent(ResponseShareUser user,Integer rebate) {
        this.rebate = rebate;
        this.shareCount = user.getShareCount();
        this.shareGive = user.getShareGive();
        this.shareMoney = user.getShareMoney();
        this.shareUrl = user.getShareUrl();
    }
}
