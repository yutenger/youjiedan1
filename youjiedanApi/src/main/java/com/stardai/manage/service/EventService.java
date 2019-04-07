package com.stardai.manage.service;

import cn.jpush.api.push.PushResult;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stardai.manage.classloader.HotLoadClassLoader;
import com.stardai.manage.constants.Constants;
import com.stardai.manage.mapper.*;
import com.stardai.manage.pojo.*;
import com.stardai.manage.request.*;
import com.stardai.manage.response.*;
import com.stardai.manage.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author jokery
 * @create 2018-01-23 13:16
 **/

@Service
@SuppressWarnings("all")

public class EventService {

	@Autowired
	private Base64Image base;
	
    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ShareMapper shareMapper;

	@Autowired
	private CreditService creditService;

	@Autowired
	private CouponMapper couponMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private HotCityMapper hotCityMapper;

	@Autowired
	private RedisCacheManager redisCacheManager;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private BaseService baseService;
	private IdWorker idWorker = new IdWorker(0, 8);
	ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	private PayMoneyMapper payMoneyMapper;

	private static ScheduledExecutorService threadPool =  Executors.newScheduledThreadPool(10);
	protected static final Logger LOG = LoggerFactory.getLogger(EventService.class);

	private String[] phoneList = {"189****3450","156****7631","139****3657","150****1673","159****9737","157****3433","152****9597","189****9316","158****5804","152****1415","176****5201","159****7519","151****8224","130****5338","178****0182","130****7000","181****1681","131****1742","150****0918","186****1527","186****8058","176****0497","187****5801","152****8310","152****4068","132****0105","132****2122","133****6369","131****6645","157****7907"};

	private String[] roleList = {"李经理","胡经理","袁经理","徐经理","王经理","屈经理","李经理","阮经理","许经理","袁经理","肖经理","杨经理","赵经理","赵经理","曾经理","程经理","周经理","吴经理","邱经理","王经理","许经理","王经理","邓经理","李经理","石经理","王经理","王经理","钟经理","张经理","彭经理"};

    //查询当前有哪些活动要弹窗
    public List<Integer> queryEventsIndate(Long currentTime) {
        return this.eventMapper.queryEventsIndate(currentTime);
    }

    //查询要弹窗的活动的url,图片url以及优先度
    public List<HashMap<String,String>> queryUrlAndPriority(List<Integer> eventIDs) {
        return this.eventMapper.queryUrlAndPriority(eventIDs);
    }

	/**
	 * @Author:Tina
	 * @Description:添加企业合作的信息
	 * @Date:2018年5月9日下午1:27:09
	 * @Param:
	 * @Return:Integer
	 */
	public Integer addCompanyCooperation(RequestCompanyCooperation rcc) {
		//一个号码7天内只能申请一次企业合作
		String phone = eventMapper.queryIsCooperation(rcc.getMobileNumber());
		//7天内没申请过企业合作，就插入一条新的信息
		if(phone == null ){
			return eventMapper.addCompanyCooperation(rcc);
		}
		return 2;
	}

	/**
	 * @Author:Tina
	 * @Description:根据活动id,查询活动的具体信息
	 * @Date:2018年5月10日下午4:18:29
	 * @Param:
	 * @Return:List<HashMap<String,String>>
	 */
	public ResponsePopupInfo queryEventInfo(int eventId) {
		return eventMapper.queryEventInfo(eventId);
	}

	/**
	 * @Author:Tina
	 * @Description:查询当前版本需要的弹屏内容（安卓）
	 * @Date:2018年5月7日下午2:07:53
	 * @Param:
	 * @Return:List<Integer>
	 */
	public  List<ResponsePopupInfo> queryPopUpIndateForAndroid(Long currentTime,String versionCode) {
		return this.eventMapper.queryPopUpIndateForAndroid(currentTime,versionCode);
	}
	
	/**
	 * @Author:Tina
	 * @Description:获取安卓端版本更新信息
	 * @Date:2018年5月10日下午5:32:23
	 * @Param:
	 * @Return:List<ResponsePopupInfo>
	 */
	public ResponsePopupInfo getAndroidVersionInfo() {
		
		return eventMapper.getAndroidVersionInfo();
	}

