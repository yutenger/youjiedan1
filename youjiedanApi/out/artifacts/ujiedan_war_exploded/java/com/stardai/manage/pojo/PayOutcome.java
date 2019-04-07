package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jdw
 * @date 2017/10/16
 */
// 验证码信息表
@Table(name = "yjd_pay_outcome")
public class PayOutcome {

	@Id
	private Long id;

	private String userId;

	private long orderNo;

	private String channel;

	private double amount;

	private String createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "PayOutcome [id=" + id + ", userId=" + userId + ", orderNo=" + orderNo + ", channel=" + channel
				+ ", amount=" + amount + ", createTime=" + createTime + "]";
	}
}
