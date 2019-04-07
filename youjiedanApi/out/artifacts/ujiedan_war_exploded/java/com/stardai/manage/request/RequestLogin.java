package com.stardai.manage.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestLogin {

	@NotNull(message = "手机号不能为空")
	@Size(min = 11, max = 11, message = "手机号为11位")
	private String mobileNumber;

	@NotNull(message = "密码不能为空")
	@Size(min = 6, max = 20, message = "密码为6到20位")
	private String password;

	//验证码
	private String vertifyCode ;
	// 0 表示密码登录，1 表示验证码登陆
	private int type;

	private String token;

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

	public String getVertifyCode() {
		return vertifyCode;
	}

	public void setVertifyCode(String vertifyCode) {
		this.vertifyCode = vertifyCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
