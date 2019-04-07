package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class ResponseUserInfo {

	// 昵称
	private String role;

	// 实名认证(1表示认证通过,2表示姓名与身份证号不匹配,3表示身份证号已经被用于认证了)
	private Integer approvePerson;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getApprovePerson() {
		return approvePerson;
	}

	public void setApprovePerson(Integer approvePerson) {
		this.approvePerson = approvePerson;
	}

	@Override
	public String toString() {
		return "ResponseUserInfo [role=" + role + ", approvePerson=" + approvePerson + "]";
	}

}
