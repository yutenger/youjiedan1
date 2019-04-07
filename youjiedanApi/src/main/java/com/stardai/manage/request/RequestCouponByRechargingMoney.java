package com.stardai.manage.request;

/**
 * @program: ujiedan
 * @Date: 2018/8/14 13:11
 * @Author: Tina
 * @Description:
 */
public class RequestCouponByRechargingMoney {

    private int page = 0;

    private int pageSize = 10;

    private String userId;

    private int money;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "RequestCouponByRechargingMoney{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", userId='" + userId + '\'' +
                ", money=" + money +
                '}';
    }
}
