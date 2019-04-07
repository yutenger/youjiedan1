package com.stardai.manage.response;

/**
 * 优接单首页选出的列表
 * 
 * @author Tina 2018年7月3日
 */
public class ResponseLoanUserInitialList {

	// 姓名
	private String loanName;
	// 性别
	private String loanSex;
	// 贷款金额，单位是万
	private Integer loanMoney;
	// 期限，单位是月
	private Integer loanTerm;
	// 所在城市
	private String loanLocation;
	// 职业
	private Integer job;
	// 月收入
	private Integer monthlyIncome;
	// 收入来源
	private Integer wagePaymentForm;
	// 月经营流水
	private Integer companyMonthlyIncome;
	// 是否本地户籍
	private Integer isNative;
	// 社保
	private Integer personalShebao;
	// 房产类型
	private Integer personalHouse;
	// 车产类型
	private Integer personalCar;
	// 微粒贷
	private String webank;
	// 订单时间
	private long orderTime;
	// 订单号
	private long orderNo;
	// 单子状态，默认是0，未被抢
	private Integer ujdStatus;
	public String getLoanName() {
		return loanName;
	}
	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}
	
	public String getLoanSex() {
		return loanSex;
	}
	public void setLoanSex(String loanSex) {
		this.loanSex = loanSex;
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
	public Integer getJob() {
		return job;
	}
	public void setJob(Integer job) {
		this.job = job;
	}
	public Integer getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(Integer monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public Integer getWagePaymentForm() {
		return wagePaymentForm;
	}
	public void setWagePaymentForm(Integer wagePaymentForm) {
		this.wagePaymentForm = wagePaymentForm;
	}
	public Integer getCompanyMonthlyIncome() {
		return companyMonthlyIncome;
	}
	public void setCompanyMonthlyIncome(Integer companyMonthlyIncome) {
		this.companyMonthlyIncome = companyMonthlyIncome;
	}
	public Integer getIsNative() {
		return isNative;
	}
	public void setIsNative(Integer isNative) {
		this.isNative = isNative;
	}
	public Integer getPersonalShebao() {
		return personalShebao;
	}
	public void setPersonalShebao(Integer personalShebao) {
		this.personalShebao = personalShebao;
	}
	public Integer getPersonalHouse() {
		return personalHouse;
	}
	public void setPersonalHouse(Integer personalHouse) {
		this.personalHouse = personalHouse;
	}
	public Integer getPersonalCar() {
		return personalCar;
	}
	public void setPersonalCar(Integer personalCar) {
		this.personalCar = personalCar;
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
	public Integer getUjdStatus() {
		return ujdStatus;
	}
	public void setUjdStatus(Integer ujdStatus) {
		this.ujdStatus = ujdStatus;
	}
	public long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	@Override
	public String toString() {
		return "ResponseLoanUserInitialList [loanName=" + loanName + ", loanSex=" + loanSex + ", loanMoney=" + loanMoney
				+ ", loanTerm=" + loanTerm + ", loanLocation=" + loanLocation + ", job=" + job + ", monthlyIncome="
				+ monthlyIncome + ", wagePaymentForm=" + wagePaymentForm + ", companyMonthlyIncome="
				+ companyMonthlyIncome + ", isNative=" + isNative + ", personalShebao=" + personalShebao
				+ ", personalHouse=" + personalHouse + ", personalCar=" + personalCar + ", webank=" + webank
				+ ", orderTime=" + orderTime + ", orderNo=" + orderNo + ", ujdStatus=" + ujdStatus + "]";
	}
	
	
	

}