	/**
	 * @Author:Tina
	 * @Description:查询当前版本需要的弹屏内容（iOS）
	 * @Date:2018年5月11日上午9:41:52
	 * @Param:
	 * @Return:List<ResponsePopupInfo>
	 */
	public List<ResponsePopupInfo> queryPopUpIndateForiOS(Long currentTime, String versionId) {
		return eventMapper.queryPopUpIndateForiOS(currentTime,versionId);
	}

	/**
	 * @Author:Tina
	 * @Description:获取iOS端版本更新信息
	 * @Date:2018年5月11日上午9:42:21
	 * @Param:
	 * @Return:ResponsePopupInfo
	 */
	public ResponsePopupInfo getiOSVersionInfo() {
		return eventMapper.getiOSVersionInfo();
	}

	/**
	 * @Author:Tina
	 * @Description:当要弹屏的活动是邀请时，去系统公告里查priority=123的数据
	 * @Date:2018年5月11日下午2:26:09
	 * @Param:
	 * @Return:ResponsePopupInfo
	 */
	public ResponsePopupInfo queryInventEventInfo() {
		return eventMapper.queryInventEventInfo();
	}

	/**
	 * @Author:Tina
	 * @Description:根据版本code获取ios版本
	 * @Date:2018年6月9日下午12:44:00
	 * @Param:
	 * @Return:String
	 */
	public String getIosVersionByVersionCode(String versionCode) {
		
		return eventMapper.getIosVersionByVersionCode(versionCode);
	}

	/**
	 * @Author:Tina
	 * @Description:根据ios版本获取安卓的版本号
	 * @Date:2018年6月9日下午12:48:43
	 * @Param:
	 * @Return:String
	 */
	public String getVersionCodeByIosVersion(String iosVersion) {
		return eventMapper.getVersionCodeByIosVersion(iosVersion);
	}
	//查询发现页面的轮播图列表
    public List<HashMap<String,Object>> queryCarouselList() {
		return eventMapper.queryCarouselList();
    }

    //用户抽取奖品，为用户发放奖励
    public void issueRewardForUser(String userId, Gift gift) {
		//为用户发放奖励
		int couponFullreduction = gift.getCouponFullreduction();
		RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();//为用户发送优惠券
		requestCouponForNewUser.setUserId(userId);
		requestCouponForNewUser.setCouponAmount(gift.getCouponAmount());
		requestCouponForNewUser.setEffectiveTime(DateUtil.getTime());
		requestCouponForNewUser.setExpirationTime(DateUtil.getAfterDayDate("1"));
		requestCouponForNewUser.setCouponChannel("抽奖活动赠送");
		if(couponFullreduction == 0){//不是满减券
			requestCouponForNewUser.setCouponId( "QW" + idWorker.nextId());
			requestCouponForNewUser.setCouponType(2);
			requestCouponForNewUser.setCouponForm(3);
		}else{
			requestCouponForNewUser.setCouponId( "QM" + idWorker.nextId());
			requestCouponForNewUser.setCouponType(2);
			requestCouponForNewUser.setCouponForm(2);
			requestCouponForNewUser.setCouponFullreduction(gift.getCouponFullreduction());
		}

		Integer result = couponMapper.addCouponForGettingUser(requestCouponForNewUser);

		//用户抽奖，扣除一次抽奖次数
		orderMapper.reviseLotteryChance(userId,DateUtil.getDay());
		//往奖励表插入一条用户获取奖励的记录
		eventMapper.addLotteryForUser(userId,gift.getCouponAmount(),gift.getCouponFullreduction(),gift.getName());
		//发推送
		threadPool.schedule(()->{
			PushResult pushResult = JiguangPush.push(userId,"恭喜您获得"+gift.getName()+"一张，请尽快使用！");
			if (pushResult != null && pushResult.isResultOK()) {
				LOG.info("针对" + userId + "的优惠券通知推送成功！");
			} else {
				LOG.info("针对" + userId + "的优惠券通知推送失败！");
			}
		},3, TimeUnit.SECONDS);

    }

