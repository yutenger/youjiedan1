package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestAllIncome {

	private int page = 0;

	private int pageSize = 10;
	
	private String userId;

	private double costMoney;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(double costMoney) {
		this.costMoney = costMoney;
	}

	@Override
	public String toString() {
		return "RequestAllIncome{" +
				"page=" + page +
				", pageSize=" + pageSize +
				", userId='" + userId + '\'' +
				", costMoney=" + costMoney +
				'}';
	}
}
