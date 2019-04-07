package com.stardai.manage.response;


public class ResponseLoanOrder {

	//抢单时间
	private long orderTime;
	
	//提交时间
	private String  submitTime;
	
	private int status;
	
	private String role ;
	
	private String mobileNumber;

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Override
	public String toString() {
		return "ResponseLoanOrder [orderTime=" + orderTime + ", submitTime=" + submitTime + ", status=" + status
				+ ", role=" + role + ", mobileNumber=" + mobileNumber + "]";
	}

	
	
}
