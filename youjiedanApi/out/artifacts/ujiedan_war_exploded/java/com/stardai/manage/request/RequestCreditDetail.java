package com.stardai.manage.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Description 积分明细表里的信息
 * @author Tina
 * @date 2018/4/11
 */
public class RequestCreditDetail {

	private String userId;
	
	private int creditValue; //用户消耗或者获取的积分
	
	private String creditPathway;//用户消耗或者获取积分的来源
	
	private String creditDetail;//积分兑换的来源明细，优惠券码或者实物码
	
	private int type;//1为消耗的积分 2为获取的积分

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createDatetime; //创建时间
	
	private int channelType;// 1 : 充值送等额积分,2 : 首次浏览活动超过5秒获得5积分,3 : 分享邀请一次1积分，每日上限20积分
	
	private int eventId ;//浏览活动获取积分时，记录活动的id
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(int creditValue) {
		this.creditValue = creditValue;
	}

	public String getCreditPathway() {
		return creditPathway;
	}

	public void setCreditPathway(String creditPathway) {
		this.creditPathway = creditPathway;
	}

	public String getCreditDetail() {
		return creditDetail;
	}

	public void setCreditDetail(String creditDetail) {
		this.creditDetail = creditDetail;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}



	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}
	
	

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	@Override
	public String toString() {
		return "RequestCreditDetail [userId=" + userId + ", creditValue=" + creditValue + ", creditPathway="
				+ creditPathway + ", creditDetail=" + creditDetail + ", type=" + type + ", createDatetime="
				+ createDatetime + ", channelType=" + channelType + ", eventId=" + eventId + "]";
	}

	

	
	
	
	
	
}
