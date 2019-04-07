/**
 * 
 */
package com.stardai.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.stardai.manage.request.RequestAddCreditByNewEdition;
import com.stardai.manage.request.RequestAllIncome;
import com.stardai.manage.request.RequestCouponByUserId;
import com.stardai.manage.request.RequestCouponForNewUser;
import com.stardai.manage.request.RequestCreditDetail;
import com.stardai.manage.response.ResponseGetCoupon;

/**
 * @author Tina
 * 2018年5月30日
 */

@Repository
public interface CreditMapper {

	/**
	 * @Author:Tina
	 * @Description:根据此人userId查询此人当天有没有签到
	 * @Date:2018年5月31日下午3:01:21
	 * @Param:
	 * @Return:Integer
	 */
	Integer queryIsSigninByUserId(@Param("userId") String userId, @Param("currentTime") String currentTime);

	/**
	 * @Author:Tina
	 * @Description:获取连续签到天数对应的积分值
	 * @Date:2018年5月31日下午3:47:42
	 * @Param:
	 * @Return:Integer
	 */
	Integer getValueBySigninCount(Integer signinCount);

	/**
	 * @Author:Tina
	 * @Description:插入当天的签到记录
	 * @Date:2018年5月31日下午3:51:03
	 * @Param:
	 * @Return:Object
	 */
	Integer addNewSigninRecord(@Param("userId") String userId, @Param("signinCredit") Integer signinCredit, @Param("signinCount") Integer signinCount);

	/**
	 * @Author:Tina
	 * @Description:获取此用户当前剩余的积分
	 * @Date:2018年5月31日下午4:44:13
	 * @Param:
	 * @Return:Integer
	 */
	Integer queryTotalCredit(String userId);

	/**
	 * @Author:Tina
	 * @Description:获取此用户当天获取的积分
	 * @Date:2018年5月31日下午4:44:17
	 * @Param:
	 * @Return:Integer
	 */
	Integer queryTodayCredit(String userId);

	/**
	 * @Author:Tina
	 * @Description:添加一条积分明细
	 * @Date:2018年6月1日上午9:38:45
	 * @Param:
	 * @Return:void
	 */
	void addCreditDetail(RequestCreditDetail requestCreditDetail);

	/**
	 * @Author:Tina
	 * @Description:更新用户积分
	 * @Date:2018年6月1日上午9:46:09
	 * @Param:
	 * @Return:void
	 */
	void updateCreditWallet(@Param("userId") String userId, @Param("creditValue") Integer creditValue);

	/**
	 * @Author:Tina
	 * @Description:查询积分汇总表里有没有该用户的记录
	 * @Date:2018年6月1日上午11:17:38
	 * @Param:
	 * @Return:Integer
	 */
	Integer queryIsEverGetCredit(String userId);

	/**
	 * @Author:Tina
	 * @Description:在积分汇总表插入一条新的记录
	 * @Date:2018年6月1日上午11:18:12
	 * @Param:
	 * @Return:void
	 */
	void addCreditWallet(@Param("userId") String userId, @Param("creditValue") Integer creditValue);

	/**
	 * @Author:Tina
	 * @Description:获取用户积分明细列表
	 * @Date:2018年6月1日下午4:46:12
	 * @Param:
	 * @Return:List<RequestCreditDetail>
	 */
	List<RequestCreditDetail> getCreditDetails(RequestAllIncome requestAllIncome);

	/**
	 * @Author:Tina
	 * @Description:积分兑换中心进行中的优惠券
	 * @Date:2018年6月4日上午9:55:58
	 * @Param:
	 * @Return:List<ResponseGetCoupon>
	 */
	List<ResponseGetCoupon> getProcessingCouponList(RequestCouponByUserId requestCouponByUserId);

	/**
	 * @Author:Tina
	 * @Description:积分兑换中心未到领取时间的优惠券
	 * @Date:2018年6月4日上午9:56:02
	 * @Param:
	 * @Return:List<ResponseGetCoupon>
	 */
	List<ResponseGetCoupon> getBerforeTimeCouponList(RequestCouponByUserId requestCouponByUserId);

