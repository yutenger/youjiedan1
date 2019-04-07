package com.stardai.manage.response;

public class ResponseRefund {
	private long orderNo;
	private String loanName;
	private Integer isDone;
	private String createTime;
	@Override
	public String toString() {
		return "ResponseRefund [orderNo=" + orderNo + ", loanName=" + loanName + ", isDone=" + isDone + ", createTime="
				+ createTime + "]";
	}
	public long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	public String getLoanName() {
		return loanName;
	}
	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}
	public Integer getIsDone() {
		return isDone;
	}
	public void setIsDone(Integer isDone) {
		this.isDone = isDone;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
