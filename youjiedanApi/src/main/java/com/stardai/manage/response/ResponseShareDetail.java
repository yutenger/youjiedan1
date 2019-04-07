package com.stardai.manage.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @program: ujiedan
 * @Date: 2018/8/16 22:55
 * @Author: Tina
 * @Description:
 */
public class ResponseShareDetail {
    //邀请人userId
    private String userId;
    //邀请人手机号
    private String mobileNumber;
    //邀请人注册时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    //邀请人当前认证状态
    private int approveStatus;
    //邀请获取的星币
    private int getMoney;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public int getGetMoney() {
        return getMoney;
    }

    public void setGetMoney(int getMoney) {
        this.getMoney = getMoney;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ResponseShareDetail{" +
                "userId='" + userId + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", createTime=" + createTime +
                ", approveStatus=" + approveStatus +
                ", getMoney=" + getMoney +
                '}';
    }
}
