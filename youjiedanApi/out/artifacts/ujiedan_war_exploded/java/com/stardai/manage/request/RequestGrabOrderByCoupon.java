package com.stardai.manage.request;

/**
 * 
 * @author Tina
 * 2018年4月13日
 */
public class RequestGrabOrderByCoupon {
	
	private long orderNo; //单子订单号

	private double price;//单子原价
	
	private double couponPrice; //使用优惠券后的价格
	
	private String userId;
	
	private String couponId; //优惠券id

	private String dateTime;
	
	private int isOriginalPrice;//是否是原价单，1表示原价单，0表示非原价单
	
//	private String passWord;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	/*
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	*/

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	
	/**
	 * @return the couponId
	 */
	public String getCouponId() {
		return couponId;
	}

	/**
	 * @param couponId the couponId to set
	 */
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	/**
	 * @return the couponPrice
	 */
	public double getCouponPrice() {
		return couponPrice;
	}

	/**
	 * @param couponPrice the couponPrice to set
	 */
	public void setCouponPrice(double couponPrice) {
		this.couponPrice = couponPrice;
	}
	
	

	public int getIsOriginalPrice() {
		return isOriginalPrice;
	}

	public void setIsOriginalPrice(int isOriginalPrice) {
		this.isOriginalPrice = isOriginalPrice;
	}

	@Override
	public String toString() {
		return "RequestGrabOrderByCoupon [orderNo=" + orderNo + ", price=" + price + ", couponPrice=" + couponPrice
				+ ", userId=" + userId + ", couponId=" + couponId + ", dateTime=" + dateTime + ", isOriginalPrice="
				+ isOriginalPrice + "]";
	}
	
	

}
