package com.stardai.manage.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.stardai.manage.pojo.PayWallet;
import com.stardai.manage.response.ResponseIncome;
import com.stardai.manage.response.ResponseOut;

/**
 * @author jdw
 * @date 2017/10/16
 */
public interface PayMoneyMapper {

	// 查询用户
	Integer queryUser(@Param("mobileNumber") String mobileNumber, @Param("userId") String userId);

	// 设置支付密码
	Integer addPayPd(@Param("pd") String pd, @Param("userId") String userId);

	// 修改支付密码
	Integer updatePayPd(@Param("pd") String pd, @Param("userId") String userId);

	// 向充值表中添加数据
	void addPayPresentMessage(@Param("userId") String userId, @Param("incomeNo") String incomeNo,
                              @Param("pathWay") String channel, @Param("money") Double amount
							, @Param("actualMoney") Double amountYuan, @Param("type") Integer type);

	// 向金额表中添加数据
	void addPresentMoney(@Param("userId") String userId, @Param("money") double money);

	// 向支出表中添加数据
	void addOutComeMoney(@Param("userId") String userId, @Param("orderNo") long orderNo, @Param("outNo") String outNo,
                         @Param("channel") String channel, @Param("amountYuan") double amountYuan);

	// 根据userId查询金额
	int queryRechargeMoney(@Param("userId") String userId);

	// 修改金额
	void updateRechargeMoney(@Param("userId") String userId, @Param("amountYuan") Double amountYuan);

	// 个人消息添加到数据库
	void addMessage(@Param("userId") String userId, @Param("type") Integer type, @Param("title") String title,
                    @Param("message") String message);

	// 修改数据库金额
	void updatePresentUserMoney(@Param("userId") String userId, @Param("result2") double result2);

	// 查询余额
	PayWallet queryAllMoney(@Param("userId") String userId);

	// 查询个人的充值记录
	List<ResponseIncome> queryAllIncome(@Param("page") int page, @Param("pageSize") int pageSize,
                                        @Param("userId") String userId);

	// 查询支出记录
	List<ResponseOut> queryAllOutcome(@Param("page") int page, @Param("pageSize") int pageSize,
                                      @Param("userId") String userId);

	// 先去查询是否已经添加过这条数据
	Integer queryIncomeMessage(@Param("userId") String userId, @Param("incomeNo") String incomeNo);

	// 给充值人加上赠送的星币
	void updatePresentMoney(@Param("userId") String tuiJianRenUserId, @Param("giveMoney") double giveMoney);

	// 根据充值的金额去查询赠送的金额
	Integer queryAddMoney(@Param("amount") int amount);
	
	// 插入不同渠道消费的星币 来源
	void addMoneySource(@Param("order_no") Long order_no, @Param("recharge") Double recharge,
                        @Param("present") Double present, @Param("channel") String channel,
                        @Param("user_id") String user_id);


	// 插入不同渠道消费的星币 来源
	void addMoneySource2(@Param("order_no") Long order_no, @Param("recharge") Double recharge,
                         @Param("present") Double present, @Param("channel") String channel,
                         @Param("channelBranch") String channelBranch, @Param("user_id") String user_id,
						 @Param("valid") int valid);

	/**
	 * @Author:Tina
	 * @Description:向支出表中添加数据,使用优惠券
	 * @Date:2018年4月13日下午1:24:27
	 * @Param:
	 * @Return:void
	 */
	void addOutComeMoneyForCoupon(@Param("userId") String userId, @Param("orderNo") Long orderNo, @Param("outNo") String outNo,
                                  @Param("channel") String channel, @Param("amountYuan") double amountYuan, @Param("couponAmount") double couponAmount);

	/**
	 * @Author:Tina
	 * @Description:用户注册，送优惠券的同时，在用户余额表里插入一条记录，方便后面充值，更新不会报错
	 * @Date:2018年4月23日下午6:01:02
	 * @Param:
	 * @Return:void
	 */
	void addPayRecord(@Param("userId") String userId);

	/**
	 * @Author:Tina
	 * @Description:查询当前时间是否有充值加赠活动
	 * @Date:2018年6月13日下午2:12:03
	 * @Param:
	 * @Return:HashMap<String,Integer>
	 */
	HashMap<String,Integer> queryExtraMoney(@Param("amount") int amount, @Param("currentTime") long currentTime);
}
