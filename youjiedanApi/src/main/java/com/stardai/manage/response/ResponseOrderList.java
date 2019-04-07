package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */

public class ResponseOrderList {

	private String loanName;

	private String loanPhone;

	private Integer loanMoney;

	private Integer loanTerm;

	private String loanLocation;

	private String job;

	private long orderTime;

	private long orderNo;

	private Integer status;

	public String getLoanPhone() {
		return loanPhone;
	}

	public void setLoanPhone(String loanPhone) {
		this.loanPhone = loanPhone;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public Integer getLoanMoney() {
		return loanMoney;
	}

	public void setLoanMoney(Integer loanMoney) {
		this.loanMoney = loanMoney;
	}

	public Integer getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(Integer loanTerm) {
		this.loanTerm = loanTerm;
	}

	public String getLoanLocation() {
		return loanLocation;
	}

	public void setLoanLocation(String loanLocation) {
		this.loanLocation = loanLocation;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ResponseOrderList{" +
				"loanName='" + loanName + '\'' +
				", loanPhone='" + loanPhone + '\'' +
				", loanMoney=" + loanMoney +
				", loanTerm=" + loanTerm +
				", loanLocation='" + loanLocation + '\'' +
				", job='" + job + '\'' +
				", orderTime=" + orderTime +
				", orderNo=" + orderNo +
				", status=" + status +
				'}';
	}
}
