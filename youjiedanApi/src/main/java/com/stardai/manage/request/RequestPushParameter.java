package com.stardai.manage.request;

/**
 * @program: ujiedan
 * @Date: 2018/8/20 10:58
 * @Author: Tina
 * @Description:
 */
public class RequestPushParameter {

    private String userId;

    private String pushAmount;
    private  String pushWebank;

    private String pushIsNative;
    // 1:贷款金额区间 2：微粒贷 3：本地户籍
    private int pushType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPushAmount() {
        return pushAmount;
    }

    public void setPushAmount(String pushAmount) {
        this.pushAmount = pushAmount;
    }

    public String getPushWebank() {
        return pushWebank;
    }

    public void setPushWebank(String pushWebank) {
        this.pushWebank = pushWebank;
    }

    public String getPushIsNative() {
        return pushIsNative;
    }

    public void setPushIsNative(String pushIsNative) {
        this.pushIsNative = pushIsNative;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    @Override
    public String toString() {
        return "RequestPushParameter{" +
                "userId='" + userId + '\'' +
                ", pushAmount='" + pushAmount + '\'' +
                ", pushWebank='" + pushWebank + '\'' +
                ", pushIsNative='" + pushIsNative + '\'' +
                ", pushType=" + pushType +
                '}';
    }
}
