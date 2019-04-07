package com.stardai.manage.request;

import java.util.Arrays;
import java.util.HashMap;

/**
 * 借款端接收字段表
 * @author Tina
 * 2018年7月3日
 */
public class RequestLoanUserInfo {

	// 姓名
	private String userName;
	// 手机号
	private String phone;
	// 贷款金额，单位是万
	private Integer amount;
	// 期限，单位是月
	private Integer term;
	// 所在城市
	private String location;
	// 身份证号
	private String idCard;
	// 婚姻状况
	private Integer married;
	// 职业
	private Integer job;
	// 收入来源
	private Integer income;
	// 月收入
	private Integer monthlyIncome;
	// 工作年限
	private Integer workYear;
	// 公司类型
	private Integer companyType;
	// 企业注册时间
	private Integer registerTime;
	// 月经营流水
	private Integer companyMonthlyIncome;
	// 社保
	private Integer sheBao;
	// 公积金
	private Integer gongJiJin;
	// 房产类型
	private Integer house;
	// 房产状况
	private Integer houseProperty;
	// 房产地址
	private String houseAddress;
	// 房产估值
	private Integer houseAssessment;
	// 是否接受抵押
	private Integer housePledge;
	// 车产类型
	private Integer car;
	// 车产状况
	private Integer carProperty;
	// 车产地址
	private String carAddress;
	// 车产估值
	private Integer carAssessment;
	// 是否接受抵押
	private Integer carPledge;
	// 保单
	private Integer chit;
	// 负债
	private Integer debt;
	// 信用卡使用情况
	private Integer status;
	// 微粒贷
	private String webank;
	// 芝麻信用
	private String zmxy;
	// 补充说明
	private String tags;
	// 进件渠道名称
	private String channel;
	// 渠道分支
	private String channelBranch;
	//借款人ip地址
	private Integer loanIp;
	
	// 上面的字段是借款端传过来的，下面字段是优接单这边生成的
	
	// 年龄
	private Integer age;
	// 性别
	private String sex;
	// 订单号
	private long orderNo;
	// 户籍所在城市
	private String oldCity;
	// 是否本地户籍
	private Integer isNative;
	// 单子评分
	private Integer score;
	// 单子价格
	private Integer price;
	// 订单时间
	private long orderTime;
	// 单子状态，默认是0，未被抢
	private Integer ujdStatus;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getTerm() {
		return term;
	}
	public void setTerm(Integer term) {
		this.term = term;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Integer getMarried() {
		return married;
	}
	public void setMarried(Integer married) {
		this.married = married;
	}
	public Integer getJob() {
		return job;
	}
	public void setJob(Integer job) {
		this.job = job;
	}
	public Integer getIncome() {
		return income;
	}
	public void setIncome(Integer income) {
		this.income = income;
	}
	public Integer getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(Integer monthlyIncome) {
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
	public Integer getCompanyMonthlyIncome() {
		return companyMonthlyIncome;
	}
	public void setCompanyMonthlyIncome(Integer companyMonthlyIncome) {
		this.companyMonthlyIncome = companyMonthlyIncome;
	}
	public Integer getSheBao() {
		return sheBao;
	}
	public void setSheBao(Integer sheBao) {
		this.sheBao = sheBao;
	}
	public Integer getGongJiJin() {
		return gongJiJin;
	}
	public void setGongJiJin(Integer gongJiJin) {
		this.gongJiJin = gongJiJin;
	}
	public Integer getHouse() {
		return house;
	}
	public void setHouse(Integer house) {
		this.house = house;
	}
	public Integer getHouseProperty() {
		return houseProperty;
	}
	public void setHouseProperty(Integer houseProperty) {
		this.houseProperty = houseProperty;
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
	public Integer getHousePledge() {
		return housePledge;
	}
	public void setHousePledge(Integer housePledge) {
		this.housePledge = housePledge;
	}
	public Integer getCar() {
		return car;
	}
	public void setCar(Integer car) {
		this.car = car;
	}
	public Integer getCarProperty() {
		return carProperty;
	}
	public void setCarProperty(Integer carProperty) {
		this.carProperty = carProperty;
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
	public Integer getCarPledge() {
		return carPledge;
	}
	public void setCarPledge(Integer carPledge) {
		this.carPledge = carPledge;
	}
	public Integer getChit() {
		return chit;
	}
	public void setChit(Integer chit) {
		this.chit = chit;
	}
	public Integer getDebt() {
		return debt;
	}
	public void setDebt(Integer debt) {
		this.debt = debt;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getWebank() {
		return webank;
	}
	public void setWebank(String webank) {
		this.webank = webank;
	}
	public String getZmxy() {
		return zmxy;
	}
	public void setZmxy(String zmxy) {
		this.zmxy = zmxy;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getChannelBranch() {
		return channelBranch;
	}
	public void setChannelBranch(String channelBranch) {
		this.channelBranch = channelBranch;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	public String getOldCity() {
		return oldCity;
	}
	public void setOldCity(String oldCity) {
		this.oldCity = oldCity;
	}
	public Integer getIsNative() {
		return isNative;
	}
	public void setIsNative(Integer isNative) {
		this.isNative = isNative;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
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
	
	public Integer getLoanIp() {
		return loanIp;
	}
	public void setLoanIp(Integer loanIp) {
		this.loanIp = loanIp;
	}
	@Override
	public String toString() {
		return "RequestLoanUserInfo [userName=" + userName + ", phone=" + phone + ", amount=" + amount + ", term="
				+ term + ", location=" + location + ", idCard=" + idCard + ", married=" + married + ", job=" + job
				+ ", income=" + income + ", monthlyIncome=" + monthlyIncome + ", workYear=" + workYear
				+ ", companyType=" + companyType + ", registerTime=" + registerTime + ", companyMonthlyIncome="
				+ companyMonthlyIncome + ", sheBao=" + sheBao + ", gongJiJin=" + gongJiJin + ", house=" + house
				+ ", houseProperty=" + houseProperty + ", houseAddress=" + houseAddress + ", houseAssessment="
				+ houseAssessment + ", housePledge=" + housePledge + ", car=" + car + ", carProperty=" + carProperty
				+ ", carAddress=" + carAddress + ", carAssessment=" + carAssessment + ", carPledge=" + carPledge
				+ ", chit=" + chit + ", debt=" + debt + ", status=" + status + ", webank=" + webank + ", zmxy=" + zmxy
				+ ", tags=" +tags + ", channel=" + channel + ", channelBranch=" + channelBranch
				+ ", loanIp=" + loanIp + ", age=" + age + ", sex=" + sex + ", orderNo=" + orderNo + ", oldCity="
				+ oldCity + ", isNative=" + isNative + ", score=" + score + ", price=" + price + ", orderTime="
				+ orderTime + ", ujdStatus=" + ujdStatus + "]";
	}
	
	
	

}
