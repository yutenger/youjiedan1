package com.stardai.manage.request;

import javax.validation.constraints.NotNull;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestPerson {

	@NotNull(message = "用户ID不能为空")
	private String userId;

	@NotNull(message = "用户姓名不能为空")
	private String name;

	@NotNull(message = "身份证号不能为空")
	private String idCard;

	private String mobileNumber;

	private String orderNo;

	//标识是从优接单过去的
	private Integer fromUJD = 1;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getFromUJD() {
		return fromUJD;
	}

	public void setFromUJD(Integer fromUJD) {
		this.fromUJD = fromUJD;
	}

	@Override
	public String toString() {
		return "RequestPerson{" +
				"userId='" + userId + '\'' +
				", name='" + name + '\'' +
				", idCard='" + idCard + '\'' +
				", mobileNumber='" + mobileNumber + '\'' +
				", orderNo='" + orderNo + '\'' +
				", fromUJD=" + fromUJD +
				'}';
	}
}
