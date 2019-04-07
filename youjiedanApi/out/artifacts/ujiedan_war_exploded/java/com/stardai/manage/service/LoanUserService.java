package com.stardai.manage.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.stardai.manage.pojo.CreditScore;
import com.stardai.manage.utils.IdWorker;
import com.stardai.manage.utils.IsNativeUtils;
import com.stardai.manage.utils.JiguangPush;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import com.google.common.collect.Maps;
import com.stardai.manage.mapper.HotCityMapper;
import com.stardai.manage.mapper.LoanUserMapper;
import com.stardai.manage.request.RequestCreditScore;
import com.stardai.manage.request.RequestLoanUserInfo;
import com.stardai.manage.response.ResponseLoanOrder;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Service
@SuppressWarnings("all")
public class LoanUserService {

	@Autowired
	private LoanUserMapper loanUserMapper;

	@Autowired
	private HotCityMapper hotCityMapper;

	private IdWorker idWorker = new IdWorker(0, 8);

	// 将小星借款app传过来的借款人信息保存到优接单的数据库(小星借款版本1.2.6以后启用)
	public long insertLoanInfo(RequestLoanUserInfo loanUserInfo) throws Exception {

		// 判断借款人是不是本地人
		String idCard = loanUserInfo.getIdCard();
		String location = loanUserInfo.getLocation();
		Integer isNative;
		//根据身份证号查询用户所在户籍地
		String oldCity = this.queryOldCity(idCard);
		if (location.equals(oldCity)) {
			// 本地户籍
			isNative = 2301;
		} else {
			// 非本地户籍
			isNative = 2302;
		}
		loanUserInfo.setIsNative(isNative);
		loanUserInfo.setOldCity(oldCity);
		// 把传过来的字段编码转成唯一的编码，如job=1，为上班族，转为job=2001，这样编码在字典表里就是唯一的
		loanUserInfo = this.changeField(loanUserInfo);
		// 对单子进行评分
		loanUserInfo = this.queryLoanScore(loanUserInfo);

		long orderNo = idWorker.nextId();
		long orderTime = System.currentTimeMillis();
		loanUserInfo.setOrderNo(orderNo);
		loanUserInfo.setOrderTime(orderTime);
		// 单子的状态，ujdStatus默认设为0，未被抢
		loanUserInfo.setUjdStatus(0);
		// 判断此人之前有没有申请过，如果一定期间内申请过就不显示在优接单
		loanUserInfo = this.isHidenOnApp(loanUserInfo);

		loanUserMapper.insertLoanInfo(loanUserInfo);

		if (loanUserInfo.getUjdStatus() == 0) {
			// 先生成推送要显示的消息
			String message = this.createMessage(loanUserInfo, location);
			// 给关注这个城市的信贷经理发推送
			//JiguangPush.jPushOrders(location, message, orderNo);
		}

		return orderNo;
	}

