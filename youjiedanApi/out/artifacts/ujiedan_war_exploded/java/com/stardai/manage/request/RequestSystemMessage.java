package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestSystemMessage {

	private int page = 0 ;

	private int pageSize = 10;
	

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

	@Override
	public String toString() {
		return "RequestSystemMessage [page=" + page + ", pageSize=" + pageSize + "]";
	}

	

}
