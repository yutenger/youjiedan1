package com.stardai.manage.response;


public class ResponseShareUser {

	private Integer shareCount;
	
	private Integer shareMoney;
	
	private String shareUrl;
	
	private String twoDimensionUrl;

	public Integer getShareCount() {
		return shareCount;
	}

	public void setShareCount(Integer shareCount) {
		this.shareCount = shareCount;
	}

	public Integer getShareMoney() {
		return shareMoney;
	}

	public void setShareMoney(Integer shareMoney) {
		this.shareMoney = shareMoney;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getTwoDimensionUrl() {
		return twoDimensionUrl;
	}

	public void setTwoDimensionUrl(String twoDimensionUrl) {
		this.twoDimensionUrl = twoDimensionUrl;
	}

	@Override
	public String toString() {
		return "ResponseShareUser{" +
				"shareCount=" + shareCount +
				", shareMoney=" + shareMoney +
				", shareUrl='" + shareUrl + '\'' +
				", twoDimensionUrl='" + twoDimensionUrl + '\'' +
				'}';
	}
}
