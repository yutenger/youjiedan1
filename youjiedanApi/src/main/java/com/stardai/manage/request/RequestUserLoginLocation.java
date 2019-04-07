package com.stardai.manage.request;

/**
 * @program: ujiedan
 * @Date: 2018/8/28 14:42
 * @Author: Tina
 * @Description:
 */
public class RequestUserLoginLocation {

    private String userId;

    private String ip;

    private String cityPositioning;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCityPositioning() {
        return cityPositioning;
    }

    public void setCityPositioning(String cityPositioning) {
        this.cityPositioning = cityPositioning;
    }

    @Override
    public String toString() {
        return "RequestUserLoginLocation{" +
                "userId='" + userId + '\'' +
                ", ip='" + ip + '\'' +
                ", cityPositioning='" + cityPositioning + '\'' +
                '}';
    }
}
