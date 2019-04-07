package com.stardai.manage.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestSmsCode {

	@NotNull(message = "手机号不能为空")
	@Size(min = 11, max = 11, message = "手机号为11位")
	private String mobileNumber;

	private String appName;

	// 1 是注册用户 2 是找回密码 3 验证码登陆 4 更换手机，使用旧手机号发送验证码时 5 更换手机，使用新手机号发送验证码时 6 使用验证码修改密码
	private Integer type;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "RequestSmsCode [mobileNumber=" + mobileNumber + ", appName=" + appName + ", type=" + type + "]";
	}

}