    //用户抽奖获取的奖励列表
	public List<ResponseLotteryList> queryLotteryReward(String userId,int page,int pageSize) {
		page = page * pageSize;
		List<ResponseLotteryList> lotteryRewardList = eventMapper.queryLotteryReward(userId,page,pageSize);
		return lotteryRewardList;
	}

	//查询全部的中奖纪录用作轮播
	public ArrayList<String> queryAllLotteryReward() {
		ArrayList<String> rewardList = new ArrayList<String>();
		int type = 0;
		ResponseUser roleAndPhone = new ResponseUser();
		List<ResponseLotteryList> lotteryRewardList = eventMapper.queryAllLotteryReward();
		for(ResponseLotteryList rll : lotteryRewardList){
			roleAndPhone = userMapper.getRoleAndPhoneByUserId(rll.getUserId());
			if(StringUtils.isNotBlank(roleAndPhone.getRole())){
			rewardList.add("恭喜"+roleAndPhone.getRole()+
							roleAndPhone.getMobileNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2")+
							"抽中"+rll.getLotteryName());
			}
		}
		return rewardList;
	}


	////展业海报首页热门推荐列表
    public List<ResponsePosterHotList> queryPosterHotList(RequestPosterList requestPosterList) {
		int page = requestPosterList.getPage() * requestPosterList.getPageSize();
		requestPosterList.setPage(page);
		return eventMapper.queryPosterHotList(requestPosterList);
    }

	public ArrayList<String> queryPosterRecord(RequestPosterList requestPosterList) {
		ArrayList<String> posterList = new ArrayList<String>();
		int type = requestPosterList.getType();
		switch (type){
			case 1:
				//搜索页面的热门推荐
				posterList = eventMapper.queryHotSearch();
				break;
			case 2:
				//搜索页面的历史纪录
				posterList = eventMapper.queryHistorySearch(requestPosterList.getUserId());
				break;
			default:
				break;
		}
		return posterList;
	}

	//根据海报名称搜索海报内容
    public List<ResponsePosterHotList> queryPosterByKeyWord(RequestPosterList requestPosterList) {
		//先看之前是否搜索过，没有就加一条，有的话就更新
		Integer count = eventMapper.queryIsSearchBefore(requestPosterList);
		if(count!= null && count == 1){
			eventMapper.updateSearchRecord(requestPosterList);
		}else{
			//记录该用户搜索的纪录
			eventMapper.addSearchRecord(requestPosterList);
		}

		//根据名称查出海报具体内容
		List<ResponsePosterHotList> rphl = eventMapper.queryPosterByKeyWord(requestPosterList);
		return rphl;
    }

	public Integer setHistoryRecord(String userId) {
		return eventMapper.setHistoryRecord(userId);
	}

	public Integer addUsingCount(RequestPosterList requestPosterList) {
		return  eventMapper.addUsingCount(requestPosterList);
	}

	//展业海报指定分类进去调的接口
    public List<ResponsePosterHotList> queryPosterForEachSort(RequestPosterList requestPosterList) {
		int page = requestPosterList.getPage() * requestPosterList.getPageSize();
		requestPosterList.setPage(page);

		return eventMapper.queryPosterForEachSort(requestPosterList);
    }

