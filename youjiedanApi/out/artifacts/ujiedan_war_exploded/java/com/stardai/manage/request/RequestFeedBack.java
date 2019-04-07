package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestFeedBack {

	// 手机号码
	private String phoneNumber;

	private String userId;
	// 内容
	private String content;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "RequestFeedBack [phoneNumber=" + phoneNumber + ", userId=" + userId + ", content=" + content + "]";
	}

}