	private String queryOldCity(String idCard) {
		//根据身份证号前6位获取此人所在的户籍城市
		String cityCode = loanUserMapper.getCityCodeByIdCardSix(idCard.substring(0, 6));
		String oldCity = "";
		if (StringUtils.isBlank(cityCode)) {
			oldCity = IsNativeUtils.isNative(idCard);
			if ("身份证号非法".equals(oldCity)) {
				cityCode = "000000";
			} else {
				//根据城市名称获取城市编码
				cityCode = hotCityMapper.getCityCodeByCityName(this.unifyLocations(oldCity));
				if (StringUtils.isBlank(cityCode)) {
					cityCode = "000000";
				}
			}
			//把没存在数据库里的身份证号前6位存起来
			loanUserMapper.setCardNoAndCityCode(idCard,cityCode);

		}
		return cityCode;
	}

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年7月5日下午6:31:23
	 * @Param:
	 * @Return:RequestLoanUserInfo
	 */
	private RequestLoanUserInfo isHidenOnApp(RequestLoanUserInfo loanUserInfo) {

		// 如果手机号码为空的话，设置单子ujdStatus=4，不显示
		if (StringUtils.isBlank(loanUserInfo.getPhone())) {
			loanUserInfo.setUjdStatus(4);
		}
		// 查看此人是否在黑名单里，如果在黑名单，设ujdStatus=4，不结算
		else if (this.getIsInBlackList(loanUserInfo) != null) {
			loanUserInfo.setUjdStatus(4);
		} else {
			// 获取当前时间
			Long orderTime = System.currentTimeMillis();
			// 查询此人之前是否申请过
			Long recentOrderTime = loanUserMapper.queryIsApplyRecently(loanUserInfo);
			// 如果申请时间在十天之内，把ujdStatus设为3,信贷经理端不显示
			Integer showTime = loanUserMapper.getShowTime();
			if (recentOrderTime != null && orderTime - recentOrderTime < showTime * 60 * 60 * 1000L) {
				loanUserInfo.setUjdStatus(3);
			}
		}

		return loanUserInfo;
	}

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年7月5日下午6:39:42
	 * @Param:
	 * @Return:Object
	 */
	private Integer getIsInBlackList(RequestLoanUserInfo loanUserInfo) {
		return loanUserMapper.getIsInBlackList(loanUserInfo);

	}

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年7月5日下午6:23:31
	 * @Param:
	 * @Return:String
	 */
	private String createMessage(RequestLoanUserInfo loanUserInfo, String location) {
		String name = loanUserInfo.getUserName();
		String sex = loanUserInfo.getSex();
		if ("男".equals(sex)) {
			sex = "先生";
		} else {
			sex = "女士";
		}
		name = name.substring(0, 1) + sex + ",";
		String city = location + ",";
		String job = loanUserMapper.getValueByCode(loanUserInfo.getJob()) + ",";
		String loanAmount = "贷款" + loanUserInfo.getAmount() + "万元,";
		String loanTerm = loanUserInfo.getTerm() + "期。";
		String message = name + city + job + loanAmount + loanTerm;
		return message;
	}

	/**
	 * @Author:Tina
	 * @Description:对单子进行评分
	 * @Date:2018年7月5日下午4:13:07
	 * @Param:
	 * @Return:RequestLoanUserInfo
	 */
	private RequestLoanUserInfo queryLoanScore(RequestLoanUserInfo loanUserInfo) {
		// 把实体类转为hashmap，方便进行评分的处理
		HashMap<String, Object> loanUserInfoMap = this.beanToMap(loanUserInfo);
		Integer score = 0;
		Integer price = 0;
		Integer isDebt = 0;
		Integer notDebt = 0;
		Integer zmxy_550 = 0;
		Integer zmxy_600 = 0;
		Integer zmxy_650 = 0;
		Integer zmxy = 0;
		Integer hasWebank = 0;

		// 到后台查询计分表
		List<RequestCreditScore> result = loanUserMapper.queryCreditScore();
		// 遍历计分表,查询用户传过来的信息里面有没有对应的字段,以及相应的分数
		for (RequestCreditScore r : result) {
			String field = r.getTypeIn();
			// 有无负债是用户填的数字,比较灵活,单独拿出来判断,所以先分别记录有无负债各算多少分
			if ("debt".equals(field)) {
				if (r.getOnlyCode() == 1601) {
					isDebt = r.getScore();
				} else {
					notDebt = r.getScore();
				}
			} else if ("zmxy".equals(field)) {
				if (r.getOnlyCode() == 1903) {
					zmxy_550 = r.getScore();
				} else if (r.getOnlyCode() == 1904) {
					zmxy_600 = r.getScore();
				} else if (r.getOnlyCode() == 1905) {
					zmxy_650 = r.getScore();
				}
			} else if ("webank".equals(field)) {
				if (r.getOnlyCode() == 2101) {
					hasWebank = r.getScore();
				}
			} else {

				Integer onlyCode =  (Integer) loanUserInfoMap.get(field);
				if (onlyCode != null && r.getOnlyCode().equals(onlyCode)) {
					score += r.getScore();
				}
			}
		}
		// 有无负债是用户填的数字,比较灵活,单独拿出来判断
		if (loanUserInfo.getDebt() != null && loanUserInfo.getDebt() > 0) {
			score +=  isDebt;
		} else {
			score += notDebt;
		}

		// 微粒贷之前填写的是有和无，现在填的是无和具体的额度，如果是具体的额度，就直接加分
		if (!StringUtils.isBlank(loanUserInfo.getWebank()) && !loanUserInfo.getWebank().equals("无")
				&& !loanUserInfo.getWebank().equals("暂未填写")) {
			score += hasWebank;
		}

		// 安信花那边传过来的芝麻信用分是具体的数值，我们这边是区间，如果在区间内的话，就给对应的分值
		if (loanUserInfo.getZmxy() != null && loanUserInfo.getZmxy().length() == 3) {
			zmxy = Integer.parseInt(loanUserInfo.getZmxy());
			if (zmxy >= 650) {
				score += zmxy_650;
			} else if (zmxy >= 600) {
				score += zmxy_600;
			} else if (zmxy >= 550) {
				score += zmxy_550;
			}
		}
		// 根据评分先算价格组成部分1
		int price1 = this.getPrice(score);
		// 根据借款金额算价格组成部分2,最低1分
		int price2 = loanUserInfo.getAmount() * 10000 / 30000;
		price2 = price2 == 0 ? 1 : price2;
		// 根据借款期限算价格组成部分3,最低1分
		int price3 = loanUserInfo.getTerm() / 12;
		price3 = price3 == 0 ? 1 : price3;
		// 算出最终价格
		price = price1 + price2 + price3;
		price = price > 50 ? 50 : price;
		loanUserInfo.setScore(score);
		loanUserInfo.setPrice(price);
		return loanUserInfo;
	}

