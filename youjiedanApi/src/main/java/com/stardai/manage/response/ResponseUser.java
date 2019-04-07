package com.stardai.manage.response;

import java.util.HashMap;
import java.util.List;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class ResponseUser {

	private String userId;

	// 用户token
	private String token;
	// 头像
	private String avatar;
	// 昵称
	private String role;
	// 手机号
	private String mobileNumber;
	// 实名认证
	private Integer approvePerson;
	// 公司认证
	private Integer approveCompany;
	// 是否设置了支付密码
	private Integer hasPayPassword;
	// 余额
	private double amount;

	private String city;
	
	private String cityNameAndCode;

	// 表示该用户是否曾经通过认证,0表示未曾通过,1表示曾认证过
	private Integer hasApproved;

	//用户接受订单推送的城市
	private String pushCity;
	
	//用户接受订单推送的时间
	private String pushTime;
	//用户是否接受订单推送,0表示接受,1表示不接受
	private Integer pushAble;
	//用户设置订单推送的贷款区间
	private String pushAmount;
	//用户设置订单推送的微粒贷
	private String pushWebank;
	//用户设置订单推送的户籍选择
	private  String pushIsNative;
	//是否首次惩罚，用以判定是否强制下线：0 否，表示正常；1 是，表示需要强制下线
	private  Integer punishmentStatus;
	//惩罚措施：0 无惩罚； 1001 只能验证码登录，1002 取消公司认证， 1003 封号
	private  Integer punishCode;

	private String pwd;

	public Integer getPunishCode() {
		return punishCode;
	}

	public void setPunishCode(Integer punishCode) {
		this.punishCode = punishCode;
	}

	public Integer getPunishmentStatus() {
		return punishmentStatus;
	}

	public void setPunishmentStatus(Integer punishmentStatus) {
		this.punishmentStatus = punishmentStatus;
	}

	public String getCityNameAndCode() {
		return cityNameAndCode;
	}

	public void setCityNameAndCode(String cityNameAndCode) {
		this.cityNameAndCode = cityNameAndCode;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPushCity() {
		return pushCity;
	}
	
	public void setPushCity(String pushCity) {
		this.pushCity = pushCity;
	}

	public String getPushTime() {
		return pushTime;
	}

	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}

	public Integer getPushAble() {
		return pushAble;
	}

	public void setPushAble(Integer pushAble) {
		this.pushAble = pushAble;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getApprovePerson() {
		return approvePerson;
	}

	public void setApprovePerson(Integer approvePerson) {
		this.approvePerson = approvePerson;
	}

	public Integer getApproveCompany() {
		return approveCompany;
	}

	public void setApproveCompany(Integer approveCompany) {
		this.approveCompany = approveCompany;
	}

	public Integer getHasPayPassword() {
		return hasPayPassword;
	}

	public void setHasPayPassword(Integer hasPayPassword) {
		this.hasPayPassword = hasPayPassword;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getHasApproved() {
		return hasApproved;
	}

	public void setHasApproved(Integer hasApproved) {
		this.hasApproved = hasApproved;
	}

	public String getPushAmount() {
		return pushAmount;
	}

	public void setPushAmount(String pushAmount) {
		this.pushAmount = pushAmount;
	}

	public String getPushWebank() {
		return pushWebank;
	}

	public void setPushWebank(String pushWebank) {
		this.pushWebank = pushWebank;
	}

	public String getPushIsNative() {
		return pushIsNative;
	}

	public void setPushIsNative(String pushIsNative) {
		this.pushIsNative = pushIsNative;
	}

	@Override
	public String toString() {
		return "ResponseUser{" +
				"userId='" + userId + '\'' +
				", token='" + token + '\'' +
				", avatar='" + avatar + '\'' +
				", role='" + role + '\'' +
				", mobileNumber='" + mobileNumber + '\'' +
				", approvePerson=" + approvePerson +
				", approveCompany=" + approveCompany +
				", hasPayPassword=" + hasPayPassword +
				", amount=" + amount +
				", city='" + city + '\'' +
				", cityNameAndCode='" + cityNameAndCode + '\'' +
				", hasApproved=" + hasApproved +
				", pushCity='" + pushCity + '\'' +
				", pushTime='" + pushTime + '\'' +
				", pushAble=" + pushAble +
				", pushAmountInterval='" + pushAmount + '\'' +
				", pushWebank='" + pushWebank + '\'' +
				", pushIsNative='" + pushIsNative + '\'' +
				", punishmentStatus=" + punishmentStatus +
				", punishCode=" + punishCode +
				", pwd='" + pwd + '\'' +
				'}';
	}
}
