package com.stardai.manage.request;

/**
 * @Description 为新注册用户发放优惠券
 * @author Tina
 * @date 2018/4/12
 */
public class RequestCouponForNewUser {
	
	private String couponId; //优惠券id

	private String userId; //优惠券持有者id

	private double couponAmount; //优惠券面额

	private String effectiveTime;//优惠券生效时间
	
	private String expirationTime; //优惠券过期时间
	
	private String couponChannel; //优惠券发放渠道
	
	private Integer couponValidity;//优惠券有效期
	
	private Integer couponCount;//优惠券剩余数量
	
	private Integer timingType;//优惠券生效计时类型：1 发放开始计时，2 领取开始计时
	
	private Integer creditValue;//优惠券兑换需要的积分

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(double couponAmount) {
		this.couponAmount = couponAmount;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getCouponChannel() {
		return couponChannel;
	}

	public void setCouponChannel(String couponChannel) {
		this.couponChannel = couponChannel;
	}
	
	

	public Integer getCouponValidity() {
		return couponValidity;
	}

	public void setCouponValidity(Integer couponValidity) {
		this.couponValidity = couponValidity;
	}
	
	

	public Integer getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(Integer couponCount) {
		this.couponCount = couponCount;
	}

	public Integer getTimingType() {
		return timingType;
	}

	public void setTimingType(Integer timingType) {
		this.timingType = timingType;
	}

	public Integer getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(Integer creditValue) {
		this.creditValue = creditValue;
	}

	@Override
	public String toString() {
		return "RequestCouponForNewUser [couponId=" + couponId + ", userId=" + userId + ", couponAmount=" + couponAmount
				+ ", effectiveTime=" + effectiveTime + ", expirationTime=" + expirationTime + ", couponChannel="
				+ couponChannel + ", couponValidity=" + couponValidity + ", couponCount=" + couponCount
				+ ", timingType=" + timingType + ", creditValue=" + creditValue + "]";
	}

	
	
	

}
