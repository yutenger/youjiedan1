package com.stardai.manage.mapper;

import java.util.List;
import com.stardai.manage.request.RequestOrdersByManyConditions;
import com.stardai.manage.request.RequestRefundApplication;
import org.apache.ibatis.annotations.Param;

import com.stardai.manage.pojo.FieldTransformation;
import com.stardai.manage.pojo.OrderTime;
import com.stardai.manage.pojo.PayMoney;
import com.stardai.manage.request.RequestFeedBackBad;
import com.stardai.manage.request.RequestFeedBackSuccess;
import com.stardai.manage.response.ResponseConfirmPrice;
import com.stardai.manage.response.ResponseLoanUserInitial;
import com.stardai.manage.response.ResponseLoanUserInitialList;
import com.stardai.manage.response.ResponseOrderList;
import com.stardai.manage.response.ResponseOrderRefund;
import com.stardai.manage.response.ResponseRefund;
@SuppressWarnings("all")
public interface OrderMapper {

	// 查询状态对应的时间
	List<OrderTime> queryAllStatus();

	/**
	 * @Author:Tina
	 * @Description:全部订单
	 * @Date:2018年5月19日下午2:58:30
	 * @Param:
	 * @Return:List<ResponseLoanUserInitialList>
	 */
	List<ResponseLoanUserInitialList> queryAllOrdersByManyConditions(RequestOrdersByManyConditions conditions);

	/**
	 * @Author:Tina
	 * @Description:最新单子，最新的单子=数据库存的时间<现在时间-设定最新的时间
	 * @Date:2018年5月19日下午2:59:02
	 * @Param:
	 * @Return:List<ResponseLoanUserInitialList>
	 */
	List<ResponseLoanUserInitialList> queryZuiXinOrdersByManyConditions(RequestOrdersByManyConditions conditions);

	/**
	 * @Author:Tina
	 * @Description:5元促销 ，5元的单子=数据库存的时间<现在时间-设定免费的时间
	 * @Date:2018年5月19日下午2:59:44
	 * @Param:
	 * @Return:List<ResponseLoanUserInitialList>
	 */
	List<ResponseLoanUserInitialList> queryDaZheOrdersByManyConditions(RequestOrdersByManyConditions conditions);

	
	/**
	 * @Author:Tina
	 * @Description:1元单子，1元的单子=数据库存的时间<现在时间-设定免费的时间
	 * @Date:2018年5月19日下午2:59:22
	 * @Param:
	 * @Return:List<ResponseLoanUserInitialList>
	 */
	List<ResponseLoanUserInitialList> queryMianFeiOrdersByManyConditions(RequestOrdersByManyConditions conditions);

	/**
	 * @Author:Tina
	 * @Description:查询有没有对最新的单子进行打折
	 * @Date:2018年5月21日上午10:06:56
	 * @Param:
	 * @Return:double
	 */
	double isDiscountForZuiXin(long currentTime);

	/**
	 * @Author:Tina
	 * @Description:根据订单号查询此单的花费的实际金额
	 * @Date:2018年6月7日上午10:11:33
	 * @Param:
	 * @Return:Double
	 */
	Double getCostMoneyByOrderNo(long orderNo);
	
	

	// 根据单子号查询订单的详细信息
	ResponseLoanUserInitial queryOrderByOrderNo(@Param("orderNo") long orderNo);

	// 查询此订单的原价
	ResponseConfirmPrice queryOrderPrice(@Param("orderNo") long orderNo);

	// 将订单储存到抢单人的数据中
	void addOrderByUserId(@Param("price") double price, @Param("userId") String userId, @Param("orderNo") long orderNo,
                          @Param("time") long time);

	// 根据userId查询订单
	List<ResponseOrderList> queryOrderListByUserId(@Param("page") int page, @Param("pageSize") int pageSize,
                                                   @Param("status") Integer status, @Param("userId") String userId);

	// 根据userId查询订单详情
	ResponseLoanUserInitial queryOrderByUserId(@Param("orderNo") long orderNo, @Param("userId") String userId);

	// 跟单反馈成功
	Integer feedBackSuccess(RequestFeedBackSuccess feedBackSuccess);

	// 跟单失败反馈
	Integer feedBackBad(RequestFeedBackBad feedBackBad);

	// 查询充值的金额与赠送的金额
	List<PayMoney> queryRechargeMoney();


	//查询是否已经就这个订单号提交过退款申请,已退款的不能再申请
	Integer queryRefundApplicationByOrderNo(@Param("orderNo") Long orderNo);

	//查询退款订单状态
	Integer queryRefundStatusByOrderNo(@Param("orderNo") Long orderNo);
	//查询被抢的单子付了多少星币
	Double queryGrabbedOrderPrice(@Param("orderNo") Long orderNo);

	//保存退款申请
    Integer insertRefundApplication(RequestRefundApplication refund);


	/**
	 * @Author:Tina
	 * @Description:把调三方接口查到的手机号归属地存到数据库
	 * @Date:2018年5月2日下午1:31:31
	 * @Param:
	 * @Return:void
	 */
	void setPhoneAttribution(@Param("loanPhoneAttribution") String loanPhoneAttribution, @Param("orderNo") Long orderNo);
	//根据订单号和订单Id查询是否匹配
    Integer queryOrderIdMatch(@Param("userId") String userId, @Param("orderNo") Long orderNo);
     //根据订单号ID查询退单详情
    ResponseOrderRefund queryOrderRefundByOrderNo(@Param("orderNo") Long orderNo);
    //根据userId查询退单列表
    List<ResponseRefund>  queryUserOrderListByUserId(@Param("userId") String userId, @Param("pageSize") Integer page, @Param("offset") Integer offset);

	

	/**
	 * @Author:Tina
	 * @Description:获取字段转换列表
	 * @Date:2018年7月4日下午7:01:03
	 * @Param:
	 * @Return:List<FieldTransformation>
	 */
	List<FieldTransformation> getFieldTransformation();

}
