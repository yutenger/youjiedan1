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

	private String couponId;

	//2.1 之前的版本，充值送星币，2.1之后充值可以使用满减券，就不送星币
	//0表示2.1之前版本，1表示2.1之后版本
	private int type;

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

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "RequestPayRecharge{" +
				"amount=" + amount +
				", clientIp='" + clientIp + '\'' +
				", userId='" + userId + '\'' +
				", pathway='" + pathway + '\'' +
				", couponId='" + couponId + '\'' +
				", type=" + type +
				'}';
	}
}
