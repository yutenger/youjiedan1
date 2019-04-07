package com.stardai.manage.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestUpdatePassword {

	private String mobileNumber;

	@NotNull(message = "旧密码不能为空")
	@Size(min = 6, max = 20, message = "密码为6到20位")
	private String oldPassword;

	@NotNull(message = "新密码不能为空")
	@Size(min = 6, max = 20, message = "密码为6到20位")
	private String newPassword;

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "RequestUpdateUserInfo [mobileNumber=" + mobileNumber + ", oldPassword=" + oldPassword + ", newPassword="
				+ newPassword + ", userId=" + userId + "]";
	}

}
