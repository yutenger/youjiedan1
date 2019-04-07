package com.stardai.manage.controller;

import cn.jpush.api.push.PushResult;
import com.alibaba.fastjson.JSONObject;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.config.Log;
import com.stardai.manage.constants.Constants;
import com.stardai.manage.pojo.Gift;
import com.stardai.manage.pojo.SnatchRecord;
import com.stardai.manage.request.RequestCompanyCooperation;
import com.stardai.manage.request.RequestCouponByUserId;
import com.stardai.manage.request.RequestPerson;
import com.stardai.manage.request.RequestPosterList;
import com.stardai.manage.response.*;
import com.stardai.manage.service.*;

import com.stardai.manage.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动
 *
 * @author jokery
 * @create 2018-01-23 11:09
 **/

@Controller
@RequestMapping(value = "MZevent")
@SuppressWarnings("all")

public class EventController extends BaseController {

	protected static final Logger LOG = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

	@Autowired
	HttpServletRequest request;

    @Autowired
	private VersionService versionCheckService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PayMoneyService payMoneyService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private BaseService baseService;

	@Autowired
	private UserService userService;

	private IdWorker idWorker = new IdWorker(0, 8);
    /**
     * 
     * @Author:Tina
     * @Description:登录时弹屏设置
     * @Date:2018年5月4日下午4:10:40
     * @Param:
     * @Return:ResponseModel
     */
    @RequestMapping(value = "popupScreenOnLoginAndroid",method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel popupScreenOnLoginAndroid (@RequestBody HashMap<String,String> versionInfo, BindingResult bindingResult) {
        
    	if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}

    	try {
        	String versionCode = versionInfo.get("versionCode");
        	
        	List<ResponsePopupInfo> data = new ArrayList<ResponsePopupInfo>();
            //查询当前系统时间
            Long currentTime = System.currentTimeMillis();
            
            if(StringUtils.isBlank(versionCode)){
            	return ResponseModel.fail("参数错误");
            }
            
            //查询需要弹屏的内容 (1 版本更新 ，2 h5活动，3 邀请同行，4 充值， 5 认证)
            List<ResponsePopupInfo> eventIDs = this.eventService.queryPopUpIndateForAndroid(currentTime,versionCode);
            //如果返回的list为空,则表示当前没有活动
            if (eventIDs != null && eventIDs.size() != 0) {
            	//eventIDs里面有两个值，priority(返回此版本所要弹的屏),eventId(要弹屏活动的id)
            	for(ResponsePopupInfo eventID : eventIDs){ 
            		if(eventID.getPriority() == 1){ //版本更新
            			eventID = eventService.getAndroidVersionInfo();
            			if(eventID == null){
            				return ResponseModel.error("未查询到当前系统下的最新版本号");
            			}
            			eventID.setPriority(1);
            		}else if(eventID.getPriority() == 2){
            			eventID = eventService.queryEventInfo(eventID.getEventId());
            			if(eventID == null){
            				return ResponseModel.error("活动数据异常");
            			}
            			eventID.setPriority(2);
            		}else if(eventID.getPriority() == 3){
            			eventID = eventService.queryInventEventInfo();//如果是邀请同行活动时，就去系统公告表里查询priority=123的数据
            			if(eventID == null){
            				return ResponseModel.error("活动数据异常");
            			}
            			eventID.setPriority(3);
            		}
            		data.add(eventID);
            		 
            	}
            	return ResponseModel.success(data);
            }else {
                return ResponseModel.fail("暂无活动");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
		
    }
    
    @RequestMapping(value = "popupScreenOnLoginiOS",method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel popupScreenOnLoginiOS (@RequestBody HashMap<String,String> versionInfo, BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
    	try {
        	String versionId = versionInfo.get("versionId");
        	if(StringUtils.isBlank(versionId)){
             	return ResponseModel.fail("参数错误");
             }
        	
        	List<ResponsePopupInfo> data = new ArrayList<ResponsePopupInfo>();
            //查询当前系统时间
            Long currentTime = System.currentTimeMillis();
            //查询需要弹屏的内容(1 版本更新 ，2 h5活动，3 邀请同行，4 充值， 5 认证)
            List<ResponsePopupInfo> eventIDs = this.eventService.queryPopUpIndateForiOS(currentTime,versionId);
            //如果返回的list为空,则表示当前没有活动
            if (eventIDs != null && eventIDs.size() != 0) {
            	//eventIDs里面有两个值，priority(返回此版本所要弹的屏),eventId(要弹屏活动的id)
            	for(ResponsePopupInfo eventID : eventIDs){ 
            		if(eventID.getPriority() == 1){ //版本更新
            			eventID = eventService.getiOSVersionInfo();
            			if(eventID == null){
            				return ResponseModel.error("未查询到当前系统下的最新版本号");
            			}
            			eventID.setPriority(1);
            		}else if(eventID.getPriority() == 2){
            			eventID = eventService.queryEventInfo(eventID.getEventId());
            			if(eventID == null){
            				return ResponseModel.error("活动数据异常");
            			}
            			eventID.setPriority(2);
            		}else if(eventID.getPriority() == 3){
            			eventID = eventService.queryInventEventInfo();//如果是邀请同行活动时，就去系统公告表里查询priority=123的数据
            			if(eventID == null){
            				return ResponseModel.error("活动数据异常");
            			}
            			eventID.setPriority(3);
            		}
            		data.add(eventID);
            	}
            	return ResponseModel.success(data);
            }else {
                return ResponseModel.fail("暂无活动");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
		
    }
    
    /**
     * 
     * @Author:Tina
     * @Description:添加企业合作的信息
     * @Date:2018年5月9日上午11:26:37
     * @Param:
     * @Return:ResponseModel
     */
    @ResponseBody
	@RequestMapping(value = "addCompanyCooperation", method = RequestMethod.POST)
	public ResponseModel addCompanyCooperation(@RequestBody RequestCompanyCooperation rcc, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ResponseModel.error(bindingResult);
			}
			
			Integer result = eventService.addCompanyCooperation(rcc);
			if (result != null && result == 1) {
				return ResponseModel.success("提交成功");
			} else {
				return ResponseModel.error("请勿重复提交");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail("网络异常,请稍后再试");
		}
	}

	//发现界面的轮播图显示
	@ResponseBody
	@RequestMapping(value = "getCarouselImage", method = RequestMethod.GET)
	public ResponseModel getCarouselImage( ) {

		List<HashMap<String,Object>> carouselList = eventService.queryCarouselList();
		if( carouselList != null && carouselList.size() > 0){
			return ResponseModel.success(carouselList);
		}
		return ResponseModel.fail("网络异常,请稍后再试");
	}

	//临时性的抽奖活动
	@Log("用户抽奖")
	@ResponseBody
	@RequestMapping(value = "lotteryEvent", method = RequestMethod.GET)
	public ResponseModel lotteryEvent(@RequestParam("userId") String userId) {
    	if(StringUtils.isBlank(userId)){
			LOG.error("userId 为空");
			return ResponseModel.fail("参数错误");
		}
		/*ResponseMoney userAccount = payMoneyService.queryAllMoney(userId);
		if(userAccount != null){
			if(userAccount.getAllMoney() < Constants.MONEYFORLOTTERY){
				LOG.error("星币余额不足");
				return ResponseModel.fail("您的星币余额不足");
			}
		}else{
			LOG.error("用户余额为空 ");
			return ResponseModel.fail("参数错误");
		}*/
		String today =  DateUtil.getDay();
		//查询是否有抽奖机会
		HashMap<String,Integer> count = orderService.getLotteryChance(userId,today);
		if(count==null){
			return ResponseModel.fail("您暂无抽奖机会");
		}
		Integer leftCount = count.get("leftCount");
		if(leftCount != null && leftCount > 0){
			//抽奖
			Gift gift = eventService.getLottery(count);
			//为用户发放奖励
			eventService.issueRewardForUser(userId,gift);
			gift.setProb(0);
			return ResponseModel.success(gift);

		}else{
			return ResponseModel.fail("您暂无抽奖机会");
		}
	}

	//获取用户的抽奖记录
	@Log("查看我的奖品")
	@ResponseBody
	@RequestMapping(value = "queryLotteryReward", method = RequestMethod.GET)
	public ResponseModel queryLotteryReward(@RequestParam("userId") String userId,@RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize) {
		if(StringUtils.isBlank(userId)){
			LOG.error("userId 为空");
			return ResponseModel.fail("网络异常,请稍后再试");
		}

		List<ResponseLotteryList> lotteryRewardList = eventService.queryLotteryReward(userId,page,pageSize);
		if( lotteryRewardList != null && lotteryRewardList.size() > 0){
			return ResponseModel.success(lotteryRewardList);
		}
		return ResponseModel.success();
	}

	//抽奖页面上方的轮播图显示
	@ResponseBody
	@RequestMapping(value = "queryLotteryCarouselInfo", method = RequestMethod.GET)
	public ResponseModel queryLotteryCarouselInfo() {
    	//查询最新的30条中奖纪录
		ArrayList<String> lotteryRewardList = eventService.queryAllLotteryReward();
		if( lotteryRewardList != null && lotteryRewardList.size() > 0){
			return ResponseModel.success(lotteryRewardList);
		}
		return ResponseModel.success();
	}

	//获取用户可抽奖次数及再抢N单即可抽奖
	@ResponseBody
	@RequestMapping(value = "queryLotteryDetail", method = RequestMethod.GET)
	public ResponseModel queryLotteryDetail(@RequestParam("userId") String userId) {
		if(StringUtils.isBlank(userId)){
			LOG.error("userId 为空");
			return ResponseModel.fail("网络异常,请稍后再试");
		}
        try {
			HashMap<String, Integer> lotteryDetail = eventService.queryLotteryDetail(userId);
			return ResponseModel.success(lotteryDetail);
	    }catch(Exception e){
        	e.printStackTrace();
        	return ResponseModel.fail("网络异常");
		}

	}



	//展业海报首页热门推荐列表
	@ResponseBody
	@RequestMapping(value = "queryPosterHotList", method = RequestMethod.POST)
	public ResponseModel queryPosterHotList(@RequestBody RequestPosterList requestPosterList, BindingResult bindingResult) {
    	if(StringUtils.isBlank(request.getHeader("MianZeToken")) || StringUtils.isBlank(request.getHeader("userId"))){
			return ResponseModel.fail("网络异常,请稍后再试");
		}
		List<ResponsePosterHotList> posterHotList = eventService.queryPosterHotList(requestPosterList);

		if( posterHotList != null && posterHotList.size() > 0){
			return ResponseModel.success(posterHotList);
		}
		return ResponseModel.success();
	}

	//展业海报指定分类进去调的接口
	@ResponseBody
	@RequestMapping(value = "queryPosterForEachSort", method = RequestMethod.POST)
	public ResponseModel queryPosterForEachSort(@RequestBody RequestPosterList requestPosterList, BindingResult bindingResult) {

		if(StringUtils.isBlank(request.getHeader("MianZeToken")) || StringUtils.isBlank(request.getHeader("userId"))){
			return ResponseModel.fail("网络异常,请稍后再试");
		}
		List<ResponsePosterHotList> posterHotList = eventService.queryPosterForEachSort(requestPosterList);

		if( posterHotList != null && posterHotList.size() > 0){
			return ResponseModel.success(posterHotList);
		}
		return ResponseModel.success();
	}



	//搜索页面，展业海报的热门推荐或历史纪录
	@ResponseBody
	@RequestMapping(value = "queryPosterRecord", method = RequestMethod.POST)
	public ResponseModel queryPosterRecord(@RequestBody RequestPosterList requestPosterList, BindingResult bindingResult) {

    	if (bindingResult.hasErrors()) {
				return ResponseModel.error(bindingResult);
    	}
		ArrayList<String> posterList = eventService.queryPosterRecord(requestPosterList);
		if( posterList != null && posterList.size() > 0){
			return ResponseModel.success(posterList);
		}
		return ResponseModel.success();
	}

	//根据海报名称搜索海报内容
	@ResponseBody
	@RequestMapping(value = "queryPosterByKeyWord", method = RequestMethod.POST)
	public ResponseModel queryPosterByKeyWord(@RequestBody RequestPosterList requestPosterList, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
		List<ResponsePosterHotList> poster = eventService.queryPosterByKeyWord(requestPosterList);
		return ResponseModel.success(poster);
	}

	//清空历史纪录
	@ResponseBody
	@RequestMapping(value = "setHistoryRecord", method = RequestMethod.GET)
	public ResponseModel setHistoryRecord(@RequestParam("userId") String userId) {
    	Integer result = eventService.setHistoryRecord(userId);
		if(result!= null && result >= 1){
			return ResponseModel.success();
		}else{
			return ResponseModel.fail();
		}

	}

	//信贷经理使用了海报，使用次数加1
	@ResponseBody
	@RequestMapping(value = "addUsingCount", method = RequestMethod.POST)
	public ResponseModel addUsingCount(@RequestBody RequestPosterList requestPosterList, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
		Integer result = eventService.addUsingCount(requestPosterList);
		if(result!= null && result == 1){
			return ResponseModel.success();
		}
		return ResponseModel.fail();

	}

	//查询大数据查询需要的星币
	@ResponseBody
	@RequestMapping(value = "queryCreditSearchCost", method = RequestMethod.GET)
	public ResponseModel queryCreditSearchCost() {
		Integer costMoney = eventService.queryCreditSearchCost();
		if(costMoney!= null && costMoney > 0){
			return ResponseModel.success(costMoney);
		}else{
			return ResponseModel.fail("网络异常，请稍后再试！");
		}

	}

	//大数据信用查询
    @ResponseBody
    @RequestMapping(value = "queryCreditRecord", method = RequestMethod.POST)
    public ResponseModel queryCreditRecord(@RequestBody RequestPerson requestPerson, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
        if(StringUtils.isBlank(requestPerson.getUserId()) || StringUtils.isBlank(requestPerson.getIdCard())
                || StringUtils.isBlank(requestPerson.getMobileNumber()) || StringUtils.isBlank(requestPerson.getName())){
            LOG.error("参数填写不完整");
            return ResponseModel.error("请把信息填写完整！");
        }
        ResponseMoney responseMoney = payMoneyService.queryAllMoney(requestPerson.getUserId());
        if(responseMoney != null && responseMoney.getAllMoney() >= eventService.queryCreditSearchCost()){
            try {
                String result = eventService.queryCreditRecord(requestPerson,responseMoney);
                return ResponseModel.success(result);
            } catch (Exception e) {
                LOG.error("用户大数据查询出现异常" + requestPerson.toString());
                e.printStackTrace();
            }
        }else{
        	LOG.info(requestPerson.toString()+"余额不足，查询失败！");
            return ResponseModel.fail("余额不足，查询失败！");
        }
        return ResponseModel.error("网络异常，请稍后再试");
    }

	@ResponseBody
	@RequestMapping(value = "getSignature", method = RequestMethod.GET)
	public ResponseModel getSignature(@RequestParam("param") String param){
		try {
			String encodeStr= MD5Util.md5(param,Constants.QUERY_DATA_HISTORY);
			return ResponseModel.success(encodeStr);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail();
		}
	}

	@RequestMapping(value = "getOpenIdByCode", method = RequestMethod.POST)
	@ResponseBody
	public ResponseModel getOpenIdByCode(String code){
		try {
			JSONObject json= WeixinUtil.getOpenId(code);
			String openId=json.getString("openid");
			if(StringUtils.isEmpty(openId)){
				return ResponseModel.fail("code非法或过期");
			}
			return ResponseModel.success(openId);
		} catch (Exception e) {
			LOG.info(e.getMessage());
			return ResponseModel.fail();
		}
	}

	//查看用户10月份活动获取的优惠券张数
	@ResponseBody
	@RequestMapping(value = "queryCouponByGrabOrder", method = RequestMethod.GET)
	public ResponseModel queryCouponByGrabOrder(@RequestParam("userId") String userId) {
		if(StringUtils.isBlank(userId)){
			return ResponseModel.fail("网络异常,请稍后再试");
		}

		ResponseCouponByGrabOrder responseCouponByGrabOrder  = eventService.queryCouponByGrabOrder(userId);

		return ResponseModel.success(responseCouponByGrabOrder);
	}

	//查看限定打折的城市
	@ResponseBody
	@RequestMapping(value = "queryDiscountCity", method = RequestMethod.GET)
	public ResponseModel queryDiscountCity() {

		List<HashMap<String, Object>> cityNameAndCode  = eventService.queryDiscountCity();
		return ResponseModel.success(cityNameAndCode);
	}
	@ResponseBody
	@RequestMapping(value = "dayConsumption")
	public ResponseModel dayConsumption(String userId){
    	try {
			Map activityInfo = eventService.getActivityInfo(1);
			if (activityInfo == null) {
				return ResponseModel.fail("活动尚未开始，请耐心等待");
			}
			String startTime = VeDate.dateToStr( (java.sql.Timestamp)activityInfo.get("start_time"),"yyyy-MM-dd HH:mm:ss");
			String endTime = VeDate.dateToStr((java.sql.Timestamp)activityInfo.get("end_time"),"yyyy-MM-dd HH:mm:ss");
			long start = VeDate.strToDateLong(startTime).getTime();
			long end = VeDate.strToDateLong(endTime).getTime();
			boolean isLimit = eventService.queryIsLimit(start, end, userId);
			if (isLimit) {
				return ResponseModel.fail("系统检测您的账户抢单异常，已终止参与本次活动");
			}
			String day = VeDate.getStringDateShort();
			int costMoney = eventService.queryDayConsumption(day, userId);
			int differ1 = (70 - costMoney)>=0?70 - costMoney:0;
			int differ2 = (150 - costMoney)>=0?150 - costMoney:0;
			Map<String, Integer> map = new HashMap<>();
			map.put("costMoney", costMoney);
			map.put("differ1", differ1);
			map.put("differ2", differ2);
			return ResponseModel.success(map);
		}catch (Exception e){
    		e.printStackTrace();
    		return ResponseModel.fail("网络异常");
		}
	}

	/**
	 * yax
	 * 消费满减 领取接口
	 * @param userId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "consumptiveConsumption")
    public ResponseModel consumptiveConsumption(String userId,int type){
    	try {
			Map activityInfo = eventService.getActivityInfo(1);
			if (activityInfo == null) {
				return ResponseModel.fail("活动尚未开始，请耐心等待");
			}
			String startTime = VeDate.dateToStr( (java.sql.Timestamp)activityInfo.get("start_time"),"yyyy-MM-dd HH:mm:ss");
			String endTime = VeDate.dateToStr((java.sql.Timestamp)activityInfo.get("end_time"),"yyyy-MM-dd HH:mm:ss");
			long start = VeDate.strToDateLong(startTime).getTime();
			long end = VeDate.strToDateLong(endTime).getTime();
			boolean isLimit = eventService.queryIsLimit(start, end, userId);
			if (isLimit) {
				return ResponseModel.fail("系统检测您的账户抢单异常，已终止参与本次活动");
			}
			String name = userService.queryNameByUserId(userId);
			String role = name.subSequence(0, 1) + "经理";
			String day = VeDate.getStringDateShort();
			int costMoney = eventService.queryDayConsumption(day, userId);
			RequestCouponByUserId requestCouponByUserId = new RequestCouponByUserId();
			String couponCode = "consumptiveconsumption10" + VeDate.getStringDateShort().replace("-", "");
			String couponId = "" + idWorker.nextId();
			requestCouponByUserId.setCouponId(couponId);
			requestCouponByUserId.setGetType(1);
			if (type == 0) { //判断当天是否领过此种优惠券
				requestCouponByUserId.setUserId(userId);
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					return ResponseModel.fail("您已获得今日奖励");
				}
				if (costMoney > 70) {
					//插入领取记录
					couponService.addCouponForUserAutomaticly(requestCouponByUserId);
					//插入用户优惠券表
					couponService.insertUserCoupon(couponId,userId, 10, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "消费满减", 2, 3, 0);
					payMoneyService.addMessage(userId, 1, "奖励通知", role+"，消费满减活动奖励的10元无门槛抢单券已发放到账户，请查收！");
					PushResult pushResult = JiguangPush.push(userId,"恭喜您获得10元无门槛抢单优惠券一张，请立即使用!");
					if (pushResult != null && pushResult.isResultOK()) {
						LOG.info("针对" + userId + "的优惠券通知推送成功！");
					} else {
						LOG.info("针对" + userId + "的优惠券通知推送失败！");
					}
					return ResponseModel.success();
				} else {
					return ResponseModel.fail("您尚未获得领券资格，先去进行消费吧");
				}
			}
			couponCode = "consumptiveconsumption50" + VeDate.getStringDateShort().replace("-", "");
			if (type == 1) { //判断当天是否领过此种优惠券
				requestCouponByUserId.setUserId(userId);
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					return ResponseModel.fail("您已获得今日奖励");
				}
				if (costMoney > 150) {
					//插入领取记录
					couponService.addCouponForUserAutomaticly(requestCouponByUserId);
					//插入用户优惠券表
					couponService.insertUserCoupon(couponId,userId, 50, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "消费满减", 2, 3, 0);
					payMoneyService.addMessage(userId, 1, "奖励通知", role+"，消费满减活动奖励的50星币无门槛抢单券已发放到账户，请查收！");
					PushResult pushResult = JiguangPush.push(userId,"恭喜您获得50星币无门槛抢单券一张，请立即使用");
					if (pushResult != null && pushResult.isResultOK()) {
						LOG.info("针对" + userId + "的优惠券通知推送成功！");
					} else {
						LOG.info("针对" + userId + "的优惠券通知推送失败！");
					}
					return ResponseModel.success();
				} else {
					return ResponseModel.fail("您尚未获得领券资格，先去进行消费吧");
				}
			}
			return ResponseModel.fail("未知异常");
		}catch (Exception e){
    		e.printStackTrace();
    		return ResponseModel.fail("网络异常");
		}
	}

	/**
	 * yax
	 * 消费满减 判断优惠券是否可用领取
	 * @param userId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "isReceiveCoupon")
    public ResponseModel isReceiveCoupon(String userId,int type){
    	try {
			String day = VeDate.getStringDateShort();
			int costMoney = eventService.queryDayConsumption(day, userId);
			RequestCouponByUserId requestCouponByUserId = new RequestCouponByUserId();
			String couponCode = "consumptiveconsumption10" + VeDate.getStringDateShort().replace("-", "");
			if (type == 0) { //判断当天是否领过此种优惠券
				requestCouponByUserId.setUserId(userId);
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					return ResponseModel.fail("您已获得今日奖励");
				}
				if (costMoney > 70) {
					return ResponseModel.success();
				} else {
					return ResponseModel.fail("您尚未获得领券资格，先去进行消费吧");
				}
			}else if (type == 1) { //判断当天是否领过此种优惠券
				couponCode = "consumptiveconsumption50" + VeDate.getStringDateShort().replace("-", "");
				requestCouponByUserId.setUserId(userId);
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					return ResponseModel.fail("您已获得今日奖励");
				}
				if (costMoney > 150) {
					return ResponseModel.success();
				} else {
					return ResponseModel.fail("您尚未获得领券资格，先去进行消费吧");
				}
			}else{
				return ResponseModel.fail("参数不正确");
			}
		}catch (Exception e) {
    		e.printStackTrace();
			return ResponseModel.fail("网络异常");
		}
	}
	@ResponseBody
	@RequestMapping(value = "snatchInfo")
    public ResponseModel snatchInfo(String userId) {
		try {
			Map activityInfo = eventService.getActivityInfo(2);
			if (activityInfo == null) {
				return ResponseModel.fail("活动尚未开始，请耐心等待");
			}
			String startTime = VeDate.dateToStr( (java.sql.Timestamp)activityInfo.get("start_time"),"yyyy-MM-dd HH:mm:ss");
			String endTime = VeDate.dateToStr((java.sql.Timestamp)activityInfo.get("end_time"),"yyyy-MM-dd HH:mm:ss");
			long start = VeDate.strToDateLong(startTime).getTime();
			long end = VeDate.strToDateLong(endTime).getTime();
			boolean isLimit = eventService.queryIsLimit(start, end, userId);
			if (isLimit) {
				return ResponseModel.fail("系统检测您的账户抢单异常，已终止参与本次活动");
			}
			Map<String,Object> map=new HashMap();
			String date = VeDate.getStringDateShort();
			String sql = "select * from snatch_a_single_invoice_record where user_id=? and create_date=?";
			SnatchRecord record = baseService.queryBySql(sql, SnatchRecord.class, userId, date);
			if(record==null){
				map.put("total",0);
				map.put("remain",2);
				map.put("info","10星币减5");
				map.put("available5",0);
				map.put("available10",0);
				map.put("available50",0);
			}else{
				int count=record.getCount();
				map.put("total",count);
				map.put("available5",record.getAvailable5());
				map.put("available10",record.getAvailable10());
				map.put("available50",record.getAvailable50());
				int remain=count%5;
				if(remain>=0&&remain<2){
					map.put("remain",2-remain);
					map.put("info","10星币减5");
				}
				if(remain>=2&&remain<3){
					map.put("remain",3-remain);
					map.put("info","20星币减10");
				}
				if(remain>=3&&remain<5){
					map.put("remain",5-remain);
					map.put("info","50星币无门槛");
				}
			}
			return ResponseModel.success(map);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail("网络异常");
		}
	}

	/**
	 * yax
	 * 抢单送免单 领取接口
	 * @param userId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "receiveSnatchCoupon")
	public ResponseModel receiveSnatchCoupon(String userId,int type){
    	try {
			Map activityInfo = eventService.getActivityInfo(2);
			if (activityInfo == null) {
				return ResponseModel.fail("活动尚未开始，请耐心等待");
			}
			String startTime = VeDate.dateToStr( (java.sql.Timestamp)activityInfo.get("start_time"),"yyyy-MM-dd HH:mm:ss");
			String endTime = VeDate.dateToStr((java.sql.Timestamp)activityInfo.get("end_time"),"yyyy-MM-dd HH:mm:ss");
			long start = VeDate.strToDateLong(startTime).getTime();
			long end = VeDate.strToDateLong(endTime).getTime();
			boolean isLimit = eventService.queryIsLimit(start, end, userId);
			if (isLimit) {
				return ResponseModel.fail("系统检测您的账户抢单异常，已终止参与本次活动");
			}
			Map<String, Object> map = new HashMap();
			String date = VeDate.getStringDateShort();
			String sql = "select * from snatch_a_single_invoice_record where user_id=? and create_date=?";
			SnatchRecord record = baseService.queryBySql(sql, SnatchRecord.class, userId, date);
			if(record==null){
				return ResponseModel.fail("您尚未获得领券资格，快去抢单后领取吧！");
			}
			String name = userService.queryNameByUserId(userId);
			String role = name.subSequence(0, 1) + "经理";
			String couponId = "" + idWorker.nextId();
			//10元减5
			if(type==0){
				int available5=record.getAvailable5();
				if(available5>0){
					eventService.updateSnatchRecord(userId,date,0,-1,0,0);
					couponService.insertUserCoupon(couponId,userId, 5, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "抢单送免单", 2, 2, 10);
					payMoneyService.addMessage(userId, 1, "奖励通知", role+"，抢单送免单活动奖励的满10星币减5抢单券已发放到账户，请查收！");
					PushResult pushResult = JiguangPush.push(userId,"恭喜您获得满10星币减5抢单券一张，请立即使用");
					if (pushResult != null && pushResult.isResultOK()) {
						LOG.info("针对" + userId + "的优惠券通知推送成功！");
					} else {
						LOG.info("针对" + userId + "的优惠券通知推送失败！");
					}
					return ResponseModel.success("领取成功");
				}else{
					return ResponseModel.fail("您尚未获得领券资格，快去抢单后领取吧！");
				}
				//20元减10
			}else if(type==1){
				int available10=record.getAvailable10();
				if(available10>0){
					eventService.updateSnatchRecord(userId,date,0,0,-1,0);
					couponService.insertUserCoupon(couponId,userId, 10, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "抢单送免单", 2, 2, 20);
					payMoneyService.addMessage(userId, 1, "奖励通知", role+"，抢单送免单活动奖励的满20星币减10抢单券已发放到账户，请查收！");
					PushResult pushResult = JiguangPush.push(userId,"恭喜您获得满20星币减10抢单券一张，请立即使用");
					if (pushResult != null && pushResult.isResultOK()) {
						LOG.info("针对" + userId + "的优惠券通知推送成功！");
					} else {
						LOG.info("针对" + userId + "的优惠券通知推送失败！");
					}
					return ResponseModel.success("领取成功");
				}else{
					return ResponseModel.fail("您尚未获得领券资格，快去抢单后领取吧！");
				}
				//50元无门槛
			}else{
				int available50=record.getAvailable50();
				if(available50>0){
					eventService.updateSnatchRecord(userId,date,0,0,0,-1);
					couponService.insertUserCoupon(couponId,userId, 50, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "抢单送免单", 2, 3, 0);
					payMoneyService.addMessage(userId, 1, "奖励通知", role+"，抢单送免单活动奖励的50无门槛抢单券已发放到账户，请查收！");
					PushResult pushResult = JiguangPush.push(userId,"恭喜您获得满50无门槛抢单券一张，请立即使用");
					if (pushResult != null && pushResult.isResultOK()) {
						LOG.info("针对" + userId + "的优惠券通知推送成功！");
					} else {
						LOG.info("针对" + userId + "的优惠券通知推送失败！");
					}
					return ResponseModel.success("领取成功");
				}else{
					return ResponseModel.fail("您尚未获得领券资格，快去抢单后领取吧！");
				}
			}

		}catch (Exception e) {
    		e.printStackTrace();
			return ResponseModel.fail("网络异常");
		}
	}

	/**
	 * yax
	 * 充值满减 领取优惠券接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "receiveFullValueReductionCoupon")
	public ResponseModel receiveFullValueReductionCoupon(String userId,int type){
		try {
			Map activityInfo = eventService.getActivityInfo(3);
			if (activityInfo == null) {
				return ResponseModel.fail("活动尚未开始，请耐心等待");
			}
			/*String startTime = VeDate.dateToStr( (java.sql.Timestamp)activityInfo.get("start_time"),"yyyy-MM-dd HH:mm:ss");
			String endTime = VeDate.dateToStr((java.sql.Timestamp)activityInfo.get("end_time"),"yyyy-MM-dd HH:mm:ss");
			long start = VeDate.strToDateLong(startTime).getTime();
			long end = VeDate.strToDateLong(endTime).getTime();
			boolean isLimit = eventService.queryIsLimit(start, end, userId);
			if (isLimit) {
				return ResponseModel.fail("系统检测您的账户抢单异常，已终止参与本次活动");
			}*/
			String day = VeDate.getStringDateShort();
			RequestCouponByUserId requestCouponByUserId = new RequestCouponByUserId();
			requestCouponByUserId.setUserId(userId);
			String couponId = "" + idWorker.nextId();
			requestCouponByUserId.setCouponId(couponId);
            requestCouponByUserId.setGetType(1);
			String couponCode = "fullvaluereduction100" + VeDate.getStringDateShort().replace("-", "");
			String name = userService.queryNameByUserId(userId);
			String role = name.subSequence(0, 1) + "经理";
			if (type == 0) { //判断当天是否领过此种优惠券
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					return ResponseModel.fail("您已获得今日奖励");
				}
				//插入领取记录
				couponService.addCouponForUserAutomaticly(requestCouponByUserId);
				//插入用户优惠券表
				couponService.insertUserCoupon(couponId,userId, 100, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("1"), "充值满减", 1, 2, 500);
				payMoneyService.addMessage(userId, 1, "奖励通知", role+"，充值满减活动奖励的满500减100充值抵扣券已发放到账户，请查收！");
				PushResult pushResult = JiguangPush.push(userId,"恭喜您获得满500减100充值抵扣券一张，请立即充值");
				if (pushResult != null && pushResult.isResultOK()) {
					LOG.info("针对" + userId + "的优惠券通知推送成功！");
				} else {
					LOG.info("针对" + userId + "的优惠券通知推送失败！");
				}
			} else if (type == 1) {
				couponCode = "fullvaluereduction250" + VeDate.getStringDateShort().replace("-", "");
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					return ResponseModel.fail("您已获得今日奖励");
				}
				//插入领取记录
				couponService.addCouponForUserAutomaticly(requestCouponByUserId);
				//插入用户优惠券表
				couponService.insertUserCoupon(couponId,userId, 250, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("1"), "充值满减", 1, 2, 1000);
				payMoneyService.addMessage(userId, 1, "奖励通知", role+"，充值满减活动奖励的满1000减250充值抵扣券已发放到账户，请查收！");
				PushResult pushResult = JiguangPush.push(userId,"恭喜您获得满1000减250充值抵扣券一张，请立即充值");
				if (pushResult != null && pushResult.isResultOK()) {
					LOG.info("针对" + userId + "的优惠券通知推送成功！");
				} else {
					LOG.info("针对" + userId + "的优惠券通知推送失败！");
				}
			} else if (type == 2) {
				couponCode = "fullvaluereduction550" + VeDate.getStringDateShort().replace("-", "");

				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					return ResponseModel.fail("您已获得今日奖励");
				}
				//插入领取记录
				couponService.addCouponForUserAutomaticly(requestCouponByUserId);
				//插入用户优惠券表
				couponService.insertUserCoupon(couponId,userId, 550, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("1"), "充值满减", 1, 2, 2000);
				payMoneyService.addMessage(userId, 1, "奖励通知", role+"，充值满减活动奖励的满2000减550充值抵扣券已发放到账户，请查收！");
				PushResult pushResult = JiguangPush.push(userId,"恭喜您获得满2000减550充值抵扣券一张，请立即充值");
				if (pushResult != null && pushResult.isResultOK()) {
					LOG.info("针对" + userId + "的优惠券通知推送成功！");
				} else {
					LOG.info("针对" + userId + "的优惠券通知推送失败！");
				}
			} else if (type == 3) {
				couponCode = "fullvaluereduction1500" + VeDate.getStringDateShort().replace("-", "");

				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					return ResponseModel.fail("您已获得今日奖励");
				}
				//插入领取记录
				couponService.addCouponForUserAutomaticly(requestCouponByUserId);
				//插入用户优惠券表
				couponService.insertUserCoupon(couponId,userId, 1500, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("1"), "充值满减", 1, 2, 5000);
				payMoneyService.addMessage(userId, 1, "奖励通知", role+"，充值满减活动奖励的满5000减1550充值抵扣券已发放到账户，请查收！");
				PushResult pushResult = JiguangPush.push(userId,"恭喜您获得满5000减1550充值抵扣券一张，请立即充值");
				if (pushResult != null && pushResult.isResultOK()) {
					LOG.info("针对" + userId + "的优惠券通知推送成功！");
				} else {
					LOG.info("针对" + userId + "的优惠券通知推送失败！");
				}
			} else if (type == 4) {
				couponCode = "fullvaluereduction3500" + VeDate.getStringDateShort().replace("-", "");

				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					return ResponseModel.fail("您已获得今日奖励");
				}
				//插入领取记录
				couponService.addCouponForUserAutomaticly(requestCouponByUserId);
				//插入用户优惠券表
				couponService.insertUserCoupon(couponId,userId, 3500, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("1"), "充值满减", 1, 2, 10000);
				payMoneyService.addMessage(userId, 1, "奖励通知", role+"，充值满减活动奖励的满10000减3500充值抵扣券已发放到账户，请查收！");
				PushResult pushResult = JiguangPush.push(userId,"恭喜您获得满10000减3500充值抵扣券一张，请立即充值");
				if (pushResult != null && pushResult.isResultOK()) {
					LOG.info("针对" + userId + "的优惠券通知推送成功！");
				} else {
					LOG.info("针对" + userId + "的优惠券通知推送失败！");
				}
			}
			return ResponseModel.success();
		}catch(Exception e){
			e.printStackTrace();
			return ResponseModel.fail("网络异常");
		}
	}

	/**
	 * 充值满减 优惠券是否已领取过
	 * @param userId
	 * @param type
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "fullValueReductionCouponInfo")
	public ResponseModel fullValueReductionCouponInfo(String userId){
		try {
			Map activityInfo = eventService.getActivityInfo(3);
			if (activityInfo == null) {
				return ResponseModel.fail("活动尚未开始，请耐心等待");
			}
			/*String startTime = VeDate.dateToStr( (java.sql.Timestamp)activityInfo.get("start_time"),"yyyy-MM-dd HH:mm:ss");
			String endTime = VeDate.dateToStr((java.sql.Timestamp)activityInfo.get("end_time"),"yyyy-MM-dd HH:mm:ss");
			long start = VeDate.strToDateLong(startTime).getTime();
			long end = VeDate.strToDateLong(endTime).getTime();
			boolean isLimit = eventService.queryIsLimit(start, end, userId);
			if (isLimit) {
				return ResponseModel.fail("系统检测您的账户抢单异常，已终止参与本次活动");
			}*/
			Map<String,Integer> map=new HashMap<>();
			String day = VeDate.getStringDateShort();
			RequestCouponByUserId requestCouponByUserId = new RequestCouponByUserId();
			requestCouponByUserId.setUserId(userId);
			String couponCode = "fullvaluereduction100" + VeDate.getStringDateShort().replace("-", "");

				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					map.put("fullvaluereduction100",1);
				}else{
					map.put("fullvaluereduction100",0);
				}

				couponCode = "fullvaluereduction250" + VeDate.getStringDateShort().replace("-", "");
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					map.put("fullvaluereduction250",1);
				}else{
					map.put("fullvaluereduction250",0);
				}

				couponCode = "fullvaluereduction550" + VeDate.getStringDateShort().replace("-", "");
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					map.put("fullvaluereduction550",1);
				}else{
					map.put("fullvaluereduction550",0);
				}

				couponCode = "fullvaluereduction1500" + VeDate.getStringDateShort().replace("-", "");
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					map.put("fullvaluereduction1500",1);
				}else{
					map.put("fullvaluereduction1500",0);
				}

				couponCode = "fullvaluereduction3500" + VeDate.getStringDateShort().replace("-", "");
				requestCouponByUserId.setCouponCode(couponCode);
				couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
				if (couponCode != null) {
					map.put("fullvaluereduction3500",1);
				}else{
					map.put("fullvaluereduction3500",0);
				}
				return ResponseModel.success(map);
		}catch (Exception e){
			e.printStackTrace();
			return ResponseModel.fail("网络异常");
		}
	}
	/**
	 * h5获取用户认证状态
	 * @param userId
	 * @return
	 */
	@PostMapping("getAuthenticationStateByUserId")
	@ResponseBody
	public  ResponseModel getAuthenticationStateByUserId(String userId){
		try {
			Integer approveResult = userService.queryApproveByUserIdNew(userId);
			if (approveResult == 1) {
				return ResponseModel.success("认证成功", null);
			}else if (approveResult == 2) {
				return ResponseModel.success("认证中",null);
			} else if (approveResult == 3) {
				return ResponseModel.success("认证失败", null);
			} else if (approveResult == 4) {
				return ResponseModel.success("未认证", null);
			}else {
				return ResponseModel.success("未知错误", null);
			}
		}catch(Exception e){
			return ResponseModel.fail("查询失败");
		}
	}

	/**
	 * 判断用户是否在活动期间注册
	 * @param userId
	 * @return
	 */
	@PostMapping("isRegistrationDuringTheEvent")
	@ResponseBody
	public ResponseModel isRegistrationDuringTheEvent(String userId){
		Map activityInfo = eventService.getActivityInfoById(4);
		String startTime = VeDate.dateToStr((java.sql.Timestamp)activityInfo.get("start_time"),"yyyy-MM-dd HH:mm:ss");
		String endTime = VeDate.dateToStr((java.sql.Timestamp)activityInfo.get("end_time"),"yyyy-MM-dd HH:mm:ss");
		String nowDay=VeDate.getStringDate();
		if(nowDay.compareTo(endTime)>0){
			return ResponseModel.fail("本次活动已结束，请期待下次活动");
		}
		if(nowDay.compareTo(startTime)<0) {
			return ResponseModel.fail("活动尚未开始，请耐心等待");
		}
        int result=eventService.isRegistrationDuringTheEvent(userId,startTime,endTime);
        return ResponseModel.success(result);
	}

	/**
	 * 新人领取充值回馈劵
	 * @param userId
	 * @param type
	 * @return
	 */
	@PostMapping("receiveNewRechargeFeedbackCoupon")
	@ResponseBody
    public ResponseModel receiveNewRechargeFeedbackCoupon(String userId,int type,int isReceive){
		try {
			Map activityInfo = eventService.getActivityInfoById(4);
			String startTime = VeDate.dateToStr((java.sql.Timestamp) activityInfo.get("start_time"), "yyyy-MM-dd HH:mm:ss");
			String endTime = VeDate.dateToStr((java.sql.Timestamp) activityInfo.get("end_time"), "yyyy-MM-dd HH:mm:ss");
			String nowDay = VeDate.getStringDate();
			if (nowDay.compareTo(endTime) > 0) {
				return ResponseModel.fail("本次活动已结束，请期待下次活动");
			}
			if (nowDay.compareTo(startTime) < 0) {
				return ResponseModel.fail("活动尚未开始，请耐心等待");
			}
			int result=eventService.isRegistrationDuringTheEvent(userId,startTime,endTime);
			if(result<=0){
				return ResponseModel.fail("您不是新注册认证用户，请领取左侧充值回馈礼包");
			}
			//String name = userService.queryNameByUserId(userId);
			//String role = name.subSequence(0, 1) + "经理";
			RequestCouponByUserId requestCouponByUserId = new RequestCouponByUserId();
			requestCouponByUserId.setUserId(userId);
			String couponId = "" + idWorker.nextId();
			requestCouponByUserId.setCouponId("CM"+couponId);
			requestCouponByUserId.setGetType(1);
			String couponCode = null;
			Integer couponAmount=null;
			Double couponFullreduction=null;
			if (type == 0) {
				couponAmount=100;
				couponFullreduction=500d;
			}else if(type==1){
				couponAmount=250;
				couponFullreduction=1000d;
			}else if(type==2){
				couponAmount=550;
				couponFullreduction=2000d;
			}else if(type==3){
				couponAmount=1500;
				couponFullreduction=5000d;
			}else if(type==4){
				couponAmount=3500;
				couponFullreduction=10000d;
			}
			couponCode = "rechargeFeedbackNew"+couponAmount;
			requestCouponByUserId.setCouponCode(couponCode);
			couponCode = couponService.queryIsGetCouponBefore(requestCouponByUserId);
			if (couponCode != null) {
				return ResponseModel.fail("您已领取过此奖励");
			}
			if(isReceive==0) {
				//插入领取记录
				couponService.addCouponForUserAutomaticly(requestCouponByUserId);
				//插入用户优惠券表
				couponService.insertUserCoupon(couponId, userId, couponAmount, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 1, 2, couponFullreduction);
				payMoneyService.addMessage(userId, 1, "奖励通知", "经理您好，充值回馈活动奖励的满" + couponFullreduction.intValue() + "减" + couponAmount + "充值抵扣券已发放到账户，请查收！");
				PushResult pushResult = JiguangPush.push(userId, "您已成功领取" + couponAmount + "元的充值抵扣券一张，请到我的优惠券页面查看！");
				if (pushResult != null && pushResult.isResultOK()) {
					LOG.info("针对" + userId + "的优惠券通知推送成功！");
				} else {
					LOG.info("针对" + userId + "的优惠券通知推送失败！");
				}
			}
			return ResponseModel.success("充值抵扣券领取成功");
		}catch (Exception e){
			e.printStackTrace();
			return ResponseModel.fail("网络异常");
		}
	}
	/**
	 * 老用户领取充值回馈劵
	 * @param userId
	 * @param type
	 * @return
	 */
	@PostMapping("receiveOldRechargeFeedbackCoupon")
	@ResponseBody
	public ResponseModel receiveOldRechargeFeedbackCoupon(String userId,int type,int isReceive){
		try {
			Map activityInfo = eventService.getActivityInfoById(4);
			String startTime = VeDate.dateToStr((java.sql.Timestamp) activityInfo.get("start_time"), "yyyy-MM-dd HH:mm:ss");
			String endTime = VeDate.dateToStr((java.sql.Timestamp) activityInfo.get("end_time"), "yyyy-MM-dd HH:mm:ss");
			String nowDay = VeDate.getStringDate();
			if (nowDay.compareTo(endTime) > 0) {
				return ResponseModel.fail("本次活动已结束，请期待下次活动");
			}
			if (nowDay.compareTo(startTime) < 0) {
				return ResponseModel.fail("活动尚未开始，请耐心等待");
			}

			//String name = userService.queryNameByUserId(userId);
			//String role = name.subSequence(0, 1) + "经理";
			RequestCouponByUserId requestCouponByUserId = new RequestCouponByUserId();
			requestCouponByUserId.setUserId(userId);
			String couponId = "" + idWorker.nextId();
			requestCouponByUserId.setCouponId("CM"+couponId);
			requestCouponByUserId.setGetType(1);
			String couponCode = null;
			Integer couponAmount=null;
			Double couponFullreduction=null;
			if (type == 0) {
				couponAmount=80;
				couponFullreduction=500d;
			}else if(type==1){
				couponAmount=200;
				couponFullreduction=1000d;
			}else if(type==2){
				couponAmount=450;
				couponFullreduction=2000d;
			}else if(type==3){
				couponAmount=1400;
				couponFullreduction=5000d;
			}else if(type==4){
				couponAmount=3000;
				couponFullreduction=10000d;
			}
			couponCode = "rechargeFeedbackOld"+couponAmount+ VeDate.getStringDateShort().replace("-", "");
			requestCouponByUserId.setCouponCode(couponCode);
            int count=couponService.queryIsGetCouponBeforeCount(requestCouponByUserId);
            if(count>=5){
				return ResponseModel.fail("今日该档位优惠券已领取，您可获取其他档位优惠券");
			}
            if(isReceive==0) {
				//插入领取记录
				couponService.addCouponForUserAutomaticly(requestCouponByUserId);
				//插入用户优惠券表
				couponService.insertUserCoupon(couponId, userId, couponAmount, 0, DateUtil.getTime(), DateUtil.getAfterDayDate("2"), "充值回馈", 1, 2, couponFullreduction);
				payMoneyService.addMessage(userId, 1, "奖励通知", "经理您好，充值回馈活动奖励的满" + couponFullreduction.intValue() + "减" + couponAmount + "充值抵扣券已发放到账户，请查收！");
				PushResult pushResult = JiguangPush.push(userId, "您已成功领取" + couponAmount + "元的充值抵扣券一张，请到我的优惠券页面查看！");
				if (pushResult != null && pushResult.isResultOK()) {
					LOG.info("针对" + userId + "的优惠券通知推送成功！");
				} else {
					LOG.info("针对" + userId + "的优惠券通知推送失败！");
				}
			}
			return ResponseModel.success("充值抵扣券领取成功");
		}catch (Exception e){
			e.printStackTrace();
			return ResponseModel.fail("网络异常");
		}
	}

	/**
	 * 充值反馈活动 按钮 pv uv统计
	 * @param buttonType
	 * @return
	 */
	@RequestMapping("buttonClick")
	@ResponseBody
   public ResponseModel buttonClick(String buttonType){
		try{
			String ip=IpUtil.getIpAddr(request);
			eventService.addPvUv(ip,buttonType);
			return ResponseModel.success();
		}catch (Exception e){
			e.printStackTrace();
			return ResponseModel.fail();
		}
   }

	/**获取弹屏信息 (2.3.0)
	 * @param versionCode
	 * @param osType
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "popupScreenOnLogin",method = RequestMethod.POST)
	@ResponseBody
   public ResponseModel popupScreenOnLogin(@RequestParam String versionCode,@RequestParam String osType,@RequestParam String userId){
		try{
			return ResponseModel.success(eventService.getPopupScreenInfo(versionCode,osType,userId));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail();
		}
   }

	/**
	 * 统计pvuv
	 * @param eventId
	 * @param type
	 * @return
	 */
   @RequestMapping(value = "statisticsPvUv",method = RequestMethod.POST)
   @ResponseBody
   public ResponseModel statisticsPvUv(String eventId,String type){
		try {
			String ip = IpUtil.getIpAddr(request);
			eventService.addStatisticsPvUv(eventId, type, ip);
			return ResponseModel.success();
		}catch (Exception e){
			e.printStackTrace();
			return ResponseModel.fail();
		}
   }
}
