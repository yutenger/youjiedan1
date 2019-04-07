package com.stardai.manage.response;

/**
 * @author jdw
 * @date 2017/10/16
 */

public class ResponseLoanUser {

	private String loanName;

	private Integer loanMoney;

	private Integer loanTerm;
	
	private Integer loanAge;
	
	private String loanPhone;

	private String loanLocation;
	//手机号码归属地
	private String loanPhoneAttribution;

	private String loanOldcity;

	private String loanMarried;

	private String job;
	private String monthlyIncome;
	private String workYear;
	//工资发放形式
	private String wagePaymentForm;
	
	//上班族的单位性质
	private String companyType;
	//企业主的企业注册年数
	private String registerTime;
	//企业主的月经营流水
	private String companyMonthlyIncome;

	private String personalShebao;

	private String personalGongjijin;

	private String house;

	private String houseAddress;
	//房产的估价
	private String houseAssessment;
	private String houseProperty;

	private String car;

	private String carAddress;

	private String carAssessment;

	private String carProperty;
	private String loanChit;

	//保单情况
	private String chitSituation;

	private String creditCardStatus;

	private Integer loanDebt;
	
	private String zmxy;
	private String webank;
	private String tags;

	private double price;

	private double orderAmount;

	private long orderTime;

	private long orderNo;

	private String jiebei;

	private String degree;

	//单子状态0未被抢,1已被抢，数据库默认值2，或者是跟单状态：0表示跟单中1表示已完成2表示跟单失败
	private Integer status;

	public String getJiebei() {
		return jiebei;
	}

	public String getHouseProperty() {
		return houseProperty;
	}

	public void setHouseProperty(String houseProperty) {
		this.houseProperty = houseProperty;
	}

	public String getCarProperty() {
		return carProperty;
	}

	public void setCarProperty(String carProperty) {
		this.carProperty = carProperty;
	}

	public void setJiebei(String jiebei) {
		this.jiebei = jiebei;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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

	public String getLoanPhone() {
		return loanPhone;
	}

	public void setLoanPhone(String loanPhone) {
		this.loanPhone = loanPhone;
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

	public String getLoanMarried() {
		return loanMarried;
	}

	public void setLoanMarried(String loanMarried) {
		this.loanMarried = loanMarried;
	}

	

	public String getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public String getChitSituation() {
		return chitSituation;
	}

	public void setChitSituation(String chitSituation) {
		this.chitSituation = chitSituation;
	}

	public String getWagePaymentForm() {
		return wagePaymentForm;
	}

	public void setWagePaymentForm(String wagePaymentForm) {
		this.wagePaymentForm = wagePaymentForm;
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

	public String getWorkYear() {
		return workYear;
	}

	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getCarAddress() {
		return carAddress;
	}

	public void setCarAddress(String carAddress) {
		this.carAddress = carAddress;
	}

	public String getLoanChit() {
		return loanChit;
	}

	public void setLoanChit(String loanChit) {
		this.loanChit = loanChit;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getCreditCardStatus() {
		return creditCardStatus;
	}

	public void setCreditCardStatus(String creditCardStatus) {
		this.creditCardStatus = creditCardStatus;
	}

	public Integer getLoanDebt() {
		return loanDebt;
	}

	public void setLoanDebt(Integer loanDebt) {
		this.loanDebt = loanDebt;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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

	public String getLoanPhoneAttribution() {
		return loanPhoneAttribution;
	}

	
	public void setLoanPhoneAttribution(String loanPhoneAttribution) {
		this.loanPhoneAttribution = loanPhoneAttribution;
	}

	@Override
	public String toString() {
		return "ResponseLoanUser{" +
				"loanName='" + loanName + '\'' +
				", loanMoney=" + loanMoney +
				", loanTerm=" + loanTerm +
				", loanAge=" + loanAge +
				", loanPhone='" + loanPhone + '\'' +
				", loanLocation='" + loanLocation + '\'' +
				", loanPhoneAttribution='" + loanPhoneAttribution + '\'' +
				", loanOldcity='" + loanOldcity + '\'' +
				", loanMarried='" + loanMarried + '\'' +
				", job='" + job + '\'' +
				", monthlyIncome='" + monthlyIncome + '\'' +
				", workYear='" + workYear + '\'' +
				", wagePaymentForm='" + wagePaymentForm + '\'' +
				", companyType='" + companyType + '\'' +
				", registerTime='" + registerTime + '\'' +
				", companyMonthlyIncome='" + companyMonthlyIncome + '\'' +
				", personalShebao='" + personalShebao + '\'' +
				", personalGongjijin='" + personalGongjijin + '\'' +
				", house='" + house + '\'' +
				", houseAddress='" + houseAddress + '\'' +
				", houseAssessment='" + houseAssessment + '\'' +
				", car='" + car + '\'' +
				", carAddress='" + carAddress + '\'' +
				", carAssessment='" + carAssessment + '\'' +
				", loanChit='" + loanChit + '\'' +
				", chitSituation='" + chitSituation + '\'' +
				", creditCardStatus='" + creditCardStatus + '\'' +
				", loanDebt=" + loanDebt +
				", zmxy='" + zmxy + '\'' +
				", webank='" + webank + '\'' +
				", tags='" + tags + '\'' +
				", price=" + price +
				", orderAmount=" + orderAmount +
				", orderTime=" + orderTime +
				", orderNo=" + orderNo +
				", status=" + status +
				'}';
	}
}