	/**
	 * @Author:Tina
	 * @Description:bean转为map
	 * @Date:2018年7月5日下午4:38:58
	 * @Param:
	 * @Return:HashMap<String,Object>
	 */
	private HashMap<String, Object> beanToMap(RequestLoanUserInfo loanUserInfo) {
		HashMap<String, Object> map = Maps.newHashMap();
		if (loanUserInfo != null) {
			BeanMap beanMap = BeanMap.create(loanUserInfo);
			for (Object key : beanMap.keySet()) {
				map.put(key + "", beanMap.get(key));
			}
		}
		return map;
	}

	// 字段转化
	public RequestLoanUserInfo changeField(RequestLoanUserInfo loanUserInfo) {
		// 婚姻状况
		loanUserInfo.setMarried(loanUserInfo.getMarried() + 1000);
		// 职业
		loanUserInfo.setJob(loanUserInfo.getJob() + 2000);
		// 单位性质
		if (loanUserInfo.getCompanyType() != null) {
			loanUserInfo.setCompanyType(loanUserInfo.getCompanyType()+3000);
		}
		// 工资发放形式
		if (loanUserInfo.getIncome() != null) {
			loanUserInfo.setIncome(loanUserInfo.getIncome()+4000);
		}
		// 工作年限
		if (loanUserInfo.getWorkYear() != null) {
			loanUserInfo.setWorkYear(loanUserInfo.getWorkYear()+5000);
		}
		// 企业注册时间
		if (loanUserInfo.getRegisterTime() != null) {
			loanUserInfo.setRegisterTime(loanUserInfo.getRegisterTime()+6000);
		}
		// 社保
		if (loanUserInfo.getSheBao() != null) {
			loanUserInfo.setSheBao(loanUserInfo.getSheBao()+7000);
		}
		// 公积金
		if (loanUserInfo.getGongJiJin() != null) {
			loanUserInfo.setGongJiJin(loanUserInfo.getGongJiJin()+8000);
		}
		if (loanUserInfo.getHouseProperty() != null) {
			int houseProperty = loanUserInfo.getHouseProperty();
			if (houseProperty == 2) {// 有房产，接受抵押
				// 是否接受抵押字段设为是
				loanUserInfo.setHousePledge(2401);
				loanUserInfo.setHouse(loanUserInfo.getHouse() + 1100);
				loanUserInfo.setHouseAssessment(loanUserInfo.getHouseAssessment() + 1200);
			} else if (houseProperty == 3) {// 有房产，不接受抵押
				// 是否接受抵押字段设为否
				loanUserInfo.setHousePledge(2402);
				loanUserInfo.setHouse(loanUserInfo.getHouse() + 1100);
				loanUserInfo.setHouseAssessment(loanUserInfo.getHouseAssessment() + 1200);
			} else if (houseProperty == 4) {// 有房产
				// 房产设为有
				loanUserInfo.setHouse(1104);
			}

		}
		if (loanUserInfo.getCarProperty() != null) {
			int carProperty = loanUserInfo.getCarProperty();
			if (carProperty == 2) {// 有车产，接受抵押
				// 是否接受抵押字段设为是
				loanUserInfo.setCarPledge(2501);
				loanUserInfo.setCar(loanUserInfo.getCar() + 1400);
				loanUserInfo.setCarAssessment(loanUserInfo.getCarAssessment() + 1500);
			} else if (carProperty == 3) {// 有车产，不接受抵押
				// 是否接受抵押字段设为否
				loanUserInfo.setCarPledge(2502);
				loanUserInfo.setCar(loanUserInfo.getCar() + 1400);
				loanUserInfo.setCarAssessment(loanUserInfo.getCarAssessment() + 1500);
			} else if (carProperty == 4) {// 有车产
				// 车产设为有
				loanUserInfo.setCar(1404);
			}

		}
		// 保单
		if (loanUserInfo.getChit() != null) {
			loanUserInfo.setChit(loanUserInfo.getChit() + 1700);
		}
		// 信用卡使用状况
		if (loanUserInfo.getStatus() != null) {
			loanUserInfo.setStatus(loanUserInfo.getStatus() + 1800);
		}
		// 补充说明
		if (loanUserInfo.getTags() != null) {

			
			String[] tags = loanUserInfo.getTags().split(",");

			int tagsInt[] = new int[tags.length];
			for (int i = 0; i < tags.length; i++) {
				tagsInt[i]=Integer.parseInt(tags[i]) + 2200;
				
			}
			String tag = Arrays.toString(tagsInt);
			loanUserInfo.setTags(tag.substring(1, tag.length()-1));
		}

		return loanUserInfo;

	}


