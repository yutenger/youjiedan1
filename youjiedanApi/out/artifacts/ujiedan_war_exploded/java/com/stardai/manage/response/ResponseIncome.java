package com.stardai.manage.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class ResponseIncome {

	private String payPathWay;

	private double payMoney;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payCreateTime;
	
	private String  incomeNo;
	
	private Integer type;

	public String getPayPathWay() {
		return payPathWay;
	}

	public void setPayPathWay(String payPathWay) {
		this.payPathWay = payPathWay;
	}

	public double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}


	public Date getPayCreateTime() {
		return payCreateTime;
	}

	public void setPayCreateTime(Date payCreateTime) {
		this.payCreateTime = payCreateTime;
	}

	public String getIncomeNo() {
		return incomeNo;
	}

	public void setIncomeNo(String incomeNo) {
		this.incomeNo = incomeNo;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ResponseIncome [payPathWay=" + payPathWay + ", payMoney=" + payMoney + ", payCreateTime="
				+ payCreateTime + ", incomeNo=" + incomeNo + ", type=" + type + "]";
	}

	
}
