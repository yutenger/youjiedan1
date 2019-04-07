package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户接受订单推送表的pojo类
 *
 * @author jokery
 * @create 2017-12-27 10:12
 **/
@Table(name = "yjd_user_push")
public class UserPush {
    @Id
    private Integer id;

    private String userId;

    private String pushTime;

    private Integer pushAble;

    private String followedCityOne;

    private String pushAmount;

    private String pushWebank;

    private String pushIsNative;

    private String createTime;

    private String updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public Integer getPushAble() {
        return pushAble;
    }

    public void setPushAble(Integer pushAble) {
        this.pushAble = pushAble;
    }

    public String getFollowedCityOne() {
        return followedCityOne;
    }

    public void setFollowedCityOne(String followedCityOne) {
        this.followedCityOne = followedCityOne;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserPush{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", pushTime='" + pushTime + '\'' +
                ", pushAble=" + pushAble +
                ", followedCityOne='" + followedCityOne + '\'' +
                ", pushAmount='" + pushAmount + '\'' +
                ", pushWebank='" + pushWebank + '\'' +
                ", pushIsNative='" + pushIsNative + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
