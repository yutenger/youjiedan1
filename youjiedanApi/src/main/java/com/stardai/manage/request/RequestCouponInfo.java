package com.stardai.manage.request;

/**
 * @Description 优惠券表的属性
 * @author Tina
 * @date 2018/4/13
 */
public class RequestCouponInfo {
	
	private String couponId; //优惠券id

	private String userId; //优惠券持有者id

	private double couponAmount; //优惠券面额
	
	private double couponDiscount; //优惠券折扣
	
	private double couponFullreduction;//优惠券满减的金额

	private String effectiveTime;//优惠券生效时间
	
	private String expirationTime; //优惠券过期时间
	
	private String couponChannel; //优惠券发放渠道
	
	private int couponForm;//优惠券消费形式：1 打折， 2 满减， 3 无门槛
	
	private int couponType;//优惠券类型：1 充值抵扣，2 抢单抵扣
	
	private String updateTime; //优惠券过期时间
	
	private String createUser; //优惠券创建者
	
	private String couponReason; //'优惠券发放原因(仅用于人工发放的优惠券)

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

	public double getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(double couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public double getCouponFullreduction() {
		return couponFullreduction;
	}

	public void setCouponFullreduction(double couponFullreduction) {
		this.couponFullreduction = couponFullreduction;
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

	
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	
	
	public String getCouponReason() {
		return couponReason;
	}

	public void setCouponReason(String couponReason) {
		this.couponReason = couponReason;
	}

	@Override
	public String toString() {
		return "RequestCouponInfo [couponId=" + couponId + ", userId=" + userId + ", couponAmount=" + couponAmount
				+ ", couponDiscount=" + couponDiscount + ", couponFullreduction=" + couponFullreduction
				+ ", effectiveTime=" + effectiveTime + ", expirationTime=" + expirationTime + ", couponChannel="
				+ couponChannel + ", couponForm=" + couponForm + ", couponType=" + couponType + ", updateTime="
				+ updateTime + ", createUser=" + createUser + ", couponReason=" + couponReason + "]";
	}
	
	

	
	

}
