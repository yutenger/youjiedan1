package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */

public class ResponseOrder {

	private String loanName;

	private String loanLocation;

	private String job;

	private String loanPhone;

	private Integer loanMoney;

	private Integer loanTerm;

	private String loanCard;

	private Integer loanAge;

	private String loanOldcity;

	private String loanMarried;

	private String loanIncome;

	private String monthlyIncome;

	private String workYear;

	private String personalShebao;

	private String personalGongjijin;

	private String house;

	private String personalHouse;

	private String houseAddress;

	private String housePledge;

	private String car;

	private String personalCar;

	private String carAddress;

	private String carPledge;

	private String loanDebt;

	private String loanChit;

	private String loanStatus;

	private Integer status;

	private long orderTime;

	private long orderNo;

	private String zmxy;
	private String webank;
	private String tags;

	//表示是否本地户籍
	private String isNative;

	//上班族的单位性质
	private String companyType;
	//企业主的企业注册年数
	private String registerTime;
	//企业主的月经营流水
	private String companyMonthlyIncome;

	//房产车产的估价
	private String houseAssessment;
	private String carAssessment;
	
	//手机号归属地
	private String loanPhoneAttribution;

	@Override
	public String toString() {
		return "ResponseOrder [loanName=" + loanName + ", loanLocation=" + loanLocation + ", job=" + job
				+ ", loanPhone=" + loanPhone + ", loanMoney=" + loanMoney + ", loanTerm=" + loanTerm + ", loanCard="
				+ loanCard + ", loanAge=" + loanAge + ", loanOldcity=" + loanOldcity + ", loanMarried=" + loanMarried
				+ ", loanIncome=" + loanIncome + ", monthlyIncome=" + monthlyIncome + ", workYear=" + workYear
				+ ", personalShebao=" + personalShebao + ", personalGongjijin=" + personalGongjijin + ", house=" + house
				+ ", personalHouse=" + personalHouse + ", houseAddress=" + houseAddress + ", housePledge=" + housePledge
				+ ", car=" + car + ", personalCar=" + personalCar + ", carAddress=" + carAddress + ", carPledge="
				+ carPledge + ", loanDebt=" + loanDebt + ", loanChit=" + loanChit + ", loanStatus=" + loanStatus
				+ ", status=" + status + ", orderTime=" + orderTime + ", orderNo="
				+ orderNo + ", zmxy=" + zmxy + ", webank=" + webank + ", tags=" + tags + ", isNative=" + isNative
				+ ", companyType=" + companyType + ", registerTime=" + registerTime + ", companyMonthlyIncome="
				+ companyMonthlyIncome + ", houseAssessment=" + houseAssessment + ", carAssessment=" + carAssessment
				+ ", loanPhoneAttribution=" + loanPhoneAttribution + "]";
	}

	public String getIsNative() {
		return isNative;
	}

	public void setIsNative(String isNative) {
		this.isNative = isNative;
	}

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

	public String getZmxy() {
		return zmxy;
	}

	public void setZmxy(String zmxy) {
		this.zmxy = zmxy;
	}

	public String getWebank() {
		return webank;
	}

	public void setWebank(String webank) {
		this.webank = webank;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public String getLoanPhone() {
		return loanPhone;
	}

	public void setLoanPhone(String loanPhone) {
		this.loanPhone = loanPhone;
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

	public String getLoanCard() {
		return loanCard;
	}

	public void setLoanCard(String loanCard) {
		this.loanCard = loanCard;
	}

	public Integer getLoanAge() {
		return loanAge;
	}

	public void setLoanAge(Integer loanAge) {
		this.loanAge = loanAge;
	}

	public String getLoanOldcity() {
		return loanOldcity;
	}

	public void setLoanOldcity(String loanOldcity) {
		this.loanOldcity = loanOldcity;
	}

	public String getLoanLocation() {
		return loanLocation;
	}

	public void setLoanLocation(String loanLocation) {
		this.loanLocation = loanLocation;
	}

	public String getLoanMarried() {
		return loanMarried;
	}

	public void setLoanMarried(String loanMarried) {
		this.loanMarried = loanMarried;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getLoanIncome() {
		return loanIncome;
	}

	public void setLoanIncome(String loanIncome) {
		this.loanIncome = loanIncome;
	}

	public String getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
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

	public String getPersonalGongjijin() {
		return personalGongjijin;
	}

	public void setPersonalGongjijin(String personalGongjijin) {
		this.personalGongjijin = personalGongjijin;
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

	public String getLoanDebt() {
		return loanDebt;
	}

	public void setLoanDebt(String loanDebt) {
		this.loanDebt = loanDebt;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	/**
	 * @return the loanPhoneAttribution
	 */
	public String getLoanPhoneAttribution() {
		return loanPhoneAttribution;
	}

	/**
	 * @param loanPhoneAttribution the loanPhoneAttribution to set
	 */
	public void setLoanPhoneAttribution(String loanPhoneAttribution) {
		this.loanPhoneAttribution = loanPhoneAttribution;
	}

}