	public String queryCreditRecord(RequestPerson requestPerson,ResponseMoney responseMoney) throws Exception{
		String orderNo=String.valueOf(IdGen.get().nextId());
		requestPerson.setOrderNo(orderNo);

		Integer costMoney = eventMapper.queryCreditSearchCost();
		//创建http post请求
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String parameters = JSON.toJSONString(requestPerson);
		//输入url
		HttpPost httpPost = new HttpPost(Constants.data_query);
		//模拟浏览器进行访问
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		//构造一个form表单的实体
		StringEntity stringEntity = new StringEntity(parameters, ContentType.APPLICATION_JSON);
		// 将请求实体设置到httpPost对象中
		httpPost.setEntity(stringEntity);
		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpclient.execute(httpPost);
			int code = response.getStatusLine().getStatusCode();
			//判断返回状态是不是200
			if (response.getStatusLine().getStatusCode() == 200) {
				String message = EntityUtils.toString(response.getEntity(), "UTF-8");
				JsonNode jsonNode = MAPPER.readTree(message);
				if("10000".equals(jsonNode.get("code").toString().replace("\"",""))){

					//扣除用户星币
					String userId = requestPerson.getUserId();
					double recharge = responseMoney.getMoneyRecharge();
					double present = responseMoney.getMoneyPresent();
					double leftMoney = 0;
					//先扣除用户充值的星币，再扣除给用户赠送的星币
					if(recharge > costMoney){
						leftMoney = recharge - costMoney;
						payMoneyMapper.updateRechargeMoney(userId, leftMoney);

					}else{
						leftMoney = present - (costMoney - recharge);
						// 修改数据库金额
						payMoneyMapper.updatePresentUserMoney(userId, leftMoney);
					}
					String outNo = System.currentTimeMillis() + new SimpleDateFormat("yyyyMMdd").format(new Date());
					// 向支出表插入支出明细
					payMoneyMapper.addOutComeMoneyForCoupon(userId, Long.valueOf(orderNo), outNo, "信用查询", costMoney , 0.0);

					message = jsonNode.get("result").get("data").toString();
					LOG.info(requestPerson.toString()+"大数据查询成功："+message);
					//往自己数据库加一条查询记录
					eventMapper.addCreditRecord(requestPerson);
					return message;
				}else{
					LOG.info("返回结果失败，message："+message);
					return null;
				}
			}else{
				LOG.info("请求借款端接口失败,状态码："+code);
				return null;
			}
		} finally {
			if (response != null) {
				response.close();
			}
			httpclient.close();
		}


	}

    public ResponseCouponByGrabOrder queryCouponByGrabOrder(String userId) {

		ResponseCouponByGrabOrder responseCouponByGrabOrder = eventMapper.queryCouponByGrabOrder(userId);
		if(responseCouponByGrabOrder == null){
			responseCouponByGrabOrder = new ResponseCouponByGrabOrder();
			responseCouponByGrabOrder.setOrderAmount(00000);
			responseCouponByGrabOrder.setCouponLimit(0);
			responseCouponByGrabOrder.setCouponNolimit(0);
		}
		String reKey = "GrabOrderGetCoupon_" + userId + "_" + DateUtil.getDay();
		if (!redisCacheManager.hasKey(reKey)) {
			responseCouponByGrabOrder.setOrderAmountForLimit(3);
			responseCouponByGrabOrder.setOrderAmountFornoLimit(5);
		}else{
			int revalue = (int) redisCacheManager.get(reKey);
			if(revalue < 3){
				responseCouponByGrabOrder.setOrderAmountForLimit(3-revalue);
				responseCouponByGrabOrder.setOrderAmountFornoLimit(5-revalue);
			}else{
				responseCouponByGrabOrder.setOrderAmountForLimit(8-revalue);
				responseCouponByGrabOrder.setOrderAmountFornoLimit(5-revalue);
			}

		}
		return responseCouponByGrabOrder;
    }

	//查询大数据查询需要的星币
	public Integer queryCreditSearchCost() {
		return eventMapper.queryCreditSearchCost();
	}

	//查看哪些城市正在打折
    public List<HashMap<String,Object>> queryDiscountCity() {
		String city = eventMapper.queryDiscountCity();
		List<HashMap<String, Object>> cityNameAndCode = new ArrayList<HashMap<String, Object>>();
		if(StringUtils.isNotBlank(city)){
			List<String> list =  Arrays.asList(city.split(","));
			cityNameAndCode = hotCityMapper
					.getCityCodeAndNameByCityCode(new ArrayList<String>(list));
		}

		return cityNameAndCode;
    }

    //用户抽奖
	 public Gift getLottery(HashMap<String,Integer> count) {
		Gift couponOne = new Gift();
		couponOne.setId(0);
		couponOne.setName("满20减5抢单券");
		couponOne.setCouponAmount(5);
		couponOne.setCouponFullreduction(20);

		Gift couponTwo = new Gift();
		couponTwo.setId(1);
		couponTwo.setName("满30减10抢单券");
		couponTwo.setCouponAmount(10);
		couponTwo.setCouponFullreduction(30);

		Gift couponThree = new Gift();
		couponThree.setId(2);
		couponThree.setName("满40减15抢单券");
		couponThree.setCouponAmount(15);
		couponThree.setCouponFullreduction(40);

		Gift couponFour = new Gift();
		couponFour.setId(3);
		couponFour.setName("5元无门槛抢单券");
		couponFour.setCouponAmount(5);
		couponFour.setCouponFullreduction(0);

		Gift couponFive = new Gift();
		couponFive.setId(4);
		couponFive.setName("10元无门槛抢单券");
		couponFive.setCouponAmount(10);
		couponFive.setCouponFullreduction(0);

		Gift couponSix = new Gift();
		couponSix.setId(5);
		couponSix.setName("免单券");
		couponSix.setCouponAmount(50);
		couponSix.setCouponFullreduction(0);

		Integer leftCount = count.get("leftCount");
		Integer totalCount = count.get("totalCount");
		List<Gift> list = new ArrayList<Gift>();
		if(leftCount.equals(totalCount)){
			couponOne.setProb(0.2D);
			couponTwo.setProb(0.2D);
			couponThree.setProb(0.2D);
			couponFour.setProb(0.2D);
			couponFive.setProb(0.2D);
			list.add(couponOne);
			list.add(couponTwo);
			list.add(couponThree);
			list.add(couponFour);
			list.add(couponFive);


		}else {
			couponOne.setProb(0.18D);
			couponTwo.setProb(0.18D);
			couponThree.setProb(0.18D);
			couponFour.setProb(0.18D);
			couponFive.setProb(0.18D);
			couponSix.setProb(0.1D);
			list.add(couponOne);
			list.add(couponTwo);
			list.add(couponThree);
			list.add(couponFour);
			list.add(couponFive);
			list.add(couponSix);
		}

		int index = DrawLotteryUtil.drawGift(list);
		return list.get(index);

	}

	//获取用户可抽奖次数及再抢N单即可抽奖
	public HashMap<String,Integer> queryLotteryDetail(String userId) {
		String reKey = "GrabOrderGetLottery_" + userId + "_" + DateUtil.getDay();
		int revalue = 0;
		HashMap<String,Integer> count =orderMapper.getLotteryChance(userId,DateUtil.getDay());
		 if(count!=null) {
			 count.remove("totalCount");
			 Integer leftCount = count.get("leftCount");
			 if (redisCacheManager.hasKey(reKey)) {
				 revalue = (int) redisCacheManager.get(reKey);

			 }
		 }else{
		 	count=new HashMap<>();
			 count.put("leftCount",0);
		 }
		int needOrder = orderMapper.getOrderCountForLottery(System.currentTimeMillis());
		int orderCount = needOrder - revalue % needOrder;
		count.put("orderCount",orderCount);
		return count;
	}
	//查询当前用户当日花费多少星币
	public  int queryDayConsumption(String day,String userId){
		return eventMapper.queryDayConsumption(day,userId);
	}
	//获取活动信息
	public  Map<String,Object> getActivityInfo(int type){
		return eventMapper.getActivityInfo(type);
	}
    public  Map<String,Object> getActivityInfoById(int type){
		return eventMapper.getActivityInfoById(type);
	}
	public  boolean queryIsLimit(long start,long end,String userId){
		return eventMapper.queryIsLimit(start,end,userId)>0?true:false;
	}

	public void activityCount(String userId) throws Exception {
		String date = VeDate.getStringDateShort();
		String sql = "select * from snatch_a_single_invoice_record where user_id=? and create_date=?";
		SnatchRecord record = baseService.queryBySql(sql, SnatchRecord.class, userId, date);
		int count=0;
		int available5=0;
		int available10=0;
		int available50=0;
		if (record == null) {
			eventMapper.insertSnatchRecord(userId);
		} else {
			count=record.getCount()+1;
			int remain=count%5;
			switch(remain){
				case 0:available50=1;
				     break;
				case 2:available5=1;
				     break;
				case 3:available10=1;
				break;
				default:
				break;
			}
			eventMapper.updateSnatchRecord(userId,date,1,available5,available10,available50);
		}
	}
	public void updateSnatchRecord(String userId, String date,int count, Integer available5, Integer available10, Integer available50){
		 eventMapper.updateSnatchRecord(userId,date,count,available5,available10,available50);
	}
	public int isRegistrationDuringTheEvent(String userId,String startTime,String endTime){
		return eventMapper.isRegistrationDuringTheEvent(userId,startTime,endTime);
	}
	public void addPvUv(String ip,String buttonType){
		eventMapper.addPvUv(ip,buttonType);
	}

	/* 弹屏逻辑
	 * @param versionCode
	 * @param osType
	 * @param userId
	 * @return
	 */
	public ResponseScreenInfo getPopupScreenInfo(String versionCode,String osType,String userId){
		ScreenSetting screenSetting=eventMapper.getScreenSettingsByVersionCode(versionCode,osType);
		if(screenSetting==null){
			return null;
		}
		Integer versionUpdateSwitch=screenSetting.getVersionUpdateSwitch();
		//版本更新
		if(versionUpdateSwitch==1){
			String versionUpdateId=screenSetting.getVersionUpdateId();
			AppVersionInfo appVersionInfo=eventMapper.getVersionInfoByOsType(osType);
			if(versionCode.compareTo(appVersionInfo.getVersionCode())<0) {
				ShellScreenMaterial shellScreenMaterial = eventMapper.getShellScreenMaterialById(versionUpdateId);
				if (appVersionInfo == null || shellScreenMaterial == null) {
					return new ResponseScreenInfo();
				}
				String property = shellScreenMaterial.getProperty();
				appVersionInfo.setIsForce(Integer.valueOf(property));
				appVersionInfo.setImgUrl(shellScreenMaterial.getImgUrl());
				appVersionInfo.setTitle(shellScreenMaterial.getTitle());
				return new ResponseScreenInfo(appVersionInfo);
			}
		}
		List<ShellScreenMaterial> shellScreenMaterials=new LinkedList<>();
		String date=VeDate.getStringDate();
		Integer specialGroupSwicth=screenSetting.getSpecialGroupSwicth();
		String specialGroupStart=screenSetting.getSpecialGroupStart();
		String specialGroupEnd=screenSetting.getSpecialGroupEnd();
		//特殊人群
		if(specialGroupSwicth==1&&date.compareTo(specialGroupStart)>=0&&date.compareTo(specialGroupEnd)<=0){
			String specialGroupId=screenSetting.getSpecialGroupId();
			List<String> groupIdList=Arrays.asList(specialGroupId.split(","));
			groupIdList.forEach(groupId->{
				ShellScreenMaterial shellScreenMaterial=eventMapper.getShellScreenMaterialById(groupId);
				String property=shellScreenMaterial.getProperty();
				CrowdCategoryMapping crowdCategoryMapping=eventMapper.getPropertyMappingByProperty(property);
				String methodName=crowdCategoryMapping.getPropertyMapping();
				boolean result= (boolean) ReflectionClassUtils.invokeMethod(null,"com.stardai.manage.hotLoadClass.CrowdCategoryMapping",methodName,userId);
				if(result){
					shellScreenMaterials.add(shellScreenMaterial);
					return;
				}
			});
			if(shellScreenMaterials.size()>0) {
				return new ResponseScreenInfo(shellScreenMaterials.get(0));
			}
		}
		Integer allPopulationSwitch=screenSetting.getAllPopulationSwitch();
		String allPopulationStart=screenSetting.getAllPopulationStart();
		String allPopulationEnd=screenSetting.getAllPopulationEnd();
		//所有人群
		if(allPopulationSwitch==1&&date.compareTo(allPopulationStart)>=0&&date.compareTo(allPopulationEnd)<=0){
		String allPopulationId=	screenSetting.getAllPopulationId();
			List<String> groupIdList=Arrays.asList(allPopulationId.split(","));
			groupIdList.forEach(groupId->{
				ShellScreenMaterial shellScreenMaterial=eventMapper.getShellScreenMaterialById(groupId);
				shellScreenMaterials.add(shellScreenMaterial);
			});
		}
		return shellScreenMaterials.size()==0?new ResponseScreenInfo():new ResponseScreenInfo(shellScreenMaterials);
	}
	public void addStatisticsPvUv(String eventId,String type,String ip){
		eventMapper.addStatisticsPvUv(eventId,type,ip);
	}

}
