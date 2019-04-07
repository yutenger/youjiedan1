package com.stardai.manage.response;

import com.stardai.manage.pojo.AppVersionInfo;
import com.stardai.manage.pojo.ShellScreenMaterial;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author yax
 * @create 2019-01-16 11:11
 **/
public class ResponseScreenInfo {
    //默认4 表示没有弹屏 1 表示版本更新 2表示特殊人群活动 3表示所有人群活动
    private int screenType;
    private AppVersionInfo appVersionInfo;
    private ShellScreenMaterial specialEvent;
    private List<ShellScreenMaterial> events;
    public ResponseScreenInfo(){
        this.screenType=4;
    }
    public ResponseScreenInfo(AppVersionInfo appVersionInfo){
     this.screenType=1;
     this.appVersionInfo=appVersionInfo;
    }
    public ResponseScreenInfo(List<ShellScreenMaterial> events){
        this.screenType=3;
        this.events=events;
    }
    public ResponseScreenInfo(ShellScreenMaterial specialEvent){
        this.screenType=2;
        this.specialEvent=specialEvent;
    }
    public ShellScreenMaterial getSpecialEvent() {
        return specialEvent;
    }

    public void setSpecialEvent(ShellScreenMaterial specialEvent) {
        this.specialEvent = specialEvent;
    }

    public int getScreenType() {
        return screenType;
    }

    public void setScreenType(int screenType) {
        this.screenType = screenType;
    }

    public AppVersionInfo getAppVersionInfo() {
        return appVersionInfo;
    }

    public void setAppVersionInfo(AppVersionInfo appVersionInfo) {
        this.appVersionInfo = appVersionInfo;
    }

    public List<ShellScreenMaterial> getEvents() {
        return events;
    }

    public void setEvents(List<ShellScreenMaterial> events) {
        this.events = events;
    }
}
