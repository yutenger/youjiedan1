package com.stardai.manage.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 优接单退款申请
 *
 * @author jokery
 * @create 2018-02-02 9:25
 **/

public class RequestCompanyCooperation {
	
	@NotNull(message = "姓名不能为空")
    private String name;

    @NotNull(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号为11位")
    private String mobileNumber;

    @NotNull(message = "所属公司不能为空")
    private String company;

    @NotNull(message = "目标客户区域不能为空")
    private String area;

    @NotNull(message = "单子需求量不能为空")
    private String requireNum;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getRequireNum() {
		return requireNum;
	}

	public void setRequireNum(String requireNum) {
		this.requireNum = requireNum;
	}

	@Override
	public String toString() {
		return "RequestCompanyCooperation [name=" + name + ", mobileNumber=" + mobileNumber + ", company=" + company
				+ ", area=" + area + ", requireNum=" + requireNum + "]";
	}

    
    

}
