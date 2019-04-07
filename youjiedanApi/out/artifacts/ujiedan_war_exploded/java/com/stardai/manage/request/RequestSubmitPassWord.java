package com.stardai.manage.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestSubmitPassWord {

	@NotNull(message = "手机号不能为空")
	@Size(min = 11, max = 11, message = "手机号为11位")
	private String mobileNumber;

	private String verifyCode;

	private String userId;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "RequestSubmitPassWord [mobileNumber=" + mobileNumber + ", verifyCode=" + verifyCode + ", userId="
				+ userId + "]";
	}

}
