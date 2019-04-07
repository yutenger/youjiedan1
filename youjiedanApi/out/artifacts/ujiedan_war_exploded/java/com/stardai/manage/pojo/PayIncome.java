package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jdw
 * @date 2017/10/16
 */
// 验证码信息表
@Table(name = "yjd_pay_income")
public class PayIncome {

	@Id
	private Long id;

	private String userId;

	private String orderNo;

	private String payPathway;

	private double payMoney;

	private String payCreateTime;
	
	private Integer type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPayPathway() {
		return payPathway;
	}

	public void setPayPathway(String payPathway) {
		this.payPathway = payPathway;
	}

	public double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}

	public String getPayCreateTime() {
		return payCreateTime;
	}

	public void setPayCreateTime(String payCreateTime) {
		this.payCreateTime = payCreateTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "PayIncome [id=" + id + ", userId=" + userId + ", orderNo=" + orderNo + ", payPathway=" + payPathway
				+ ", payMoney=" + payMoney + ", payCreateTime=" + payCreateTime + ", type=" + type + "]";
	}

}
