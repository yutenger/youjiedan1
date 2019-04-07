package com.stardai.manage.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestLoanRegister {

	@NotNull(message = "手机号不能为空")
	@Size(min = 11, max = 11, message = "手机号为11位")
	private String mobileNumber;

	@NotNull(message = "验证码不能为空")
	@Size(min = 4, max = 6, message = "验证码为4到6位")
	private String verifyCode;

	private String channel;
	
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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		return "RequestLoanRegister [mobileNumber=" + mobileNumber + ", verifyCode=" + verifyCode + ", channel="
				+ channel + "]";
	}
	

}
