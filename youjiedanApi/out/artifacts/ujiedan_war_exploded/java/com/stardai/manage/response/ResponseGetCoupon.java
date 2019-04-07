package com.stardai.manage.response;

/**
 * 领券中心获取的券的信息
 * @author Tina
 * 2018年5月25日
 */

public class ResponseGetCoupon {
	
	private String couponCode;//优惠券码，用来标识是哪种优惠券
	
	private int couponAmount; // 优惠券面额 
	
	private String couponTypeName; // 优惠券类型：充值抵扣券，抢单抵扣券
	
	private String couponFormName; // 优惠券消费形式：无门槛使用
	
	private int couponGetStatus; // 优惠券状态，0：未开始，1：已领取，2：未领取或者是立即兑换，3：已结束
	
    private String beginTime; //如果领券未开始，要把开始时间返回前端
    
    private int leftPercent; //剩余未领的百分比，剩余50%就把50传过去
    
    private int couponInitialCount; //优惠券发放数量
    
    private int couponCount; //优惠券剩余数量
    
    private String getBeginTime; //领券的开始时间,后台取到的具体时间，要进行转化
    
    private int couponForm; //优惠券花费形式：1 打折， 2 满减， 3 无门槛
    
    private int couponType; //优惠券类型：1 充值抵扣，2 抢单抵扣
    
    private int creditValue; // 兑换优惠券所需要的积分

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public int getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(int couponAmount) {
		this.couponAmount = couponAmount;
	}

	public String getCouponTypeName() {
		return couponTypeName;
	}

	public void setCouponTypeName(String couponTypeName) {
		this.couponTypeName = couponTypeName;
	}

	public String getCouponFormName() {
		return couponFormName;
	}

	public void setCouponFormName(String couponFormName) {
		this.couponFormName = couponFormName;
	}

	public int getCouponGetStatus() {
		return couponGetStatus;
	}

	public void setCouponGetStatus(int couponGetStatus) {
		this.couponGetStatus = couponGetStatus;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public int getLeftPercent() {
		return leftPercent;
	}

	public void setLeftPercent(int leftPercent) {
		this.leftPercent = leftPercent;
	}

	public int getCouponInitialCount() {
		return couponInitialCount;
	}

	public void setCouponInitialCount(int couponInitialCount) {
		this.couponInitialCount = couponInitialCount;
	}

	public int getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}

	public String getGetBeginTime() {
		return getBeginTime;
	}

	public void setGetBeginTime(String getBeginTime) {
		this.getBeginTime = getBeginTime;
	}

	
	
	public int getCouponForm() {
		return couponForm;
	}

	public void setCouponForm(int couponForm) {
		this.couponForm = couponForm;
	}

	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}
	
	

	public int getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(int creditValue) {
		this.creditValue = creditValue;
	}

	@Override
	public String toString() {
		return "ResponseGetCoupon [couponCode=" + couponCode + ", couponAmount=" + couponAmount + ", couponTypeName="
				+ couponTypeName + ", couponFormName=" + couponFormName + ", couponGetStatus=" + couponGetStatus
				+ ", beginTime=" + beginTime + ", leftPercent=" + leftPercent + ", couponInitialCount="
				+ couponInitialCount + ", couponCount=" + couponCount + ", getBeginTime=" + getBeginTime
				+ ", couponForm=" + couponForm + ", couponType=" + couponType + ", creditValue=" + creditValue + "]";
	}

	
    
}
