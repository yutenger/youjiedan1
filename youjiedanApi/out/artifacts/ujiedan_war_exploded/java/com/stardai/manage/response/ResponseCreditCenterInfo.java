package com.stardai.manage.response;

/**
 * 
 * @author Tina
 * 2018年5月31日
 */
public class ResponseCreditCenterInfo {

	//全部积分
	private int totalCredit;
	
	//当天获取的全部积分
	private int  todayTotalCredit;
	
	//签到状态   0：当天已签到，1：当天未签到
	private Integer signinStatus;
	
	//当天签到获取的积分
	private int  signinCredit;

	public int getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(int totalCredit) {
		this.totalCredit = totalCredit;
	}

	public int getTodayTotalCredit() {
		return todayTotalCredit;
	}

	public void setTodayTotalCredit(int todayTotalCredit) {
		this.todayTotalCredit = todayTotalCredit;
	}

	public Integer getSigninStatus() {
		return signinStatus;
	}

	public void setSigninStatus(Integer signinStatus) {
		this.signinStatus = signinStatus;
	}

	public int getSigninCredit() {
		return signinCredit;
	}

	public void setSigninCredit(int signinCredit) {
		this.signinCredit = signinCredit;
	}

	@Override
	public String toString() {
		return "ResponseCreditCenterInfo [totalCredit=" + totalCredit + ", todayTotalCredit=" + todayTotalCredit
				+ ", signinStatus=" + signinStatus + ", signinCredit=" + signinCredit + "]";
	}
	
	
	
}