	// 再次查询此单有没有被抢
	public Integer queryStatusByOrderNo(long orderNo) {
		Integer result = loanUserMapper.queryStatusByOrderNo(orderNo);
		return result;
	}



	// 直接到贷款经理order表中查询是否存在
	public ResponseLoanOrder queryOrder(long orderNo) {
		ResponseLoanOrder loanOrder = loanUserMapper.queryOrder(orderNo);
		return loanOrder;
	}

	// 根据用户提交的借款申请中的所在城市到数据库中的城市名统一表中查一下转换成统一的城市名叫法,以便优接单软件按照所在城市搜索
	public String unifyLocations(String location) {
		String unifiedLocation = loanUserMapper.queryLocation(location);
		if (unifiedLocation == null) {
			unifiedLocation = "全国";
		}
		return unifiedLocation;
	}

	// 将小星借款app传过来的借款人信息保存到优接单的数据库(小星借款版本1.2.6以后启用)
	public long insertLoanUserInfo(HashMap<String, Object> loanUserInfo) throws Exception {
		// 将借款金额转换成单位元
		int loanAmount = (int) (Double.valueOf((loanUserInfo.get("amount").toString())) * 10000);
		// 传过来的电话号码是long,转换成string
		// String phone = loanUserInfo.get("phone").toString();
		// 获取贷款期限
		Integer termInt = (int) loanUserInfo.get("term");
		// 判断借款人是不是本地人
		String idCard = loanUserInfo.get("idCard").toString();
		String location = loanUserInfo.get("location").toString();
		String isNative = null;
		String oldCity = IsNativeUtils.isNative(idCard);
		if ("身份证号非法".equals(oldCity)) {
			isNative = "非本地户籍";
		}
		oldCity = this.unifyLocations(oldCity);
		if (location.equals(oldCity)) {
			isNative = "本地户籍";
		} else {
			isNative = "非本地户籍";
		}
		loanUserInfo.put("isNative", isNative);
		loanUserInfo.put("oldCity", oldCity);

		HashMap<String, Object> scoreAndPrice = this.queryScoreNew(loanUserInfo, loanAmount, termInt);
		String term = termInt.toString();
		// 旧的订单号生成方法,废弃
		// long orderNo = System.currentTimeMillis() + (int) (Math.random() *
		// 10000);
		long orderNo = idWorker.nextId();
		long orderTime = System.currentTimeMillis();
		loanUserInfo.putAll(scoreAndPrice);
		loanUserInfo.put("orderNo", orderNo);
		loanUserInfo.put("loanAmount", loanAmount);
		loanUserInfo.put("orderTime", orderTime);
		loanUserInfo.put("term", term);
		loanUserInfo.put("explain", "1个月以内");
		// loanUserInfo.put("phone",phone);

		// 根据职业和房产车产情况不同先清理map里可能的空字符串
		loanUserInfo = this.caseByCases(loanUserInfo);
		// ujdStatus默认设为0
		loanUserInfo.put("ujdStatus", 0);
		// 判断此人之前有没有申请过，并判断是否对此人进行渠道结算
		loanUserInfo = this.isCloseAmountBytime(loanUserInfo);
		// 查看此人是否在黑名单里，如果在黑名单，设ujdStatus=4，不结算
		if (this.queryIsInBlackList(loanUserInfo) != null) {
			loanUserInfo.put("ujdStatus", 4);
			loanUserInfo.put("closeAmount", 0);
		}
		// 如果手机号码为空的话，设置单子ujdStatus=4，不显示
		if (loanUserInfo.get("phone") == null || loanUserInfo.get("phone").toString() == "") {

			loanUserInfo.put("ujdStatus", 4);

		}

		if ("全国".equals(location)) {
			// 如果进来的单子城市定位是全国，就将status设为5，不显示在优接单
			loanUserInfo.put("ujdStatus", 5);
		}

		loanUserMapper.insertLoanUserInfo(loanUserInfo);
		if ("0".equals(loanUserInfo.get("ujdStatus").toString())) {
			// 先生成推送要显示的消息
			String message = this.createMessage(loanUserInfo);
			// 给关注这个城市的信贷经理发推送
			JiguangPush.jPushOrders((String) loanUserInfo.get("location"), message, orderNo);
		}
		return orderNo;
	}

