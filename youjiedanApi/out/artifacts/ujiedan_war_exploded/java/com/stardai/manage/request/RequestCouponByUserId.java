package com.stardai.manage.request;



/**
 * @Description 获取优惠券列表所需要的参数
 * @author Tina
 * @date 2018/4/11
 */
public class RequestCouponByUserId {

	private String userId;
	
	private int couponStatus;

	private int page = 0;

	private int pageSize = 10;
	
	private String nowTime;
	
	private String couponCode;
	
	private String couponId;
	
	private int getType;  //1：领券中心获取的优惠券，2：积分兑换获取的优惠券
	
	private int creditValue; // 兑换所需要的积分
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	
	public int getCouponStatus() {
		return couponStatus;
	}

	
	public void setCouponStatus(int couponStatus) {
		this.couponStatus = couponStatus;
	}

	
	
	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}

	
	
	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	
	

	public int getGetType() {
		return getType;
	}

	public void setGetType(int getType) {
		this.getType = getType;
	}

	
	
	public int getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(int creditValue) {
		this.creditValue = creditValue;
	}

	@Override
	public String toString() {
		return "RequestCouponByUserId [userId=" + userId + ", couponStatus=" + couponStatus + ", page=" + page
				+ ", pageSize=" + pageSize + ", nowTime=" + nowTime + ", couponCode=" + couponCode + ", couponId="
				+ couponId + ", getType=" + getType + ", creditValue=" + creditValue + "]";
	}

	


}
