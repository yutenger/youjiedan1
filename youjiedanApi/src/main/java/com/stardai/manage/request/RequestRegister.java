package com.stardai.manage.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestRegister {

	@NotNull(message = "手机号不能为空")
	@Size(min = 11, max = 11, message = "手机号为11位")
	private String mobileNumber;

	@NotNull(message = "密码不能为空")
	@Size(min = 6, max = 20, message = "密码为6到20位")
	private String password;

	@NotNull(message = "验证码不能为空")
	@Size(min = 4, max = 6, message = "验证码为4到6位")
	private String verifyCode;

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

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	@Override
	public String toString() {
		return "RequestUserInfoRegister [mobileNumber=" + mobileNumber + ", password=" + password + ", verifyCode="
				+ verifyCode + "]";
	}

}
