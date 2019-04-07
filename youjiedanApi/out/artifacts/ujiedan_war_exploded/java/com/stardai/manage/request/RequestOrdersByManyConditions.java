package com.stardai.manage.request;

import java.util.ArrayList;

/**
 * @Description:1.5.0版本更换筛选条件，订单筛选变多选
 * @author Tina 2018年5月19日
 */

public class RequestOrdersByManyConditions {

	private int page = 0;

	private int pageSize = 10;
	
	// 订单状态（1 全部订单，2 可抢订单）
	private Integer billStatus; 
	
	//订单类型选择  1:打卡贷,2:社保贷,3:企业贷,4:证件贷,5:优房贷,6:优车贷,7:微粒贷,8:保单贷
	private ArrayList<Integer> conditions; 
	
	// 折扣类型选择，没有选就默认是0（1: 最新发布，2: 6折促销，3: 5元抢单）
	private int status; 

	// 城市列表选择，如果传的是全国，cities= [1]
	private ArrayList<String> cities; 

	//记录是否选择订单类型，1:传了订单类型   2:没有传订单类型
	private Integer isChooseType; 

	private Integer daKaDai;
	private Integer sheBaoDai;
	private Integer qiYeDai;
	private Integer zhengJianDai;
	private Integer youFangDai;
	private Integer youCheDai;
	private Integer weiLiDai;
	private Integer baoDanDai;

	private Long time;
	private Long start;
	private Long end;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ArrayList<String> getCities() {
		return cities;
	}

	public void setCities(ArrayList<String> cities) {
		this.cities = cities;
	}

	public Integer getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	public ArrayList<Integer> getConditions() {
		return conditions;
	}

	public void setConditions(ArrayList<Integer> conditions) {
		this.conditions = conditions;
	}

	public Integer getDaKaDai() {
		return daKaDai;
	}

	public void setDaKaDai(Integer daKaDai) {
		this.daKaDai = daKaDai;
	}

	public Integer getSheBaoDai() {
		return sheBaoDai;
	}

	public void setSheBaoDai(Integer sheBaoDai) {
		this.sheBaoDai = sheBaoDai;
	}

	public Integer getQiYeDai() {
		return qiYeDai;
	}

	public void setQiYeDai(Integer qiYeDai) {
		this.qiYeDai = qiYeDai;
	}

	public Integer getZhengJianDai() {
		return zhengJianDai;
	}

	public void setZhengJianDai(Integer zhengJianDai) {
		this.zhengJianDai = zhengJianDai;
	}

	public Integer getYouFangDai() {
		return youFangDai;
	}

	public void setYouFangDai(Integer youFangDai) {
		this.youFangDai = youFangDai;
	}

	public Integer getYouCheDai() {
		return youCheDai;
	}

	public void setYouCheDai(Integer youCheDai) {
		this.youCheDai = youCheDai;
	}

	public Integer getWeiLiDai() {
		return weiLiDai;
	}

	public void setWeiLiDai(Integer weiLiDai) {
		this.weiLiDai = weiLiDai;
	}

	public Integer getBaoDanDai() {
		return baoDanDai;
	}

	public void setBaoDanDai(Integer baoDanDai) {
		this.baoDanDai = baoDanDai;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	
	public Integer getIsChooseType() {
		return isChooseType;
	}

	
	public void setIsChooseType(Integer isChooseType) {
		this.isChooseType = isChooseType;
	}

	@Override
	public String toString() {
		return "RequestOrdersByManyConditions [page=" + page + ", pageSize=" + pageSize + ", status=" + status
				+ ", cities=" + cities + ", billStatus=" + billStatus + ", conditions=" + conditions + ", isChooseType="
				+ isChooseType + ", daKaDai=" + daKaDai + ", sheBaoDai=" + sheBaoDai + ", qiYeDai=" + qiYeDai
				+ ", zhengJianDai=" + zhengJianDai + ", youFangDai=" + youFangDai + ", youCheDai=" + youCheDai
				+ ", weiLiDai=" + weiLiDai + ", baoDanDai=" + baoDanDai + ", time=" + time + ", start=" + start
				+ ", end=" + end + "]";
	}
	
	

}
