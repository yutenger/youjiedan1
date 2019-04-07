package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestOrderByUserId {

	private String userId;

	private long orderNo;
	
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

	@Override
	public String toString() {
		return "RequestOrderByUserId [userId=" + userId + ", orderNo=" + orderNo + "]";
	}


}