	/**
	 * @Author:Tina
	 * @Description:积分兑换中心已经结束的优惠券
	 * @Date:2018年6月4日上午9:56:07
	 * @Param:
	 * @Return:List<ResponseGetCoupon>
	 */
	List<ResponseGetCoupon> getEndCouponList(RequestCouponByUserId requestCouponByUserId);

	/**
	 * @Author:Tina
	 * @Description:查询此用户抢过多少原价单
	 * @Date:2018年6月6日下午6:04:23
	 * @Param:
	 * @Return:Integer
	 */
	Integer getOriginalCountByUserId(String userId);

	/**
	 * @Author:Tina
	 * @Description:更新用户抢的原价单数量
	 * @Date:2018年6月6日下午6:22:34
	 * @Param:
	 * @Return:void
	 */
	void updateOriginalCount(String userId);

	/**
	 * @Author:Tina
	 * @Description:插入用户抢的一条原价单记录
	 * @Date:2018年6月6日下午6:23:05
	 * @Param:
	 * @Return:void
	 */
	void insertOriginalCount(String userId);

	/**
	 * @Author:Tina
	 * @Description:查看是否添加过此积分的明细（避免因为服务器延迟，添加了多条记录，所以先查后增）
	 * @Date:2018年6月7日下午3:46:29
	 * @Param:
	 * @Return:Integer
	 */
	Integer isAddInCreditDetail(RequestCreditDetail requestCreditDetail);

	/**
	 * @Author:Tina
	 * @Description:查这个用户之前是否浏览过此活动
	 * @Date:2018年6月7日下午5:15:38
	 * @Param:
	 * @Return:Integer
	 */
	Integer isBrowseSameEvent(RequestCreditDetail requestCreditDetail);

	/**
	 * @Author:Tina
	 * @Description:查询用户更新到此版本是否获取过积分
	 * @Date:2018年6月7日下午7:13:52
	 * @Param:
	 * @Return:Integer
	 */
	Integer isGetCreditByNewEdition(RequestAddCreditByNewEdition versionInfo);

	/**
	 * @Author:Tina
	 * @Description:用户更新版本获取积分，存到表里用作记录
	 * @Date:2018年6月7日下午7:29:53
	 * @Param:
	 * @Return:void
	 */
	void addCreditByNewEdition(RequestAddCreditByNewEdition versionInfo);

	/**
	 * @Author:Tina
	 * @Description:用户浏览活动，添加一条记录
	 * @Date:2018年6月8日下午4:18:24
	 * @Param:
	 * @Return:void
	 */
	void addBrowseRecord(RequestCreditDetail requestCreditDetail);

	/**
	 * @Author:Tina
	 * @Description:根据标识码查询要兑换东西的剩余数量
	 * @Date:2018年6月9日下午1:29:13
	 * @Param:
	 * @Return:int
	 */
	int getExchangeCountByCode(String couponCode);

	/**
	 * @Author:Tina
	 * @Description:用户点击兑换，把兑换商品的数量减1
	 * @Date:2018年6月9日下午1:29:39
	 * @Param:
	 * @Return:Object
	 */
	void setExchangeCount(String couponCode);

	/**
	 * @Author:Tina
	 * @Description:获取兑换物品的详细信息
	 * @Date:2018年6月9日下午1:43:39
	 * @Param:
	 * @Return:RequestCouponForNewUser
	 */
	RequestCouponForNewUser getExchangeInfoByCode(String couponCode);
	
	/**
	 * 
	 * @Author:Tina
	 * @Description:查看此订单跟单反馈成功是否赠送过奖励
	 * @Date:2018年6月12日下午4:51:20
	 * @Param:
	 * @Return:Integer
	 */
	Integer isAddForFBSuccess(RequestCreditDetail requestCreditDetail);

	/**
	 * 如果备份表不存在则创建
	 * @param tableName
	 * @return
	 */
	int createTableIfNotExist(@Param("tableName") String tableName);

	/**
	 * 备份数据
	 * @param tableName
	 * @return
	 */
	int backUpTable(@Param("tableName") String tableName);

	/**积分清零
	 * @return
	 */
	int clearTiming();
}
