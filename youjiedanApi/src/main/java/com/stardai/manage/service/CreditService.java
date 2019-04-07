/**
 * 
 */
package com.stardai.manage.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.utils.VeDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.mapper.CouponMapper;
import com.stardai.manage.mapper.CreditMapper;
import com.stardai.manage.request.RequestAddCreditByNewEdition;
import com.stardai.manage.request.RequestAllIncome;
import com.stardai.manage.request.RequestCouponByUserId;
import com.stardai.manage.request.RequestCouponForNewUser;
import com.stardai.manage.request.RequestCreditDetail;
import com.stardai.manage.response.ResponseGetCoupon;
import com.stardai.manage.utils.DateUtil;
import com.stardai.manage.utils.IdWorker;
import com.stardai.manage.utils.RedisCacheManager;

/**
 * @author Tina 2018年5月30日
 */

@Service
@SuppressWarnings("all")
public class CreditService {
	@Autowired
	private RedisCacheManager redisCacheManager;

	@Autowired
	private CreditMapper creditMapper;

	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private PayMoneyService payMoneyService;

	protected static final Logger LOG = LoggerFactory.getLogger(CreditService.class);
	private static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private IdWorker idWorker = new IdWorker(0, 8);

	private final static SimpleDateFormat sdfTimeHour = new SimpleDateFormat("yyyy-MM-dd HH:00:00");

	/**
	 * @Author:Tina
	 * @Description:根据此人userId查询此人当天有没有签到
	 * @Date:2018年5月31日下午3:00:21
	 * @Param:
	 * @Return:Integer
	 */
	public Integer queryIsSigninByUserId(String userId, String currentTime) {

		return creditMapper.queryIsSigninByUserId(userId, currentTime);
	}

	/**
	 * @Author:Tina
	 * @Description:获取连续签到天数对应的积分值
	 * @Date:2018年5月31日下午3:40:36
	 * @Param:
	 * @Return:Integer
	 */
	public Integer getValueBySigninCount(Integer signinCount) {

		return creditMapper.getValueBySigninCount(signinCount);
	}

	/**
	 * @Author:Tina
	 * @Description:插入当天的签到记录
	 * @Date:2018年5月31日下午3:40:40
	 * @Param:
	 * @Return:void
	 */
	public Integer addNewSigninRecord(String userId, Integer signinCredit, Integer signinCount) {
		return creditMapper.addNewSigninRecord(userId, signinCredit, signinCount);

	}

	/**
	 * @Author:Tina
	 * @Description:返回该用户的当前剩余积分和当天获取的积分
	 * @Date:2018年5月31日下午4:39:01
	 * @Param:
	 * @Return:HashMap<String,Object>
	 */
	public HashMap<String, Object> queryTotalAndTodayCredit(String userId) {
		HashMap<String, Object> totalAndtoday = new HashMap<String, Object>();
		// 获取此用户当前剩余的积分
		Integer totalCredit = creditMapper.queryTotalCredit(userId);
		if (totalCredit == null) {
			totalCredit = 0;
		}
		// 获取此用户当天获取的积分
		Integer todayCredit = creditMapper.queryTodayCredit(userId);
		if (todayCredit == null) {
			todayCredit = 0;
		}
		totalAndtoday.put("totalCredit", totalCredit);
		totalAndtoday.put("todayCredit", todayCredit);
		return totalAndtoday;
	}

	/**
	 * @Author:Tina
	 * @Description:添加一条积分明细
	 * @Date:2018年6月1日上午9:30:08
	 * @Param:
	 * @Return:void
	 */
	public void addCreditDetail(RequestCreditDetail requestCreditDetail) {
		creditMapper.addCreditDetail(requestCreditDetail);

	}

	/**
	 * @Author:Tina
	 * @Description:更新用户积分
	 * @Date:2018年6月1日上午9:45:50
	 * @Param:
	 * @Return:void
	 */
	public void updateCreditWallet(String userId, Integer creditValue) {
		creditMapper.updateCreditWallet(userId, creditValue);

	}

