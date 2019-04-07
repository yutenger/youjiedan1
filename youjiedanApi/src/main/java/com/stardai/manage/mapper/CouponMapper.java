/**
 * 
 */
package com.stardai.manage.mapper;

import java.util.HashMap;
import java.util.List;

import com.stardai.manage.request.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.stardai.manage.pojo.CouponInfo;
import com.stardai.manage.response.ResponseGetCoupon;

/**
 * @author Administrator
 * 2018年4月10日
 */

@Repository
public interface CouponMapper {
	
	/**
	 * 
	 * @Author:Tina
	 * @Description:为新注册用户发放优惠券 
	 * @Date:2018年4月12日下午6:59:57
	 * @Param:
	 * @Return:Integer
	 */
    Integer addCouponForNewUser(List<RequestCouponForNewUser> coupons);

    /**
     * 
     * @Author:Tina
     * @Description:获取优惠券列表
     * @Date:2018年4月12日下午7:00:21
     * @Param:
     * @Return:List<HashMap<String,Object>>
     */
    List<CouponInfo> queryCouponByStatus(RequestCouponByUserId requestCouponByUserId);
    
    List<CouponInfo> queryCouponUsedByStatus(RequestCouponByUserId requestCouponByUserId);
    
    
	/**
	 *
	 * @Author:Tina
	 * @Description:获取注册赠送优惠券的有效时间,面额种类
	 * @Date:2018年4月12日下午1:31:21
	 * @Param:
	 * @Return:Long
	 */
    HashMap<String,Object> getValidTimeForNewUser();

	/**
	 * @Author:Tina
	 * @Description:查看该用户当前可使用的优惠券张数
	 * @Date:2018年4月12日下午2:54:49
	 * @Param:
	 * @Return:HashMap<String,Object>
	 */
	HashMap<String, Object> getCouponCounts(String userId);

	/**
	 * @Author:Tina
	 * @Description:根据优惠券id查询优惠券信息
	 * @Date:2018年4月13日上午11:00:07
	 * @Param:
	 * @Return:double
	 */
	RequestCouponInfo queryCouponInfo(String couponId);

	/**
	 * @Author:Tina
	 * @Description:把已使用的优惠券添加到另一张表里
	 * @Date:2018年4月13日下午3:31:16
	 * @Param:
	 * @Return:Integer
	 */
	Integer addCouponUsed(RequestCouponInfo requestCouponInfo);

	/**
	 * @Author:Tina
	 * @Description:从原来的表里把已使用的优惠券移除
	 * @Date:2018年4月13日下午3:33:03
	 * @Param:
	 * @Return:void
	 */
	void removeUsedCoupon(String couponId);

	/**
	 * @Author:Tina
	 * @Description:查看当天有没有点击量
	 * @Date:2018年4月16日下午6:10:51
	 * @Param:
	 * @Return:Integer
	 */
	Integer getAmountByDateAndStatus(@Param("couponStatus") int couponStatus, @Param("day") String day);

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年4月16日下午6:10:56
	 * @Param:
	 * @Return:void
	 */
	void updateClickAmount(@Param("couponStatus") int couponStatus, @Param("day") String day);

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年4月16日下午6:11:00
	 * @Param:
	 * @Return:void
	 */
	void addClickAmount(@Param("couponStatus") int couponStatus, @Param("day") String day);

	/**
	 * @Author:Tina
	 * @Description:注册送优惠券发生异常的话，就把该用户的userId插入到一张表里
	 * @Date:2018年4月17日上午8:56:37
	 * @Param:
	 * @Return:void
	 */
	void addUnusualRecords(String userId);

	/**
	 * @Author:Tina
	 * @Description:当前正在进行中的优惠券
	 * @Date:2018年5月25日下午3:19:37
	 * @Param:
	 * @Return:List
	 */
	List<ResponseGetCoupon> getProcessingCouponList(RequestCouponByUserId requestCouponByUserId);

	/**
	 * @Author:Tina
	 * @Description:未开始的优惠券列表
	 * @Date:2018年5月25日下午3:19:41
	 * @Param:
	 * @Return:List
	 */
	List<ResponseGetCoupon> getBerforeTimeCouponList(RequestCouponByUserId requestCouponByUserId);

	/**
	 * @Author:Tina
	 * @Description:已结束的优惠券列表
	 * @Date:2018年5月25日下午3:19:47
	 * @Param:
	 * @Return:List
	 */
	List<ResponseGetCoupon> getEndCouponList(RequestCouponByUserId requestCouponByUserId);

	/**
	 * @Author:Tina
	 * @Description:查看此人当天有没有领过优惠券
	 * @Date:2018年5月25日下午6:10:02
	 * @Param:
	 * @Return:String
	 */
	String queryIsGetCouponToday(@Param("userId") String userId, @Param("couponCode") String couponCode);

	/**
	 * @Author:Tina
	 * @Description:用户领过券之后，把领券信息插入到数据库表里，以便查询用户是否领取过该券
	 * @Date:2018年5月26日下午8:19:36
	 * @Param:
	 * @Return:int
	 */
	int addCouponForUserAutomaticly(RequestCouponByUserId requestCouponByUserId);

	/**
	 * @Author:Tina
	 * @Description:把领取的优惠券插入优惠券表里
	 * @Date:2018年5月26日下午8:50:13
	 * @Param:
	 * @Return:Integer
	 */
	Integer addCouponForGettingUser(RequestCouponForNewUser requestCouponForNewUser);

	/**
	 * @Author:Tina
	 * @Description:根据优惠券的标识符查询优惠券的具体信息
	 * @Date:2018年5月27日下午12:31:25
	 * @Param:
	 * @Return:RequestCouponForNewUser
	 */
	RequestCouponForNewUser getCouponInfoByCode(String couponCode);

	/**
	 * @Author:Tina
	 * @Description:用户领取优惠券之后，数量减1
	 * @Date:2018年5月27日下午3:08:50
	 * @Param:
	 * @Return:void
	 */
	void setCouponCount(String couponCode);

	/**
	 * @Author:Tina
	 * @Description:根据优惠券码获取优惠券的剩余数量
	 * @Date:2018年5月28日下午3:49:48
	 * @Param:
	 * @Return:int
	 */
	int getCouponCountByCode(String couponCode);

	/**
	 * 
	 * @Author:Tina
	 * @Description:查询此人有没有领取过此种优惠券
	 * @Date:2018年6月14日下午4:05:41
	 * @Param:
	 * @Return:String
	 */
	String queryIsGetCouponBefore(RequestCouponByUserId requestCouponByUserId);


	int  queryIsGetCouponBeforeCount(RequestCouponByUserId requestCouponByUserId);
	/**
	 *  查询该用户充值时可使用的优惠券
	 * @param userId
	 * @return
	 */
	List<CouponInfo> queryCouponForRecharge(String userId);

    List<CouponInfo> queryCouponForGivingMoney(RequestCouponByRechargingMoney requestCouponByRechargingMoney);

    //查询已使用的优惠券张数
	Integer getAllCouponCountsUsed(RequestAllIncome requestAllIncome);

	//查询未使用的优惠券张数
	Integer getAllCouponCountsUnused(RequestAllIncome requestAllIncome);

	//查询过期的优惠券张数
	Integer getAllCouponCountsOverdue(RequestAllIncome requestAllIncome);

	Integer getUseAbleCouponCounts(@Param("userId") String userId,@Param("couponType") int couponType);
	String queryCouponCodeBycouponId(String couponId);
}
