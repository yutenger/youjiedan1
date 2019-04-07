package com.stardai.manage.request;

import java.util.ArrayList;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class RequestOrderByCities {

	private int page = 0;

	private int pageSize = 10;

	private String job;

	private String status;

	private ArrayList<String> cities;

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

	public ArrayList<String> getCities() {
		return cities;
	}

	public void setCities(ArrayList<String> cities) {
		this.cities = cities;
	}

	@Override
	public String toString() {
		return "RequestOrderByCities [page=" + page + ", pageSize=" + pageSize + ", job=" + job + ", status=" + status
				+ ", cities=" + cities + "]";
	}

	
}
