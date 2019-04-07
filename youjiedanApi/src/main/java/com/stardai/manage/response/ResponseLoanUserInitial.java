package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */

public class ResponseLoanUserInitial {

	private String loanName;
	
	private String loanPhone;
	//手机号码归属地
	private String loanPhoneAttribution;

	private Integer loanMoney;

	private Integer loanTerm;
	
	private Integer loanAge;

	private String loanSex;
	
	private String loanOldcity;

	private String loanLocation;

	private Integer loanMarried;
	//月收入
	private Integer job;
	//工资发放形式
	private Integer wagePaymentForm;
	//月收入
	private String monthlyIncome;
	//工作年限
	private Integer workYear;
	//上班族的单位性质
	private Integer companyType;
	//企业主的企业注册年数
	private Integer registerTime;
	//企业主的月经营流水
	private String companyMonthlyIncome;

	private Integer personalShebao;

	private Integer personalGongjijin;
	//名下房产
	private Integer houseProperty;
	//房子是否抵押
	private Integer housePledge;
	//房产情况
	private Integer personalHouse;
	//房产所在城市
	private String houseAddress;
	//房产的估价
	private Integer houseAssessment;

    private Integer carProperty;

	private Integer carPledge;
	
	private Integer personalCar;

	private String carAddress;
	
	private Integer carAssessment;
	//负债
	private Integer loanDebt;
	//保单
	private Integer loanChit;
	//保单情况
	private Integer chitSituation;
	//信用卡使用状况
	private Integer creditCardStatus;
	//芝麻信用
	private String zmxy;
	//微粒贷
	private String webank;
	//补充说明
	private String tags;
	//表示是否本地户籍
	private Integer isNative;

	private Integer jiebei;

    private Integer degree;

	public Integer getHouseProperty() {
		return houseProperty;
	}

	public void setHouseProperty(Integer houseProperty) {
		this.houseProperty = houseProperty;
	}

	public Integer getCarProperty() {
		return carProperty;
	}

	public void setCarProperty(Integer carProperty) {
		this.carProperty = carProperty;
	}

	public Integer getJiebei() {
		return jiebei;
	}

