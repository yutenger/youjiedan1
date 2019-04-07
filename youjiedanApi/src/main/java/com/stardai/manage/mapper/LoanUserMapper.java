package com.stardai.manage.mapper;

import java.util.HashMap;
import java.util.List;

import com.stardai.manage.pojo.CreditScore;
import org.apache.ibatis.annotations.Param;
import com.stardai.manage.request.RequestCreditScore;
import com.stardai.manage.request.RequestLoanUserInfo;
import com.stardai.manage.response.ResponseLoanOrder;

/**
 * @author jdw
 * @date 2017/10/16
 */
public interface LoanUserMapper {

    //小星借款APP添加借款信息(小星借款1.2.6版本后启用)
    void insertLoanInfo(RequestLoanUserInfo loanUserInfo);

    //新接口使用
    void insertLoanInfoNew(RequestLoanUserInfo loanUserInfo);


    // 设置此订单已经被抢
    Integer updateLoanUserStatus(@Param("orderNo") long orderNo, @Param("status") int status);

    //API 设置打折状态
    Integer updateLoanUserDiscountState(@Param("orderNo") long orderNo, @Param("discountedState") int discountedState);

    // 再次查询此单有没有被抢
    Integer queryStatusByOrderNo(@Param("orderNo") long orderNo);

    //查询分数表(小星借款1.2.6版本以后)
    List<CreditScore> queryScoreNew();


    // 直接到贷款经理order表中查询是否存在
    ResponseLoanOrder queryOrder(@Param("orderNo") long orderNo);

    //根据订单号查找申请贷款时间
    Long queryLoanOrderTime(@Param("orderNo") long orderNo);

    // 根据用户提交的借款申请中的所在城市到数据库中的城市名统一表中查一下转换成统一的城市名叫法,以便优接单软件按照所在城市搜索
    String queryLocation(@Param("location") String location);

    //小星借款APP添加借款信息(小星借款1.2.6版本后启用)
    void insertLoanUserInfo(HashMap<String, Object> loanUserInfo);

    //小星借款查询抢单要付多少星币
    Integer queryPrice(@Param("score") Integer score);

    //查询当前借款人是否已经申请过，并返回上次申请的时间
    Long queryIsApply(HashMap<String, Object> loanUserInfo);

    //通过订单号查询借款人手机号
    String queryLoanPhoneByOrderNo(Long orderNo);

    //查询此订单是否导给企业用户
    ResponseLoanOrder queryIsGrabByEnterprise(long orderNo);


    /**
     * @Author:Tina
     * @Description:查询字段对应的评分
     * @Date:2018年7月5日下午5:23:22
     * @Param:
     * @Return:List<RequestCreditScore>
     */
    List<RequestCreditScore> queryCreditScore();

    /**
     * 新接口评分查询
     *
     * @return
     */
    List<RequestCreditScore> queryCreditScoreNew();

    /**
     * @Author:Tina
     * @Description:查询用户是否在黑名单列表
     * @Date:2018年7月5日下午6:41:53
     * @Param:
     * @Return:Integer
     */
    Integer getIsInBlackList(RequestLoanUserInfo loanUserInfo);


    /**
     * @Author:Tina
     * @Description:查询此人最近是否申请过订单
     * @Date:2018年7月5日下午6:50:47
     * @Param:
     * @Return:Long
     */
    Long queryIsApplyRecently(RequestLoanUserInfo loanUserInfo);

    /**
     * @Author:Tina
     * @Description:获取第二次申请可以展示在优接单的间隔时间
     * @Date:2018年7月5日下午7:04:07
     * @Param:
     * @Return:Integer
     */
    Integer getShowTime();

    /**
     * @Author:Tina
     * @Description:根据字段的编号查询字段表示的含义
     * @Date:2018年7月5日下午7:11:56
     * @Param:
     * @Return:String
     */
    String getValueByCode(Integer code);

    //根据身份证号前几位获取此人所在的户籍城市
    String getCityCodeByIdCard(String cardNo);

    //当前库里没有的身份证号前几位存到数据库里
    void setCardNoAndCityCode(@Param("idCard") String idCard, @Param("cityCode") String cityCode, @Param("cityName") String cityName);

    //查询是否属于接单地区
    Integer queryIsInCity(String city);

    //查询给差资质单子打的折扣
    Double getDiscountForLowQualification(long now);

    //根据订单号查询此订单借款人所在城市
    String getLoanCityByOrderNo(long orderNo);

    //根据生成的订单号查找api订单号
    String queryApiIdByOrderNo(long orderNo);

    //回调后修改手机号
    Integer updateLoanUserPhone(String phone, long orderNum);

}
