package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jdw
 * @date 2017/10/16
 */
// 用户信息表
@Table(name = "yjd_user_info")
public class User {

	// 对应数据库中的主键
	@Id
	private Long id;

	private String userId;

	@NotNull(message = "姓名不能为空")
	@Size(min = 1, max = 20, message = "姓名最多20个字")
	private String userName;

	// 密码
	private String password;

	// 用户token
	@Transient
	private String token;

	// 用户手机号
	private String mobileNumber;

	// 性别
	private String sex;

	// 地址
	private String address;

	// 用户身份证号
	private String idCard;

	// 创建时间
	private Long createTime;

	// 头像
	private String avatar;

	// 用户昵称
	private String role;
	// 实名认证
	private Integer approvePerson;
	// 公司认证
	private Integer approveCompany;

	// 关注的城市
	private String cities;

	// 注册用户的来源渠道
	private String channel;

	// 邀请这个人注册的邀请者的userId
	private String sharedId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
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

	public String getCities() {
		return cities;
	}

	public void setCities(String cities) {
		this.cities = cities;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSharedId() {
		return sharedId;
	}

	public void setSharedId(String sharedId) {
		this.sharedId = sharedId;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", userName=" + userName + ", password=" + password
				+ ", token=" + token + ", mobileNumber=" + mobileNumber + ", sex=" + sex + ", address=" + address
				+ ", idCard=" + idCard + ", createTime=" + createTime + ", avatar=" + avatar + ", role=" + role
				+ ", approvePerson=" + approvePerson + ", approveCompany=" + approveCompany + ", cities=" + cities
				+ ", channel=" + channel + ", sharedId=" + sharedId + "]";
	}

}