	private HashMap<String, Object> caseByCases(HashMap<String, Object> loanUserInfo) {
		String job = loanUserInfo.get("job").toString();
		if (job.equals("上班族")) {
			loanUserInfo.remove("registerTime");
			loanUserInfo.remove("companyMonthlyIncome");
		}
		if (job.equals("自由职业")) {
			loanUserInfo.remove("registerTime");
			loanUserInfo.remove("companyMonthlyIncome");
			loanUserInfo.remove("companyType");
			loanUserInfo.remove("workYear");
		}
		if (job.equals("企业主")) {
			loanUserInfo.remove("companyType");
			loanUserInfo.remove("workYear");
			loanUserInfo.remove("monthlyIncome");
			loanUserInfo.remove("income");

		}
		// 名下房产
		String houseProperty = loanUserInfo.get("houseProperty").toString();
		if (houseProperty.equals("无房产")) {
			loanUserInfo.remove("house");
			loanUserInfo.remove("houseAddress");
			loanUserInfo.remove("houseAssessment");
			loanUserInfo.remove("housePledge");
		} else if (houseProperty.equals("有房产，接受抵押")) {
			loanUserInfo.put("housePledge", "是");
		} else {
			loanUserInfo.put("housePledge", "否");
		}
		// 名下车产
		String carProperty = loanUserInfo.get("carProperty").toString();
		if (carProperty.equals("无车辆")) {

			loanUserInfo.remove("car");
			loanUserInfo.remove("carAddress");
			loanUserInfo.remove("carAssessment");
			loanUserInfo.remove("carPledge");
		} else if (carProperty.equals("有车辆，接受抵押")) {
			loanUserInfo.put("carPledge", "是");
		} else {
			loanUserInfo.put("carPledge", "否");
		}
		return loanUserInfo;
	}

