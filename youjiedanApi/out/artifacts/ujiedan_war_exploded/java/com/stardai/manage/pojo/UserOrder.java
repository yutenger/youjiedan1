package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jdw
 * @date 2017/10/16
 */
// 反馈
@Table(name = "yjd_user_order")
public class UserOrder {

	@Id
	private Long id;

	private String userId;

	private long orderNo;

	private Integer orderPrice;

	private long orderTime;

	private Integer status;

	private long orderSuccessAmount;

	private Integer orderSuccessTerm;

	private String orderSuccessTime;

	private String orderBadCause;

	private String orderEvaluate;

	//提交时间
	private String submitTime;
	
	// 创建时间
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

	public Integer getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public long getOrderSuccessAmount() {
		return orderSuccessAmount;
	}

	public void setOrderSuccessAmount(long orderSuccessAmount) {
		this.orderSuccessAmount = orderSuccessAmount;
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

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	@Override
	public String toString() {
		return "UserOrder [id=" + id + ", userId=" + userId + ", orderNo=" + orderNo + ", orderPrice=" + orderPrice
				+ ", orderTime=" + orderTime + ", status=" + status + ", orderSuccessAmount=" + orderSuccessAmount
				+ ", orderSuccessTerm=" + orderSuccessTerm + ", orderSuccessTime=" + orderSuccessTime
				+ ", orderBadCause=" + orderBadCause + ", orderEvaluate=" + orderEvaluate + ", submitTime=" + submitTime
				+ ", createTime=" + createTime + "]";
	}

}
