package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jdw
 * @date 2017/10/16
 */
// 验证码信息表
@Table(name = "yjd_pay_money")
public class PayMoney {

	@Id
	private Long id;

	private Integer startMoney;

	private Integer addMoney;

	private Integer endMoney;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStartMoney() {
		return startMoney;
	}

	public void setStartMoney(Integer startMoney) {
		this.startMoney = startMoney;
	}

	public Integer getAddMoney() {
		return addMoney;
	}

	public void setAddMoney(Integer addMoney) {
		this.addMoney = addMoney;
	}

	public Integer getEndMoney() {
		return endMoney;
	}

	public void setEndMoney(Integer endMoney) {
		this.endMoney = endMoney;
	}

	@Override
	public String toString() {
		return "PayMoney [id=" + id + ", startMoney=" + startMoney + ", addMoney=" + addMoney + ", endMoney=" + endMoney
				+ "]";
	}

}
