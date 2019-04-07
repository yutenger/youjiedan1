package com.stardai.manage.request;

/**
 * 
 * @author Tina
 * 2018年6月7日
 */
public class RequestAddCreditByNewEdition {

	private String userId;

	private String versionCode;
	
	private String iosVersion;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getIosVersion() {
		return iosVersion;
	}

	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}

	@Override
	public String toString() {
		return "RequestAddCreditByNewEdition [userId=" + userId + ", versionCode=" + versionCode + ", iosVersion="
				+ iosVersion + "]";
	}

	
}