	public void setJiebei(Integer jiebei) {
		this.jiebei = jiebei;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public Integer getDiscountedState() {
		return discountedState;
	}

	public void setDiscountedState(Integer discountedState) {
		this.discountedState = discountedState;
	}

	//订单时间
	private long orderTime;

	private long orderNo;
	//订单金额
	private Integer orderAmount;
	//单子状态0未被抢,1已被抢，数据库默认值2
	private Integer status;
	//打折状态 针对API
	private Integer discountedState;
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
	public String getLoanPhoneAttribution() {
		return loanPhoneAttribution;
	}
	public void setLoanPhoneAttribution(String loanPhoneAttribution) {
		this.loanPhoneAttribution = loanPhoneAttribution;
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
	public Integer getLoanAge() {
		return loanAge;
	}
	public void setLoanAge(Integer loanAge) {
		this.loanAge = loanAge;
	}
	public String getLoanSex() {
		return loanSex;
	}
	public void setLoanSex(String loanSex) {
		this.loanSex = loanSex;
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
	public Integer getLoanMarried() {
		return loanMarried;
	}
	public void setLoanMarried(Integer loanMarried) {
		this.loanMarried = loanMarried;
	}
	public Integer getJob() {
		return job;
	}
	public void setJob(Integer job) {
		this.job = job;
	}
	public Integer getWagePaymentForm() {
		return wagePaymentForm;
	}
	public void setWagePaymentForm(Integer wagePaymentForm) {
		this.wagePaymentForm = wagePaymentForm;
	}
	public String getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public Integer getWorkYear() {
		return workYear;
	}
	public void setWorkYear(Integer workYear) {
		this.workYear = workYear;
	}
	public Integer getCompanyType() {
		return companyType;
	}
	public void setCompanyType(Integer companyType) {
		this.companyType = companyType;
	}
	public Integer getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Integer registerTime) {
		this.registerTime = registerTime;
	}
	public String getCompanyMonthlyIncome() {
		return companyMonthlyIncome;
	}
	public void setCompanyMonthlyIncome(String companyMonthlyIncome) {
		this.companyMonthlyIncome = companyMonthlyIncome;
	}
	public Integer getPersonalShebao() {
		return personalShebao;
	}
	public void setPersonalShebao(Integer personalShebao) {
		this.personalShebao = personalShebao;
	}
	public Integer getPersonalGongjijin() {
		return personalGongjijin;
	}
	public void setPersonalGongjijin(Integer personalGongjijin) {
		this.personalGongjijin = personalGongjijin;
	}
	public Integer getHousePledge() {
		return housePledge;
	}
	public void setHousePledge(Integer housePledge) {
		this.housePledge = housePledge;
	}
	public Integer getPersonalHouse() {
		return personalHouse;
	}
	public void setPersonalHouse(Integer personalHouse) {
		this.personalHouse = personalHouse;
	}
	public String getHouseAddress() {
		return houseAddress;
	}
	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}
	public Integer getHouseAssessment() {
		return houseAssessment;
	}
	public void setHouseAssessment(Integer houseAssessment) {
		this.houseAssessment = houseAssessment;
	}
	public Integer getCarPledge() {
		return carPledge;
	}
	public void setCarPledge(Integer carPledge) {
		this.carPledge = carPledge;
	}
	public Integer getPersonalCar() {
		return personalCar;
	}
	public void setPersonalCar(Integer personalCar) {
		this.personalCar = personalCar;
	}
	public String getCarAddress() {
		return carAddress;
	}
	public void setCarAddress(String carAddress) {
		this.carAddress = carAddress;
	}
	public Integer getCarAssessment() {
		return carAssessment;
	}
	public void setCarAssessment(Integer carAssessment) {
		this.carAssessment = carAssessment;
	}
	public Integer getLoanDebt() {
		return loanDebt;
	}
	public void setLoanDebt(Integer loanDebt) {
		this.loanDebt = loanDebt;
	}
	public Integer getLoanChit() {
		return loanChit;
	}
	public void setLoanChit(Integer loanChit) {
		this.loanChit = loanChit;
	}
	public Integer getCreditCardStatus() {
		return creditCardStatus;
	}
	public void setCreditCardStatus(Integer creditCardStatus) {
		this.creditCardStatus = creditCardStatus;
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
	public Integer getIsNative() {
		return isNative;
	}
	public void setIsNative(Integer isNative) {
		this.isNative = isNative;
	}
	public long getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(long orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getChitSituation() {
		return chitSituation;
	}

	public void setChitSituation(Integer chitSituation) {
		this.chitSituation = chitSituation;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public String toString() {
		return "ResponseLoanUserInitial{" +
				"loanName='" + loanName + '\'' +
				", loanPhone='" + loanPhone + '\'' +
				", loanPhoneAttribution='" + loanPhoneAttribution + '\'' +
				", loanMoney=" + loanMoney +
				", loanTerm=" + loanTerm +
				", loanAge=" + loanAge +
				", loanSex='" + loanSex + '\'' +
				", loanOldcity='" + loanOldcity + '\'' +
				", loanLocation='" + loanLocation + '\'' +
				", loanMarried=" + loanMarried +
				", job=" + job +
				", wagePaymentForm=" + wagePaymentForm +
				", monthlyIncome='" + monthlyIncome + '\'' +
				", workYear=" + workYear +
				", companyType=" + companyType +
				", registerTime=" + registerTime +
				", companyMonthlyIncome='" + companyMonthlyIncome + '\'' +
				", personalShebao=" + personalShebao +
				", personalGongjijin=" + personalGongjijin +
				", housePledge=" + housePledge +
				", personalHouse=" + personalHouse +
				", houseAddress='" + houseAddress + '\'' +
				", houseAssessment=" + houseAssessment +
				", carPledge=" + carPledge +
				", personalCar=" + personalCar +
				", carAddress='" + carAddress + '\'' +
				", carAssessment=" + carAssessment +
				", loanDebt=" + loanDebt +
				", loanChit=" + loanChit +
				", chitSituation=" + chitSituation +
				", creditCardStatus=" + creditCardStatus +
				", zmxy='" + zmxy + '\'' +
				", webank='" + webank + '\'' +
				", tags='" + tags + '\'' +
				", isNative=" + isNative +
				", orderTime=" + orderTime +
				", orderNo=" + orderNo +
				", orderAmount=" + orderAmount +
				", status=" + status +
				'}';
	}
}
