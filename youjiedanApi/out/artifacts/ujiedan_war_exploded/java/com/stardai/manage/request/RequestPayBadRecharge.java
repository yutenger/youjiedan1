package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestPayBadRecharge {

	private int amount;

	private long orderNo;

	private String userId;

	private String pathway;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
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
		return "RequestPayBadRecharge [amount=" + amount + ", orderNo=" + orderNo + ", userId=" + userId + ", pathway="
				+ pathway + "]";
	}

}
