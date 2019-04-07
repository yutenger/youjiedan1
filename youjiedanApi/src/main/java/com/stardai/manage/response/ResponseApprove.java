package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */
public class ResponseApprove {

	private Integer approvePerson;

	private Integer approveCompany;

	private Integer hasApproved;

	public Integer getApprovePerson() {
		return approvePerson;
	}

	public void setApprovePerson(Integer approvePerson) {
		this.approvePerson = approvePerson;
	}

	public Integer getApproveCompany() {
		return approveCompany;
	}

	public void setApproveCompany(Integer approveCompany) {
		this.approveCompany = approveCompany;
	}

	public Integer getHasApproved() {
		return hasApproved;
	}

	public void setHasApproved(Integer hasApproved) {
		this.hasApproved = hasApproved;
	}

	public ResponseApprove() {
		super();
	}

	public ResponseApprove(Integer approvePerson, Integer approveCompany, Integer hasApproved) {
		super();
		this.approvePerson = approvePerson;
		this.approveCompany = approveCompany;
		this.hasApproved = hasApproved;
	}

	@Override
	public String toString() {
		return "ResponseApprove [approvePerson=" + approvePerson + ", approveCompany=" + approveCompany
				+ ", hasApproved=" + hasApproved + "]";
	}

}
