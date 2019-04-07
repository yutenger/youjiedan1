package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestFeedBackBad {

	private String userId;

	private long orderNo;

	private String orderBadCause;

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

	public String getOrderBadCause() {
		return orderBadCause;
	}

	public void setOrderBadCause(String orderBadCause) {
		this.orderBadCause = orderBadCause;
	}

	public String getOrderEvaluate() {
		return orderEvaluate;
	}

	public void setOrderEvaluate(String orderEvaluate) {
		this.orderEvaluate = orderEvaluate;
	}

	@Override
	public String toString() {
		return "RequestFeedBackBad [userId=" + userId + ", orderNo=" + orderNo + ", orderBadCause=" + orderBadCause
				+ ", orderEvaluate=" + orderEvaluate + "]";
	}

}
