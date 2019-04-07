package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jdw
 * @date 2017/10/16
 */
// 验证码信息表
@Table(name = "yjd_pay_wallet")
public class PayWallet {

	@Id
	private Long id;

	private String userId;

	private double mRecharge;

	private double mPresent;

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

	public double getmRecharge() {
		return mRecharge;
	}

	public void setmRecharge(double mRecharge) {
		this.mRecharge = mRecharge;
	}

	public double getmPresent() {
		return mPresent;
	}

	public void setmPresent(double mPresent) {
		this.mPresent = mPresent;
	}

	@Override
	public String toString() {
		return "PayWallet [id=" + id + ", userId=" + userId + ", mRecharge=" + mRecharge + ", mPresent=" + mPresent
				+ "]";
	}

}
