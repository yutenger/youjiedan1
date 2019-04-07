package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestSetPassWord {

	private String userId;

	private String payPd;

	// true 是注册用户 其他是找回用户
	private String type;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayPd() {
		return payPd;
	}

	public void setPayPd(String payPd) {
		this.payPd = payPd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "RequestSetPassWord [userId=" + userId + ", payPd=" + payPd + ", type=" + type + "]";
	}

}
