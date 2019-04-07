package com.stardai.manage.pojo;

import javax.persistence.Table;

/**
 * @author Tina
 * @date 2019/4/19
 */
// 验证码信息表
@Table(name = "yjd_coupon_deduction")
public class CouponInfo {


	private String couponId; // 优惠券id

	private double couponAmount; // 优惠券面额

	private String couponValue; // 优惠券面额 (拼上星币的)

	private double couponDiscount; // 优惠券折扣

	private double couponFullreduction;// 优惠券满减的金额

	private String effectiveTime;// 优惠券生效时间

	private String expirationTime; // 优惠券过期时间

	private String periodTime; // 优惠券时间区间

	private int couponForm;// 优惠券消费形式：1 打折， 2 满减， 3 无门槛

	private String couponFormName; // 优惠券消费形式：转化为对应的字符串传到前台

	private int couponType;// 优惠券类型：1 充值抵扣，2 抢单抵扣

	private String couponTypeName; // 优惠券类型：转化为对应的字符串传到前台

	private int couponStatus;// 优惠券状态 1：未使用，2：已使用，3：已过期

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(double couponAmount) {
		this.couponAmount = couponAmount;
	}

	public String getCouponValue() {
		return couponValue;
	}

	public void setCouponValue(String couponValue) {
		this.couponValue = couponValue;
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

	public String getPeriodTime() {
		return periodTime;
	}

	public void setPeriodTime(String periodTime) {
		this.periodTime = periodTime;
	}

	public int getCouponForm() {
		return couponForm;
	}

	public void setCouponForm(int couponForm) {
		this.couponForm = couponForm;
	}

	public String getCouponFormName() {
		return couponFormName;
	}

	public void setCouponFormName(String couponFormName) {
		this.couponFormName = couponFormName;
	}

	public int getCouponType() {
		return couponType;
	}

	public void setCouponType(int couponType) {
		this.couponType = couponType;
	}

	public String getCouponTypeName() {
		return couponTypeName;
	}

	public void setCouponTypeName(String couponTypeName) {
		this.couponTypeName = couponTypeName;
	}

	public int getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(int couponStatus) {
		this.couponStatus = couponStatus;
	}

	@Override
	public String toString() {
		return "CouponInfo [couponId=" + couponId + ", couponAmount=" + couponAmount + ", couponValue=" + couponValue
				+ ", couponDiscount=" + couponDiscount + ", couponFullreduction=" + couponFullreduction
				+ ", effectiveTime=" + effectiveTime + ", expirationTime=" + expirationTime + ", periodTime="
				+ periodTime + ", couponForm=" + couponForm + ", couponFormName=" + couponFormName + ", couponType="
				+ couponType + ", couponTypeName=" + couponTypeName + ", couponStatus=" + couponStatus + "]";
	}

	
}
