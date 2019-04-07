package com.stardai.manage.pojo;

import javax.persistence.Table;

/**
 * 
 * @author Administrator
 * @Description : 优接单1.4.0需求,新增用户打开APP时,返回iOS对应版本的审核状态 2018年5月9日
 */

// 返回iOS对应版本的审核状态
@Table(name = "yjd_sys_version_check")
public class SysVersionCheckForIOS {

	private String iosVersion;

	private String verifyStatus;

	@Override
	public String toString() {
		return "SysVersionCheckForIOS [iosVersion=" + iosVersion + ", verifyStatus=" + verifyStatus + "]";
	}

	public String getIosVersion() {
		return iosVersion;
	}

	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}

	public String getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(String verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

}
