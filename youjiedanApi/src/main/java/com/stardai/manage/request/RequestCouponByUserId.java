package com.stardai.manage.request;



/**
 * @Description 获取优惠券列表所需要的参数
 * @author Tina
 * @date 2018/4/11
 */
public class RequestCouponByUserId {

	private String userId;

	private double costMoney; //用户抢单时，单子花费的金额
	
	private int couponStatus; //1：未使用，2：已使用，3：已过期

	private Integer couponChoice; //0:抢单无门槛抵扣券 1:抢单券，包括无门槛，满减券 2：所有的优惠券，包括抢单券，充值券

	private int page = 0;

	private int pageSize = 10;
	
	private String nowTime;
	
	private String couponCode;
	
	private String couponId;
	
	private int getType;  //1：领券中心获取的优惠券，2：积分兑换获取的优惠券
	
	private int creditValue; // 兑换所需要的积分

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	private Integer couponType;//优惠券类型：1 充值抵扣，2 抢单抵扣

	public Integer getAvaliable() {
		return avaliable;
	}

	public void setAvaliable(Integer avaliable) {
		this.avaliable = avaliable;
	}

	private Integer avaliable;//是否可用  0代表可用(我的优惠券)  1代表不可用(历史优惠券)

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

	public Integer getCouponChoice() {
		return couponChoice;
	}

	public void setCouponChoice(Integer couponChoice) {
		this.couponChoice = couponChoice;
	}

	public double getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(double costMoney) {
		this.costMoney = costMoney;
	}

	@Override
	public String toString() {
		return "RequestCouponByUserId{" +
				"userId='" + userId + '\'' +
				", costMoney=" + costMoney +
				", couponStatus=" + couponStatus +
				", couponChoice=" + couponChoice +
				", page=" + page +
				", pageSize=" + pageSize +
				", nowTime='" + nowTime + '\'' +
				", couponCode='" + couponCode + '\'' +
				", couponId='" + couponId + '\'' +
				", getType=" + getType +
				", creditValue=" + creditValue +
				", couponType=" + couponType +
				'}';
	}
}
