package com.stardai.manage.request;

import java.util.ArrayList;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestCompany {

	private String userId;

	private String companyCity;

	private String companyName;

	private String companyAddress;

	private String companyType; //1 表示公司列表选择的公司 2 表示自己填的公司

	private String companyBranch;
	
	private String certificationPhone ;  //实名手机

	private ArrayList<String> companyImages;

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

	public ArrayList<String> getCompanyImages() {
		return companyImages;
	}

	public void setCompanyImages(ArrayList<String> companyImages) {
		this.companyImages = companyImages;
	}
	
	

	public String getCertificationPhone() {
		return certificationPhone;
	}

	public void setCertificationPhone(String certificationPhone) {
		this.certificationPhone = certificationPhone;
	}
	
	

	@Override
	public String toString() {
		return "RequestCompany [userId=" + userId + ", companyCity=" + companyCity + ", companyName=" + companyName
				+ ", companyAddress=" + companyAddress + ", companyType=" + companyType + ", companyBranch="
				+ companyBranch + ", certificationPhone=" + certificationPhone + ", companyImages=" + companyImages
				+ "]";
	}

}
