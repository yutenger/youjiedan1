package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestFeedBackSuccess {

	private String userId;

	private long orderNo;

	private long orderSuccesAmount;

	private Integer orderSuccessTerm;

	private String orderSuccessTime;

	private String orderEvaluate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public long getOrderSuccesAmount() {
		return orderSuccesAmount;
	}

	public void setOrderSuccesAmount(long orderSuccesAmount) {
		this.orderSuccesAmount = orderSuccesAmount;
	}

	public Integer getOrderSuccessTerm() {
		return orderSuccessTerm;
	}

	public void setOrderSuccessTerm(Integer orderSuccessTerm) {
		this.orderSuccessTerm = orderSuccessTerm;
	}

	public String getOrderSuccessTime() {
		return orderSuccessTime;
	}

	public void setOrderSuccessTime(String orderSuccessTime) {
		this.orderSuccessTime = orderSuccessTime;
	}

	public String getOrderEvaluate() {
		return orderEvaluate;
	}

	public void setOrderEvaluate(String orderEvaluate) {
		this.orderEvaluate = orderEvaluate;
	}

	@Override
	public String toString() {
		return "RequestFeedBackSuccess [userId=" + userId + ", orderNo=" + orderNo + ", orderSuccesAmount="
				+ orderSuccesAmount + ", orderSuccessTerm=" + orderSuccessTerm + ", orderSuccessTime="
				+ orderSuccessTime + ", orderEvaluate=" + orderEvaluate + "]";
	}

}
