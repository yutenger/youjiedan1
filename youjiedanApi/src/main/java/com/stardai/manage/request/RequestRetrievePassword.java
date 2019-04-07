package com.stardai.manage.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestRetrievePassword {

	private String mobileNumber;

	@NotNull(message = "密码不能为空")
	@Size(min = 6, max = 12, message = "密码为6到12位")
	private String password;
    
	private String verifyCode;
	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "RequestRetrievePassword [mobileNumber=" + mobileNumber + ", password=" + password + ", verifyCode="
				+ verifyCode + "]";
	}

}
