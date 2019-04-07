package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jdw
 * @date 2017/10/16
 */
// 公司认证
@Table(name = "yjd_user_company")
public class UserCompany {

	@Id
	private Long id;

	private String userId;

	private String companyCity;

	private String companyName;

	private String companyAddress;

	private String companyType;

	private String companyBranch;

	private String companyImages;
	// 创建时间
	private long createTime;

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

	public String getCompanyCity() {
		return companyCity;
	}

	public void setCompanyCity(String companyCity) {
		this.companyCity = companyCity;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyBranch() {
		return companyBranch;
	}

	public void setCompanyBranch(String companyBranch) {
		this.companyBranch = companyBranch;
	}

	public String getCompanyImages() {
		return companyImages;
	}

	public void setCompanyImages(String companyImages) {
		this.companyImages = companyImages;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", userId=" + userId + ", companyCity=" + companyCity + ", companyName="
				+ companyName + ", companyAddress=" + companyAddress + ", companyType=" + companyType
				+ ", companyBranch=" + companyBranch + ", companyImages=" + companyImages + ", createTime=" + createTime
				+ "]";
	}

}
