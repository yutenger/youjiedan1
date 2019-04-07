package com.stardai.manage.pojo;

public class SnatchRecord {
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getAvailable5() {
        return available5;
    }

    public void setAvailable5(Integer available5) {
        this.available5 = available5;
    }

    public Integer getAvailable10() {
        return available10;
    }

    public void setAvailable10(Integer available10) {
        this.available10 = available10;
    }

    public Integer getAvailable50() {
        return available50;
    }

    public void setAvailable50(Integer available50) {
        this.available50 = available50;
    }

    private String createDate;
    private Integer count;
    private Integer available5;
    private Integer available10;
    private Integer available50;
}
