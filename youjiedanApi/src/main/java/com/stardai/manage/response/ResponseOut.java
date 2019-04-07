package com.stardai.manage.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ResponseOut {

	private long orderNo;

	private double amount; //单子原价
	
	private double couponAmount; //优惠券面额

	private String channel;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	private String outNo;

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOutNo() {
		return outNo;
	}

	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}

	

	
	public double getCouponAmount() {
		return couponAmount;
	}

	
	public void setCouponAmount(double couponAmount) {
		this.couponAmount = couponAmount;
	}

	@Override
	public String toString() {
		return "ResponseOut [orderNo=" + orderNo + ", amount=" + amount + ", couponAmount=" + couponAmount
				+ ", channel=" + channel + ", createTime=" + createTime + ", outNo=" + outNo + "]";
	}
	
	

	

}
