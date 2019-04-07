package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestOrderListByUserId {

	private int page = 0;

	private int pageSize = 10;

	private String userId;

	// 0表示跟进中 1表示已完成 成功与失败的单子
	private Integer status;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "RequestOrderByUserId [page=" + page + ", pageSize=" + pageSize + ", userId=" + userId + ", status="
				+ status + "]";
	}

}