	// 计算信用分(小星借款1.2.6后启用)
	public HashMap<String, Object> queryScoreNew(HashMap<String, Object> loanUserInfo, int loanAmount, int term) {
		Integer score = 0;
		Integer price = 0;
		Integer isDebt = 0;
		Integer notDebt = 0;
		Integer zmxy_550 = 0;
		Integer zmxy_600 = 0;
		Integer zmxy_650 = 0;
		Integer zmxy = 0;
		Integer hasWebank = 0;

		// 到后台查询计分表
		List<CreditScore> result = loanUserMapper.queryScoreNew();
		// 遍历计分表,查询用户传过来的信息里面有没有对应的字段,以及相应的分数
		for (CreditScore r : result) {
			String field = r.getField();
			// 有无负债是用户填的数字,比较灵活,单独拿出来判断,所以先分别记录有无负债各算多少分
			if ("debt".equals(field)) {
				if ("有".equals(r.getType())) {
					isDebt = r.getScore();
				} else {
					notDebt = r.getScore();
				}
			} else if ("zmxy".equals(field)) {
				if (r.getType().startsWith("550")) {
					zmxy_550 = r.getScore();
				} else if (r.getType().startsWith("600")) {
					zmxy_600 = r.getScore();
				} else if (r.getType().startsWith("650")) {
					zmxy_650 = r.getScore();
				}
			} else if ("webank".equals(field)) {
				if ("有".equals(r.getType())) {
					hasWebank = r.getScore();
				}
			} else {

				String type = (String) loanUserInfo.get(field);
				if (StringUtils.isNotBlank(type) && r.getType().equals(type)) {
					score += r.getScore();
				}
			}
		}
		// 有无负债是用户填的数字,比较灵活,单独拿出来判断
		if (loanUserInfo.get("debt").equals("0") || StringUtils.isBlank((String) loanUserInfo.get("debt"))) {
			score += notDebt;
		} else {
			score += isDebt;
		}

		// 微粒贷之前填写的是有和无，现在填的是无和具体的额度，如果是具体的额度，就直接加分
		if (!StringUtils.isBlank((String) loanUserInfo.get("webank")) && !loanUserInfo.get("webank").equals("无")) {
			score += hasWebank;
		}

		// 安信花那边传过来的芝麻信用分是具体的数值，我们这边是区间，如果在区间内的话，就给对应的分值
		if (loanUserInfo.get("zmxy") != null && loanUserInfo.get("zmxy").toString().length() == 3) {
			zmxy = Integer.parseInt(loanUserInfo.get("zmxy").toString());
			if (zmxy >= 650) {
				score += zmxy_650;
			} else if (zmxy >= 600) {
				score += zmxy_600;
			} else if (zmxy >= 550) {
				score += zmxy_550;
			}
		}

		// 根据评分先算价格组成部分1
		int price1 = this.getPrice(score);
		// 根据借款金额算价格组成部分2,最低1分
		int price2 = loanAmount / 30000;
		price2 = price2 == 0 ? 1 : price2;
		// 根据借款期限算价格组成部分3,最低1分
		int price3 = term / 12;
		price3 = price3 == 0 ? 1 : price3;
		// 算出最终价格
		price = price1 + price2 + price3;
		price = price > 50 ? 50 : price;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("score", score);
		map.put("price", price);
		return map;
	}

	// 根据信用分算卖多少星币
	public Integer getPrice(int score) {
		Integer price = this.loanUserMapper.queryPrice(score);
		return price;
	}

	// 生成订单推送消息显示内容
	private String createMessage(HashMap<String, Object> loanUserInfo) throws Exception {
		String name = (String) loanUserInfo.get("userName");
		String sex = (String) loanUserInfo.get("sex");
		if ("男".equals(sex)) {
			sex = "先生";
		} else {
			sex = "女士";
		}
		name = name.substring(0, 1) + sex + ",";
		String city = loanUserInfo.get("location") + ",";
		String job = loanUserInfo.get("job") + ",";
		String loanAmount = "贷款" + loanUserInfo.get("amount") + "万元,";
		String loanTerm = loanUserInfo.get("term") + "期。";
		String message = name + city + job + loanAmount + loanTerm;
		return message;
	}

	// 根据身份证号码获取户籍所在地并判断是不是本地人
	public String isNative(String idCard, String location) {
		String result = "非本地户籍";
		String oldCity = IsNativeUtils.isNative(idCard);
		if ("身份证号非法".equals(oldCity)) {
			return result;
		}
		oldCity = this.unifyLocations(oldCity);
		if (location.equals(oldCity)) {
			result = "本地户籍";
		}
		return result;
	}

	// 查询此人是否在黑名单里
	public Integer queryIsInBlackList(HashMap<String, Object> loanUserInfo) {
		return loanUserMapper.queryIsInBlackList(loanUserInfo);

	}

	// 判断此人之前有没有申请过，并判断是否对此人进行渠道结算
	private HashMap<String, Object> isCloseAmountBytime(HashMap<String, Object> loanUserInfo) {
		// 获取当前时间
		Long orderTime = System.currentTimeMillis();
		// 查询此人之前是否申请过
		Long recentOrderTime = loanUserMapper.queryIsApply(loanUserInfo);
		// 如果申请时间在十天之内，把ujdStatus设为3,信贷经理端不显示
		if (recentOrderTime != null && orderTime - recentOrderTime < 864000000) {
			loanUserInfo.put("ujdStatus", 3);
		}
		return loanUserInfo;

	}

	public String queryLoanPhoneByOrderNo(Long orderNo) {
		return loanUserMapper.queryLoanPhoneByOrderNo(orderNo);
	}

	// 设置此单已被抢
	public Integer updateLoanUserStatus(long orderNo) {
		Integer isGrabbed = loanUserMapper.updateLoanUserStatus(orderNo);
		return isGrabbed;
	}
}
