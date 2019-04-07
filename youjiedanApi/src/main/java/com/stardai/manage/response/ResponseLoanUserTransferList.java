package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */

public class ResponseLoanUserTransferList {
	
	private long orderNo;

	private String loanName;

	private Integer loanMoney;

	private Integer loanTerm;

	private String loanLocation;

	private String job;

	// 企业主的月经营流水
	private String companyMonthlyIncome;
	//月收入
	private String monthlyIncome;
	//工资发放形式
	private String loanIncome;
	// 表示是否本地户籍
	private String isNative;

	private String personalShebao;
	private String personalGongjijin;
	private String house;
	
	private String car;
	//微粒贷
	private String webank;
	//创建时间
	private long orderTime;
	// 给前端判断要显示订单右上角哪种标签
	// 1表示最新,2表示5元,3表示1元,4表示不显示标签
	private Integer priceStatus;
	// 前端每个单子右下角展示的订单状态（1 已被抢，0 未被抢）
	private Integer billStatus;

	public Integer getDiscountedState() {
		return discountedState;
	}

	public void setDiscountedState(Integer discountedState) {
		this.discountedState = discountedState;
	}

	//打折状态 针对API设置
	private Integer discountedState;
	public long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	public String getLoanName() {
		return loanName;
	}
	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}
	public Integer getLoanMoney() {
		return loanMoney;
	}
	public void setLoanMoney(Integer loanMoney) {
		this.loanMoney = loanMoney;
	}
	public Integer getLoanTerm() {
		return loanTerm;
	}
	public void setLoanTerm(Integer loanTerm) {
		this.loanTerm = loanTerm;
	}
	public String getLoanLocation() {
		return loanLocation;
	}
	public void setLoanLocation(String loanLocation) {
		this.loanLocation = loanLocation;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getCompanyMonthlyIncome() {
		return companyMonthlyIncome;
	}
	public void setCompanyMonthlyIncome(String companyMonthlyIncome) {
		this.companyMonthlyIncome = companyMonthlyIncome;
	}
	public String getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public String getLoanIncome() {
		return loanIncome;
	}
	public void setLoanIncome(String loanIncome) {
		this.loanIncome = loanIncome;
	}
	public String getIsNative() {
		return isNative;
	}
	public void setIsNative(String isNative) {
		this.isNative = isNative;
	}
	public String getPersonalShebao() {
		return personalShebao;
	}
	public void setPersonalShebao(String personalShebao) {
		this.personalShebao = personalShebao;
	}
	public String getHouse() {
		return house;
	}
	public void setHouse(String house) {
		this.house = house;
	}
	public String getCar() {
		return car;
	}
	public void setCar(String car) {
		this.car = car;
	}
	public String getWebank() {
		return webank;
	}
	public void setWebank(String webank) {
		this.webank = webank;
	}
	public long getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getPriceStatus() {
		return priceStatus;
	}
	public void setPriceStatus(Integer priceStatus) {
		this.priceStatus = priceStatus;
	}
	public Integer getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	public String getPersonalGongjijin() {
		return personalGongjijin;
	}

	public void setPersonalGongjijin(String personalGongjijin) {
		this.personalGongjijin = personalGongjijin;
	}

	@Override
	public String toString() {
		return "ResponseLoanUserTransferList{" +
				"orderNo=" + orderNo +
				", loanName='" + loanName + '\'' +
				", loanMoney=" + loanMoney +
				", loanTerm=" + loanTerm +
				", loanLocation='" + loanLocation + '\'' +
				", job='" + job + '\'' +
				", companyMonthlyIncome='" + companyMonthlyIncome + '\'' +
				", monthlyIncome='" + monthlyIncome + '\'' +
				", loanIncome='" + loanIncome + '\'' +
				", isNative='" + isNative + '\'' +
				", personalShebao='" + personalShebao + '\'' +
				", personalGongjijin=" + personalGongjijin +
				", house='" + house + '\'' +
				", car='" + car + '\'' +
				", webank='" + webank + '\'' +
				", orderTime=" + orderTime +
				", priceStatus=" + priceStatus +
				", billStatus=" + billStatus +
				", discountedState=" + discountedState +
				'}';
	}
}
