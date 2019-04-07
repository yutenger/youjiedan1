package com.stardai.manage.request;

import javax.validation.constraints.NotNull;

/**
 * 用户修改接收推送时间
 *
 * @author jokery
 * @create 2018-01-03 9:29
 **/

public class RequestPushTime {
    @NotNull(message = "用户ID不能为空")
    private String userId;

    @NotNull(message = "接收推送时间不能为空")
    private String pushTime;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RequestPushTime{" +
                "userId='" + userId + '\'' +
                ", pushTime='" + pushTime + '\'' +
                '}';
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }
}
