package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "yjd_sys_area")
public class OrderCityList {
    @Id
    private Integer areaId;
    //城市首字母
    private String firstLetter;
    //城市编码
    private String areaCode;
    //城市名称
    private String areaName;
    
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public String getFirstLetter() {
		return firstLetter;
	}
	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	@Override
	public String toString() {
		return "OrderCityList [areaId=" + areaId + ", firstLetter=" + firstLetter + ", areaCode=" + areaCode
				+ ", areaName=" + areaName + "]";
	}
   
    
}
