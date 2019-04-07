package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */

public class ResponseLoanUserList {
	
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

	

	

	private String loanSex;
	private String personalHouse;

	private String houseAddress;

	private String housePledge;

	

	private String personalCar;

	private String carAddress;

	private String carPledge;

	private String loanChit;

	

	private Integer loanScore;

	

	

	
	
	private String loanStatus;

	
	
	private String workYear;
	
	private String zmxy;
	

	
	//上班族的单位性质
	private String companyType;
	//企业主的企业注册年数
	private String registerTime;
	

	//房产车产的估价
	private String houseAssessment;
	private String carAssessment;

	
	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getCompanyMonthlyIncome() {
		return companyMonthlyIncome;
	}

	public void setCompanyMonthlyIncome(String companyMonthlyIncome) {
		this.companyMonthlyIncome = companyMonthlyIncome;
	}

	public String getHouseAssessment() {
		return houseAssessment;
	}

	public void setHouseAssessment(String houseAssessment) {
		this.houseAssessment = houseAssessment;
	}

	public String getCarAssessment() {
		return carAssessment;
	}

	public void setCarAssessment(String carAssessment) {
		this.carAssessment = carAssessment;
	}

	public String getIsNative() {
		return isNative;
	}

	public void setIsNative(String isNative) {
		this.isNative = isNative;
	}

	public String getWebank() {
		return webank;
	}

	public void setWebank(String webank) {
		this.webank = webank;
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

	public String getWorkYear() {
		return workYear;
	}

	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}

	public String getPersonalShebao() {
		return personalShebao;
	}

	public void setPersonalShebao(String personalShebao) {
		this.personalShebao = personalShebao;
	}

	public String getZmxy() {
		return zmxy;
	}

	public void setZmxy(String zmxy) {
		this.zmxy = zmxy;
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

	public String getLoanSex() {
		return loanSex;
	}

	public void setLoanSex(String loanSex) {
		this.loanSex = loanSex;
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

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getPersonalHouse() {
		return personalHouse;
	}

	public void setPersonalHouse(String personalHouse) {
		this.personalHouse = personalHouse;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public String getHousePledge() {
		return housePledge;
	}

	public void setHousePledge(String housePledge) {
		this.housePledge = housePledge;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getPersonalCar() {
		return personalCar;
	}

	public void setPersonalCar(String personalCar) {
		this.personalCar = personalCar;
	}

	public String getCarAddress() {
		return carAddress;
	}

	public void setCarAddress(String carAddress) {
		this.carAddress = carAddress;
	}

	public String getCarPledge() {
		return carPledge;
	}

	public void setCarPledge(String carPledge) {
		this.carPledge = carPledge;
	}

	public String getLoanChit() {
		return loanChit;
	}

	public void setLoanChit(String loanChit) {
		this.loanChit = loanChit;
	}

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public Integer getLoanScore() {
		return loanScore;
	}

	public void setLoanScore(Integer loanScore) {
		this.loanScore = loanScore;
	}

	public long getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getPriceStatus() {
		return priceStatus;
	}

	public void setPriceStatus(Integer priceStatus) {
		this.priceStatus = priceStatus;
	}

	/**
	 * @return the billStatus
	 */
	public Integer getBillStatus() {
		return billStatus;
	}

	/**
	 * @param billStatus the billStatus to set
	 */
	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	@Override
	public String toString() {
		return "ResponseLoanUserList [loanName=" + loanName + ", loanMoney=" + loanMoney + ", loanTerm=" + loanTerm
				+ ", loanSex=" + loanSex + ", loanLocation=" + loanLocation + ", job=" + job + ", house=" + house
				+ ", personalHouse=" + personalHouse + ", houseAddress=" + houseAddress + ", housePledge=" + housePledge
				+ ", car=" + car + ", personalCar=" + personalCar + ", carAddress=" + carAddress + ", carPledge="
				+ carPledge + ", loanChit=" + loanChit + ", loanStatus=" + loanStatus + ", loanScore=" + loanScore
				+ ", orderTime=" + orderTime + ", orderNo=" + orderNo + ", priceStatus=" + priceStatus + ", billStatus="
				+ billStatus + ", monthlyIncome=" + monthlyIncome + ", loanIncome=" + loanIncome + ", workYear="
				+ workYear + ", personalShebao=" + personalShebao + ", zmxy=" + zmxy + ", webank=" + webank
				+ ", isNative=" + isNative + ", companyType=" + companyType + ", registerTime=" + registerTime
				+ ", companyMonthlyIncome=" + companyMonthlyIncome + ", houseAssessment=" + houseAssessment
				+ ", carAssessment=" + carAssessment + "]";
	}
	
	

}