	/**
	 * @Author:Tina
	 * @Description:查询积分汇总表里有没有该用户的记录
	 * @Date:2018年6月1日上午11:10:30
	 * @Param:
	 * @Return:Integer
	 */
	public Integer queryIsEverGetCredit(String userId) {

		return creditMapper.queryIsEverGetCredit(userId);
	}

	/**
	 * @Author:Tina
	 * @Description:在积分汇总表插入一条新的记录
	 * @Date:2018年6月1日上午11:15:56
	 * @Param:
	 * @Return:void
	 */
	public void addCreditWallet(String userId, Integer creditValue) {
		creditMapper.addCreditWallet(userId, creditValue);

	}

	/**
	 * @Author:Tina
	 * @Description:获取用户积分明细列表
	 * @Date:2018年6月1日下午4:44:58
	 * @Param:
	 * @Return:List<RequestCreditDetail>
	 */
	public List<RequestCreditDetail> getCreditDetails(RequestAllIncome requestAllIncome) {
		int pageSize = requestAllIncome.getPageSize();
		int page = requestAllIncome.getPage() * pageSize;
		requestAllIncome.setPage(page);
		return creditMapper.getCreditDetails(requestAllIncome);
	}

	/**
	 * @Author:Tina
	 * @Description: 获取积分兑换优惠券列表
	 * @Date:2018年6月4日上午9:43:59
	 * @Param:
	 * @Return:List<ResponseGetCoupon>
	 */
	public List<ResponseGetCoupon> getCreditForExchangeList(RequestCouponByUserId requestCouponByUserId) {
		String nowDate = DateUtil.getDay(); // 获取当天日期
		String nowTime = DateUtil.getTime();// 获取当前时间
		String userId = requestCouponByUserId.getUserId();

		requestCouponByUserId.setNowTime(nowTime);
		// 进行中的优惠券列表
		List<ResponseGetCoupon> processingCouponList = creditMapper.getProcessingCouponList(requestCouponByUserId);
		// 未开始的优惠券列表
		List<ResponseGetCoupon> berforeTimeCouponList = creditMapper.getBerforeTimeCouponList(requestCouponByUserId);
		// 已结束的优惠券列表
		List<ResponseGetCoupon> endCouponList = creditMapper.getEndCouponList(requestCouponByUserId);

		// 对进行中的优惠券进行遍历修改
		for (ResponseGetCoupon rgc : processingCouponList) {
			String couponCode = rgc.getCouponCode();
			if (rgc.getCouponInitialCount() > 0) {
				double leftPercent = (rgc.getCouponInitialCount() - rgc.getCouponCount()) * 1.0
						/ rgc.getCouponInitialCount() * 100;
				rgc.setLeftPercent((int) leftPercent);
			}
			rgc.setCouponGetStatus(2);//进行中状态
			//设置优惠券类型
			rgc = this.setCouponTypeAndForm(rgc);


		}
		String getBeginTime = "";
		// 对未开始的优惠券进行遍历修改
		for (ResponseGetCoupon rgc : berforeTimeCouponList) {
			getBeginTime = rgc.getGetBeginTime();// 获取领券的开始时间
			try {
				Calendar beginTimeCal=Calendar.getInstance();
				Calendar todayCal=Calendar.getInstance();
				beginTimeCal.setTime(sdfTime.parse(getBeginTime));
				todayCal.setTime(new Date());
				//如果领券开始时间是当天
				if(beginTimeCal.get(Calendar.DAY_OF_MONTH) == todayCal.get(Calendar.DAY_OF_MONTH)){
					rgc.setBeginTime(beginTimeCal.get(Calendar.HOUR_OF_DAY) + "点开始");
				}else{
					rgc.setBeginTime(beginTimeCal.get(Calendar.DAY_OF_MONTH) + "日"
							+ beginTimeCal.get(Calendar.HOUR_OF_DAY)+ "点开始");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			rgc.setCouponGetStatus(0);//未开始状态
			//设置优惠券类型
			rgc = this.setCouponTypeAndForm(rgc);
		}
		// 对已结束的优惠券列表遍历修改
		for (ResponseGetCoupon rgc : endCouponList) {
			String couponCode = rgc.getCouponCode();
			rgc.setCouponGetStatus(3); //已结束状态
			//设置优惠券类型
			rgc = this.setCouponTypeAndForm(rgc);
		}
		processingCouponList.addAll(berforeTimeCouponList);
		processingCouponList.addAll(endCouponList);

		return processingCouponList;
	}

	public ResponseGetCoupon setCouponTypeAndForm(ResponseGetCoupon rgc) {
		int couponType = rgc.getCouponType();
		int couponForm = rgc.getCouponForm();

		if(couponType ==2){
			rgc.setCouponTypeName("抢单抵扣券");
			if(couponForm == 2){
				rgc.setCouponFormName("满"+ new Double(rgc.getCouponFullreduction()).intValue()+ "可用");
			}
		}else{
			rgc.setCouponTypeName("充值抵扣券");
			if(couponForm == 2){
				rgc.setCouponFormName("充"+ new Double(rgc.getCouponFullreduction()).intValue()+ "可用");
			}
		}

		if(couponForm ==3){
			rgc.setCouponFormName("无门槛使用");
		}else if(couponForm == 1){
			rgc.setCouponFormName(rgc.getCouponDiscount()+"折使用");
		}
		return rgc;
	}

	/**
	 * @Author:Tina
	 * @Description:查看是否添加过此积分的明细（避免因为服务器延迟，添加了多条记录，所以先查后增）
	 * @Date:2018年6月7日下午3:40:53
	 * @Param:
	 * @Return:Integer
	 */
	public Integer isAddInCreditDetail(RequestCreditDetail requestCreditDetail) {
		return creditMapper.isAddInCreditDetail(requestCreditDetail);
	}

	/**
	 * @Author:Tina
	 * @Description:充值，浏览活动，分享邀请时加积分调的接口
	 * @Date:2018年6月7日下午4:21:09
	 * @Param:
	 * @Return:void
	 */
	public void addCreditByChannels(RequestCreditDetail requestCreditDetail) {

		String userId = requestCreditDetail.getUserId();
		requestCreditDetail.setType(2);
		try {
			int channelType = requestCreditDetail.getChannelType();
			if (channelType == 1) { // 充值：每次充值送等额积分
				requestCreditDetail.setCreditPathway("充值");
				requestCreditDetail.setCreditDetail("充值");
				Integer isAddInCreditDetail = creditMapper.isAddInCreditDetail(requestCreditDetail);// 先查询有没有添加过
				if (isAddInCreditDetail == null) {
					creditMapper.addCreditDetail(requestCreditDetail);
					creditMapper.updateCreditWallet(userId, requestCreditDetail.getCreditValue());
				}

			} else if (channelType == 2) { // 浏览活动：首次浏览活动超过5秒获得5积分
				requestCreditDetail.setCreditPathway("浏览活动");
				requestCreditDetail.setCreditDetail("浏览活动");
				requestCreditDetail.setCreditValue(5);
				// 先去查这个用户之前是否浏览过此活动
				Integer eventId = creditMapper.isBrowseSameEvent(requestCreditDetail);
				if (eventId == null) {
					creditMapper.addCreditDetail(requestCreditDetail);
					creditMapper.updateCreditWallet(userId, requestCreditDetail.getCreditValue());
					creditMapper.addBrowseRecord(requestCreditDetail);//添加浏览记录
				}

			} else if (channelType == 3) {// 分享邀请：分享一次1积分，每日上限20积分
				requestCreditDetail.setCreditPathway("分享邀请");
				requestCreditDetail.setCreditDetail("分享邀请");
				requestCreditDetail.setCreditValue(1);
				String reKey = "userShare_" + userId + "_" + DateUtil.getDay();
				int revalue = 0;
				long dayMis = 0;
				// 获取当前时间与当天最后时间相差的毫秒值
				dayMis = sdfDay.parse(DateUtil.getDay()).getTime() + 1000 * 60 * 60 * 24 - 1
						- System.currentTimeMillis();
				// 查看今天有没有分享过邀请链接
				if (redisCacheManager.hasKey(reKey)) { // 如果分享过

					revalue = (int) redisCacheManager.get(reKey);
					if (revalue < 20) {
						// 分享次数<20，加1积分
						creditMapper.addCreditDetail(requestCreditDetail);
						// 更新用户积分
						creditMapper.updateCreditWallet(userId, 1);
						revalue += 1;
						redisCacheManager.set(reKey, revalue, dayMis / 1000);
					}
				} else { // 今天第一次分享
					creditMapper.addCreditDetail(requestCreditDetail);
					creditMapper.updateCreditWallet(userId, 1);
					revalue += 1;
					// 设置此人当天的key，value
					redisCacheManager.set(reKey, revalue, dayMis / 1000);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Author:Tina
	 * @Description:查询用户更新到此版本是否获取过积分
	 * @Date:2018年6月7日下午7:13:21
	 * @Param:
	 * @Return:Integer
	 */
	public Integer isGetCreditByNewEdition(RequestAddCreditByNewEdition versionInfo) {

		return creditMapper.isGetCreditByNewEdition(versionInfo);
	}

	/**
	 * @Author:Tina
	 * @Description:用户更新版本获取积分，存到表里用作记录
	 * @Date:2018年6月7日下午7:29:08
	 * @Param:
	 * @Return:void
	 */
	public void addCreditByNewEdition(RequestAddCreditByNewEdition versionInfo) {
		creditMapper.addCreditByNewEdition(versionInfo);

	}

	/**
	 * @Author:Tina
	 * @Description:根据标识码查询要兑换东西的剩余数量
	 * @Date:2018年6月9日下午1:21:53
	 * @Param:
	 * @Return:int
	 */
	public int getExchangeCountByCode(String couponCode) {

		return creditMapper.getExchangeCountByCode(couponCode);
	}

	/**
	 * @Author:Tina
	 * @Description:用户点击兑换，把兑换商品的数量减1
	 * @Date:2018年6月9日下午1:21:57
	 * @Param:
	 * @Return:void
	 */
	public void setExchangeCount(String couponCode) {
		creditMapper.setExchangeCount(couponCode);

	}

	/**
	 * @Author:Tina
	 * @Description:用户点击兑换，把兑换的东西挪到该用户的账户
	 * @Date:2018年6月9日下午1:22:20
	 * @Param:
	 * @Return:int
	 */
	public int goCreditForExchange(RequestCouponByUserId requestCouponByUserId) {
		
		
		String userId = requestCouponByUserId.getUserId();
		
		// 获取此用户当前剩余的积分
		Integer totalCredit = creditMapper.queryTotalCredit(userId);
		
		
		// 根据该券的标识码查询此种券的面额，生效时间，过期时间等
		RequestCouponForNewUser rcfn = creditMapper.getExchangeInfoByCode(requestCouponByUserId.getCouponCode());
		if (rcfn != null) {
			//前端传过来的需要的积分和后端查到的积分不一致
			if(requestCouponByUserId.getCreditValue() != rcfn.getCreditValue() ){
				return 0;
			}
			//前端传过来的需要的积分和后端查到的积分一致，且此用户剩余的积分大于要消耗的积分，才可以兑换
			if(requestCouponByUserId.getCreditValue() == rcfn.getCreditValue() && rcfn.getCreditValue() <= totalCredit){ 
				// 剩余券的数量大于0，用户才可以领
				if (rcfn.getCouponCount() > 0) {
					String couponId = "QW" + idWorker.nextId();
					requestCouponByUserId.setCouponId(couponId);
					requestCouponByUserId.setGetType(2);// 标记是积分兑换获取的
					// 用户领过券之后，把领券信息插入到数据库表里，用作记录
					Integer result = couponMapper.addCouponForUserAutomaticly(requestCouponByUserId);
					if (result != null && result == 1) {
						RequestCouponForNewUser requestCouponForNewUser = new RequestCouponForNewUser();
						requestCouponForNewUser.setCouponType(rcfn.getCouponType());
						requestCouponForNewUser.setCouponForm(rcfn.getCouponForm());
						requestCouponForNewUser.setCouponId(couponId);
						requestCouponForNewUser.setUserId(userId);
						requestCouponForNewUser.setCouponAmount(rcfn.getCouponAmount());
						requestCouponForNewUser.setCouponFullreduction(rcfn.getCouponFullreduction());
						requestCouponForNewUser.setCouponDiscount(rcfn.getCouponDiscount());
						// 如果优惠券是发放开始计时，直接从后台取优惠券的有效期
						if (rcfn.getTimingType() == 1) {
							requestCouponForNewUser.setEffectiveTime(rcfn.getEffectiveTime());
							requestCouponForNewUser.setExpirationTime(rcfn.getExpirationTime());
						} else {// 优惠券是领取开始计时，计算优惠券的有效期
							Integer couponValidity = rcfn.getCouponValidity();// 获取优惠券的有效时长
							long currentTime = System.currentTimeMillis();
							Date begindate = new Date(currentTime);
							Date enddate = new Date(currentTime + couponValidity * 60 * 60 * 1000L);
							requestCouponForNewUser.setEffectiveTime(sdfTimeHour.format(begindate));
							requestCouponForNewUser.setExpirationTime(sdfTimeHour.format(enddate));

						}

						requestCouponForNewUser.setCouponChannel("积分兑换");
						// 把用户领取的优惠券插入到优惠券表里
						Integer addResult = couponMapper.addCouponForGettingUser(requestCouponForNewUser);
						if (addResult != null && addResult == 1) {
							//扣去用户消耗的积分
							RequestCreditDetail requestCreditDetail = new RequestCreditDetail();
							requestCreditDetail.setUserId(userId);
							requestCreditDetail.setType(1);
							requestCreditDetail.setCreditValue(rcfn.getCreditValue());
							requestCreditDetail.setCreditPathway("兑换抵扣券");
							requestCreditDetail.setCreditDetail(requestCouponByUserId.getCouponCode());
							this.addCreditDetail(requestCreditDetail);
							
							this.updateCreditWallet(userId, rcfn.getCreditValue()*(-1));
							
							
							// 正常返回
							payMoneyService.addMessage(userId, 1, "优惠券发放通知",
									"恭喜您成功兑换优惠券一张，请到我的优惠券页面查看！");
							return 1;
						}
					}
				} else {
					// 优惠券领完了
					return 2;
				}
			}else{
				//积分余额不足
				return 3;
			}
			
		}
		// 异常返回
		return 0;
	}

	/**
	 * @Author:Tina
	 * @Description:查看此订单跟单反馈成功是否赠送过奖励
	 * @Date:2018年6月12日下午4:53:01
	 * @Param:
	 * @Return:int
	 */
	public Integer isAddForFBSuccess(RequestCreditDetail requestCreditDetail) {
		return creditMapper.isAddForFBSuccess(requestCreditDetail);
	}

	/**
	 * 定时积分清零以及备份
	 */
    public void clearTimingAndBackUp(){
         int year= Integer.valueOf(VeDate.getStringTime("yyyy"))-1;
         String tableName="yjd_credit_wallet_back_up_"+year;
		creditMapper.createTableIfNotExist(tableName);
		int result=creditMapper.backUpTable(tableName);
		if(result>0){
			LOG.info("备份成功");
		}
		result=creditMapper.clearTiming();
		if(result>0){
			LOG.info("积分清零成功");
		}
	}
}
