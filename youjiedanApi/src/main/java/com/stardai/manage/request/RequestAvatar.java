package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestAvatar {

	private String userId;

	private String avatar;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "RequestAvatar [userId=" + userId + ", avatar=" + avatar + "]";
	}

}
