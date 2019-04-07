package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestPayRecharge {

	private int amount;

	private String clientIp;

	private String userId;

	private String pathway;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPathway() {
		return pathway;
	}

	public void setPathway(String pathway) {
		this.pathway = pathway;
	}

	@Override
	public String toString() {
		return "ResponsePayRecharge [amount=" + amount + ", clientIp=" + clientIp + ", userId=" + userId + ", pathway="
				+ pathway + "]";
	}

}
