package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jdw
 * @date 2017/10/16
 */
// 验证码信息表
@Table(name = "yjd_order_time")
public class OrderTime {

	@Id
	private Long id;

	// 最新 5元 1元
	private String status;

	// 时间
	private Integer orderTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Integer orderTime) {
		this.orderTime = orderTime;
	}

	@Override
	public String toString() {
		return "OrderTime [id=" + id + ", status=" + status + ", orderTime=" + orderTime + "]";
	}

}
