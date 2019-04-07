package com.stardai.manage.request;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestOrder {

	private int page = 0;

	private int pageSize = 10;

	private String job;

	private String status;

	private String city;

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

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "RequestOrder [page=" + page + ", pageSize=" + pageSize + ", job=" + job + ", status=" + status
				+ ", city=" + city + "]";
	}

}
