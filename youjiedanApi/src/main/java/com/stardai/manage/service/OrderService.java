package com.stardai.manage.service;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.stardai.manage.mapper.*;
import com.stardai.manage.request.*;
import com.stardai.manage.response.*;
import com.stardai.manage.utils.*;
import okhttp3.FormBody;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.pojo.FieldTransformation;
import com.stardai.manage.pojo.OrderTime;
import com.stardai.manage.pojo.PayMoney;

import javax.management.ObjectName;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Service
@SuppressWarnings("all")
public class OrderService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private LoanUserMapper loanUserMapper;

    @Autowired
    private PhoneAttributionUtils phoneAttributionUtils;

    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    private CreditMapper creditMapper;

    @Autowired
    private HotCityMapper hotCityMapper;

    @Autowired
    private LoanUserService loanUserService;

    @Autowired
    private FunctionCountService functionCountService;
    @Autowired
    private FeedBackToChannelsMapper feedBackToChannelsMapper;
    private static String ENCODING = "UTF-8";

    private static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
    protected static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    // 查询状态对应的时间
    public List<OrderTime> queryAllStatus() {
        List<OrderTime> orderTime = orderMapper.queryAllStatus();
        return orderTime;
    }

    /**
     * @Author:Tina
     * @Description: 1.5.0版本, 筛选条件改变，订单筛选变多选，折扣专区单选，订单状态单选
     * @Date:2018年5月19日下午1:26:23
     * @Param:
     * @Return:List<ResponseLoanUserList>
     */
    public List<ResponseLoanUserTransferList> queryOrdersByManyConditions(RequestOrdersByManyConditions conditions,
                                                                          List<OrderTime> orderTimeList) {
        int page = conditions.getPage() * conditions.getPageSize();
        conditions.setPage(page);
        // 1: 最新发布，2: 6折促销，3: 5元抢单
        int status = conditions.getStatus();
        // 前端传过来的城市列表转换
        conditions.setCities(this.setCityTransform(conditions.getCities()));
        // 把前端接收来的订单筛选list转换一下
        conditions = this.setOrderChoice(conditions);

        conditions.setBeginMoney(conditions.getBeginMoney() * 10000);
        conditions.setEndMoney(conditions.getEndMoney() * 10000);

        int zuiXing = 0;
        int mianFei = 0;
        for (OrderTime orderTime : orderTimeList) {
            if (orderTime.getId() == 2) {
                zuiXing = orderTime.getOrderTime();
            } else if (orderTime.getId() == 3) {
                mianFei = orderTime.getOrderTime();
            }
        }

        Long zuiXingMillis = zuiXing * 60L * 60 * 1000;
        Long mianFeiMillis = mianFei * 60L * 60 * 1000;

        // 字段转换列表
        List<FieldTransformation> fieldTransformation = orderMapper.getFieldTransformation();
        // 传到前端的字符串列表
        List<ResponseLoanUserTransferList> loanUserList = new ArrayList<>();
        // 从数据库筛选出的单子列表
        List<ResponseLoanUserInitialList> loanUserInitialList = new ArrayList<>();

        if (status == 0) {
            // 根据条件能查询所有的单子
            loanUserInitialList = orderMapper.queryAllOrdersByManyConditions(conditions);
            if (loanUserInitialList != null && loanUserInitialList.size() > 0) {
                for (ResponseLoanUserInitialList loanUserInitial : loanUserInitialList) {
                    ResponseLoanUserTransferList loanUser = new ResponseLoanUserTransferList();
                    loanUser = this.changeField(loanUserInitial, fieldTransformation);
                    loanUser = this.changeTagsNew(loanUser, zuiXingMillis, mianFeiMillis);
                    loanUserList.add(loanUser);
                }
            } else {
                loanUserList = new ArrayList();
            }
            //埋点统计折扣类型
            functionCountService.addFunctionCountForApp("折扣_全部状态");

        } else if (status == 1) {
            // 根据条件能查询最新的单子 最新的单子=保存到数据库的时间>现在时间-设定最新的时间

            long time = System.currentTimeMillis() - zuiXingMillis;
            conditions.setTime(time);
            loanUserInitialList = orderMapper.queryZuiXinOrdersByManyConditions(conditions);

            if (loanUserInitialList != null && loanUserInitialList.size() > 0) {
                for (ResponseLoanUserInitialList loanUserInitial : loanUserInitialList) {
                    ResponseLoanUserTransferList loanUser = new ResponseLoanUserTransferList();
                    loanUser = this.changeField(loanUserInitial, fieldTransformation);
                    loanUser.setPriceStatus(1);
                    if (loanUser.getDiscountedState() == 1) {
                        loanUser.setPriceStatus(2);
                    }
                    loanUserList.add(loanUser);
                }
            } else {
                loanUserList = new ArrayList();
            }
            //埋点统计折扣类型
            functionCountService.addFunctionCountForApp("折扣_最新发布");
        } else if (status == 2) {
            // 根据条件能查询 第二种促销优惠 = 数据库存的时间>现在时间-设定1元的时间
            // 并且数据库存的时间<现在时间-设定最新的时间
            long end = System.currentTimeMillis() - mianFeiMillis;
            long start = System.currentTimeMillis() - zuiXingMillis;
            conditions.setStart(start);
            conditions.setEnd(end);
            conditions.setDiscountedState(1);
            loanUserInitialList = orderMapper.queryDaZheOrdersByManyConditions(conditions);
            if (loanUserInitialList != null && loanUserInitialList.size() > 0) {
                for (ResponseLoanUserInitialList loanUserInitial : loanUserInitialList) {
                    ResponseLoanUserTransferList loanUser = new ResponseLoanUserTransferList();
                    loanUser = this.changeField(loanUserInitial, fieldTransformation);
                    // 六折促销
                    loanUser.setPriceStatus(2);
                    loanUserList.add(loanUser);

                }
            } else {
                loanUserList = new ArrayList();
            }
            //埋点统计折扣类型
            functionCountService.addFunctionCountForApp("折扣_6折促销");

        } else if (status == 3) {
            // 根据条件能查询 第三种促销 的单子 =保存到数据库的时间<现在时间-设定1元抢单的时间
            long time = System.currentTimeMillis() - mianFeiMillis;
            conditions.setTime(time);
            loanUserInitialList = orderMapper.queryMianFeiOrdersByManyConditions(conditions);
            if (loanUserInitialList != null && loanUserInitialList.size() > 0) {
                for (ResponseLoanUserInitialList loanUserInitial : loanUserInitialList) {
                    ResponseLoanUserTransferList loanUser = new ResponseLoanUserTransferList();
                    loanUser = this.changeField(loanUserInitial, fieldTransformation);
                    // 5元
                    loanUser.setPriceStatus(3);
                    loanUserList.add(loanUser);

                }
            } else {
                loanUserList = new ArrayList();
            }

            //埋点统计折扣类型
            functionCountService.addFunctionCountForApp("折扣_5元抢单");
        }

        return loanUserList;
    }

    /**
     * @Author:Tina
     * @Description:TODO
     * @Date:2018年7月4日下午7:05:53
     * @Param:
     * @Return:ResponseLoanUserList
     */
    private ResponseLoanUserTransferList changeField(ResponseLoanUserInitialList loanUserInitial,
                                                     List<FieldTransformation> fieldTransformation) {
       // LOG.info("fieldTransformation"+fieldTransformation);
        ResponseLoanUserTransferList loanUser = new ResponseLoanUserTransferList();
        String loanSex = loanUserInitial.getLoanSex();
        if ("男".equals(loanSex)) {
            loanUser.setLoanName(loanUserInitial.getLoanName().substring(0, 1) + "先生");
        } else {
            loanUser.setLoanName(loanUserInitial.getLoanName().substring(0, 1) + "女士");
        }
        loanUser.setLoanMoney(loanUserInitial.getLoanMoney());
        loanUser.setLoanTerm(loanUserInitial.getLoanTerm());
        loanUser.setOrderTime(loanUserInitial.getOrderTime());
        loanUser.setOrderNo(loanUserInitial.getOrderNo());
        loanUser.setBillStatus(loanUserInitial.getUjdStatus());
        loanUser.setDiscountedState(loanUserInitial.getDiscountedState());
        String webank = loanUserInitial.getWebank();
        //if(!webank.equals("无") && !webank.equals("暂未填写") && !webank.contains("万")){
        //loanUser.setWebank(loanUserInitial.getWebank() + "万");
        //}else{
        loanUser.setWebank(loanUserInitial.getWebank());
        //}
        loanUser.setMonthlyIncome(loanUserInitial.getMonthlyIncome());
        String companyMonthlyIncome = loanUserInitial.getCompanyMonthlyIncome();
        //if(StringUtils.isNotBlank(companyMonthlyIncome) ){
        //if(!companyMonthlyIncome.contains("万")){
        //loanUser.setCompanyMonthlyIncome(companyMonthlyIncome+"万");
        //}else{
        loanUser.setCompanyMonthlyIncome(companyMonthlyIncome);
        //}

        //}

        String loanLocation = hotCityMapper.getCityNameByCityCode(loanUserInitial.getLoanLocation());
        loanUser.setLoanLocation(loanLocation);
        if (loanUserInitial.getHouseProperty() == 9001) {
            loanUser.setHouse("无房");
        } else {
            loanUser.setHouse("有房");
        }
        if (loanUserInitial.getCarProperty() == 1301) {
            loanUser.setCar("无车");
        } else {
            loanUser.setCar("有车");
        }

        for (FieldTransformation ftf : fieldTransformation) {
            if (ftf.getOnlyCode().equals(loanUserInitial.getJob())) {
                loanUser.setJob(ftf.getValue());
            } else if (ftf.getOnlyCode().equals(loanUserInitial.getWagePaymentForm())) {
                loanUser.setLoanIncome(ftf.getValue());
            } else if (ftf.getOnlyCode().equals(loanUserInitial.getIsNative())) {
                loanUser.setIsNative(ftf.getValue());
            } else if (ftf.getOnlyCode().equals(loanUserInitial.getPersonalShebao())) {
                loanUser.setPersonalShebao(ftf.getValue());
            } else if (ftf.getOnlyCode().equals(loanUserInitial.getPersonalGongjijin())){
                loanUser.setPersonalGongjijin(ftf.getValue());
            }

        }

        return loanUser;
    }

    // 查询充值的金额与赠送的金额
    public List<PayMoney> queryRechargeMoney() {
        List<PayMoney> rechargeMoneyList = orderMapper.queryRechargeMoney();
        return rechargeMoneyList;
    }

    // 再次查询此单的价格是否正确，里面可能会对原价单进行打折，返回值有，是否是原价单，以及单子价格是否正确，1.5.0开始使用的接口
    public RequestIsOriginalPrice queryOrderPrice2(double price, long orderNo) {
        RequestIsOriginalPrice requestIsOriginalPrice = new RequestIsOriginalPrice();
        // 查询此订单的原价与订单创建的时间
        ResponseConfirmPrice priceResult = orderMapper.queryOrderPrice(orderNo);
        //打折状态 针对api设置
        int disCountState = priceResult.getDiscountedState();
        if (priceResult == null) {

            requestIsOriginalPrice.setPriceResult(false);
            return requestIsOriginalPrice;
        }
        int confirm = 0;
        int zuiXing = 0;
        int mianFei = 0;
        List<OrderTime> orderTimeList = this.queryAllStatus();
        if (orderTimeList != null && orderTimeList.size() > 0) {
            for (OrderTime orderTime : orderTimeList) {
                if (orderTime.getId() == 2) {
                    zuiXing = orderTime.getOrderTime();
                } else if (orderTime.getId() == 3) {
                    mianFei = orderTime.getOrderTime();
                }
            }
            long zuiXingMillis = zuiXing * 60L * 60 * 1000;
            long mianFeiMillis = mianFei * 60L * 60 * 1000;

            long time = System.currentTimeMillis() - priceResult.getOrderTime();
            if (time > mianFeiMillis) {
                // 5元
                confirm = 5;
                requestIsOriginalPrice.setIsOriginalPrice(0);
            } else if (disCountState == 1) {
                // 六折促销  针对API设置
                confirm = Integer
                        .parseInt(new DecimalFormat("######0").format(priceResult.getOrderAmount() * 0.6));
                requestIsOriginalPrice.setIsOriginalPrice(2);
            } else if (time > 0 && time < zuiXingMillis) {
                long now = System.currentTimeMillis();

                // 先查看有没有对最新的单子进行打折
                double isdiscount = orderMapper.isDiscountForZuiXin(now);
                if (isdiscount != 0) {
                    confirm = Integer
                            .parseInt(new DecimalFormat("######0").format(priceResult.getOrderAmount() * isdiscount));
                    requestIsOriginalPrice.setIsOriginalPrice(2); // 如果是最新单子打折
                } else {
                    // 最新
                    confirm = priceResult.getOrderAmount();

                    requestIsOriginalPrice.setIsOriginalPrice(1); // 最新单子不打折
                }
                //查看是否对限定城市进行打折
                String city = loanUserMapper.getLoanCityByOrderNo(orderNo); //单子所在城市
                HashMap<String, Object> limitCityAndDiscount = orderMapper.isDiscountForLimitCity(now);
                //Object limitCity = limitCityAndDiscount.get("limitCity");
                if (limitCityAndDiscount != null && null != limitCityAndDiscount.get("limitCity")
                        && limitCityAndDiscount.get("limitCity").toString().contains(city)) {
                    Object limitDiscount = limitCityAndDiscount.get("discount");
                    if (limitDiscount != null && (float) limitCityAndDiscount.get("discount") != 0) {
                        confirm = Integer.parseInt(new DecimalFormat("######0").format(priceResult.getOrderAmount() * (float) limitCityAndDiscount.get("discount")));
                        requestIsOriginalPrice.setIsOriginalPrice(2); // 限定城市单子打折
                    }


                }
            } else if (time > zuiXingMillis && time < mianFeiMillis) {
                // 六折促销
                confirm = Integer
                        .parseInt(new DecimalFormat("######0").format(priceResult.getOrderAmount() * 0.6));
                requestIsOriginalPrice.setIsOriginalPrice(2);
            }
        }

        if (price == confirm) {
            requestIsOriginalPrice.setPriceResult(true);
            return requestIsOriginalPrice;
        } else {
            requestIsOriginalPrice.setPriceResult(false);
            return requestIsOriginalPrice;
        }
    }

    // 根据userId查询订单
    public List<ResponseOrderList> queryOrderListByUserId(RequestOrderListByUserId orderByUserId) {
        Integer status = orderByUserId.getStatus();
        int pageSize = orderByUserId.getPageSize();
        int page = orderByUserId.getPage() * pageSize;
        String userId = orderByUserId.getUserId();
        List<ResponseOrderList> orderList = orderMapper.queryOrderListByUserId(page, pageSize, status, userId);

        for (ResponseOrderList order : orderList) {
            //把对应的城市编码转为城市
            order.setLoanLocation(hotCityMapper.getCityNameByCityCode(order.getLoanLocation()));

        }

        if (orderList != null && orderList.size() > 0) {
            return orderList;
        }
        return new ArrayList();
    }

    // 我的单子：根据userId查询订单详情
    public ResponseLoanUser queryOrderByUserId(RequestOrderByUserId orderByUserId) {
        long orderNo = orderByUserId.getOrderNo();
        String userId = orderByUserId.getUserId();
        ResponseLoanUser orderTransform = new ResponseLoanUser();
        ResponseLoanUserInitial order = orderMapper.queryOrderByUserId(orderNo, userId);
        String loanPhone = order.getLoanPhone();
        String loanPhoneAttribution = "";
        if (order == null) {
            return null;
        } else {
            // 进去单子详情时，查询手机归属地是否为空，为空的话，就调三方接口查询出归属地，并保存到数据库
            if (StringUtils.isBlank(order.getLoanPhoneAttribution())) {

                loanPhoneAttribution = phoneAttributionUtils.getPhoneAttribution(loanPhone);


                orderMapper.setPhoneAttribution(loanPhoneAttribution, orderNo);
            } else {
                loanPhoneAttribution = order.getLoanPhoneAttribution();
            }
            orderTransform.setLoanPhoneAttribution(loanPhoneAttribution);
            orderTransform.setLoanPhone(loanPhone);
            // 把城市编码转为对应的字符串传到前端
            orderTransform.setLoanLocation(hotCityMapper.getCityNameByCityCode(order.getLoanLocation()));
            orderTransform.setLoanOldcity(hotCityMapper.getCityNameByCityCode(order.getLoanOldcity()));
            if (StringUtils.isNotBlank(order.getHouseAddress())) {
                orderTransform.setHouseAddress(hotCityMapper.getCityNameByCityCode(order.getHouseAddress()));
            }
            if (StringUtils.isNotBlank(order.getCarAddress())) {
                orderTransform.setCarAddress(hotCityMapper.getCityNameByCityCode(order.getCarAddress()));
            }
            this.setFieldTransForm(order, orderTransform);


        }
        return orderTransform;
    }

    // 跟单反馈成功
    public Integer feedBackSuccess(RequestFeedBackSuccess feedBackSuccess) {
        Integer feedBackResult = orderMapper.feedBackSuccess(feedBackSuccess);
        return feedBackResult;
    }

    // 跟单失败反馈
    public Integer feedBackBad(RequestFeedBackBad feedBackBad) {
        Integer feedBackResult = orderMapper.feedBackBad(feedBackBad);
        return feedBackResult;
    }

    /**
     * @Author : jokery
     * @Description : 提交借款申请
     * @Date : 2018/2/2 9:42
     * @Param :refund
     * @Return :
     */
    public Integer addRefundApplication(RequestRefundApplication refund) {
        Long orderNo = refund.getOrderNo();
        String userId = userMapper.getUserIdByMobileNumber(refund.getMobileNumber());
        if (StringUtils.isBlank(userId)) {
            return 4;
        }
        Integer isMatch = orderMapper.queryOrderIdMatch(userId, orderNo);
        if (isMatch == null) {
            return 5;
        }

        Long orderTime = orderMapper.getOrderTimeByOrderNo(orderNo);
        //如果提交退款时间和订单创建时间相差大于7天，就不退款
        if (orderTime != null && System.currentTimeMillis() - orderTime > 7 * 24 * 60 * 60 * 1000L) {
            return 6;
        }
        // 先查询该订单是否存在,如果存在,返回价格
        Double price = this.orderMapper.queryGrabbedOrderPrice(orderNo);
        String loanPhone = loanUserMapper.queryLoanPhoneByOrderNo(orderNo);
        Double costMoney = this.orderMapper.getCostMoneyByOrderNo(orderNo);// 订单的实际花费金额，扣除优惠券之后的
        if (costMoney == null || (costMoney != null && costMoney == 0)) {
            // 不是原价单，不能申请
            return 3;
        }
        if (price == null) {
            // 没有查到价格,说明订单不存在
            return 2;
        } else {
            // 查到了价格,该订单存在,再查询该订单是否已经提交过退款申请
            Integer result = this.orderMapper.queryRefundApplicationByOrderNo(orderNo);
            if (result != null && result == 0) {
                //查询此用户本月虚假退单的次数
                Integer fakeOrder = orderMapper.getFakeOrderCount(userId);
                int status = 0;
                if (fakeOrder != null && fakeOrder >= 3) {
                    status = 2;
                }
                //查询是否在抢单异常拒绝退单时间内
                long currentTime = System.currentTimeMillis();
                Integer isLimit = orderMapper.getIsRefundLimit(userId, currentTime);
                if (isLimit != null && isLimit > 0) {
                    status = 2;
                }
                // 没有查到该单子已经退款的记录,设置退款价格并保存到数据表
                refund.setOrderPrice(costMoney);
                refund.setLoanPhone(loanPhone);
                refund.setUserId(userId);
                refund.setStatus(status);
                // 如果正常插入数据应返回1
                return this.orderMapper.insertRefundApplication(refund);
            } else {
                return 0;
            }
        }

    }

    /**
     * @Author : yax
     * @Description : 检测订单Id(退单模块) 0:没有退款记录 1:订单号不存在 2:退单已在申请中 3:已退单 4:不退款
     * @Date : 2018/5/18
     * @Param :refund
     * @Return :
     */
    public Integer orderIdCheck(String userId, String orderno) {
        Long orderNo = Long.valueOf(orderno);
        // 先查询该订单是否与用户Id匹配
        Integer count = this.orderMapper.queryOrderIdMatch(userId, orderNo);
        if (count == null || count == 0) {
            return 1;
        }
        // 查询订单退单状态
        Integer status = this.orderMapper.queryRefundStatusByOrderNo(orderNo);
        if (status == null) {
            return 0;
        } else {
            switch (status) {
                case 0:
                    return 2;
                case 1:
                    return 3;
                case 2:
                    return 4;
                default:
                    return -1;
            }
        }

    }

    /**
     * @param orderNo
     * @return
     * @Author : yax
     * @Description : 根据订单号查询退单详情
     */
    public ResponseOrderRefund queryOrderRefundByOrderNo(long orderNo) {
        return orderMapper.queryOrderRefundByOrderNo(orderNo);
    }

    public List<ResponseRefund> queryUserOrderListByUserId(String userId, int pageSize, int page) {
        return orderMapper.queryUserOrderListByUserId(userId, pageSize, (page - 1) * pageSize);
    }

    /**
     * @Author:Tina
     * @Description:根据订单号查询此单的花费的实际金额
     * @Date:2018年6月7日上午9:45:56
     * @Param:
     * @Return:Double
     */
    public Double getCostMoneyByOrderNo(long orderNo) {
        return orderMapper.getCostMoneyByOrderNo(orderNo);

    }

    // 对传过来的城市列表进行处理
    public ArrayList<String> setCityTransform(ArrayList<String> cities) {
        ArrayList<String> citiesTransform = new ArrayList<String>();
        String cityTransform = "";
        if (cities == null || cities.size() == 0) {
            citiesTransform.add("1");
        } else {
            for (String cityCode : cities) {
                // 前端定位的城市获取的编码是计算到县区的，这里要把区编码转成对应的二级城市编码
                cityTransform = hotCityMapper.getCityCodeTransform(cityCode);
                // 如果搜出的二级城市编码是空，说明传过来的就已经是二级城市编码，就直接存到列表里
                if (StringUtils.isBlank(cityTransform)) {
                    citiesTransform.add(cityCode);
                } else {
                    citiesTransform.add(cityTransform);
                }
            }
        }
        return citiesTransform;
    }

    /**
     * @Author:Tina
     * @Description:把前端接收来的订单筛选list转换一下
     * @Date:2018年5月19日下午2:15:49
     * @Param:
     * @Return:void
     */
    private RequestOrdersByManyConditions setOrderChoice(RequestOrdersByManyConditions conditions) {
        ArrayList<Integer> condition = conditions.getConditions();
        if (condition != null && condition.size() > 0) {

            for (int i : condition) {
                if (i == 0) {
                    conditions.setIsChooseType(2); // 传过来的数据只有一个0的话，就是没有传订单类型，isChooseType设为2
                    condition.clear();
                    conditions.setConditions(condition);
                    break;
                } else {
                    conditions.setIsChooseType(1); // 选择订单类型的话，isChooseType设为1
                    if (i == 1) {
                        //埋点统计订单类型
                        functionCountService.addFunctionCountForApp("订单_打卡贷");
                        conditions.setDaKaDai(1);
                        continue;
                    }
                    if (i == 2) {
                        functionCountService.addFunctionCountForApp("订单_社保贷");
                        conditions.setSheBaoDai(1);
                        continue;
                    }
                    if (i == 3) {
                        functionCountService.addFunctionCountForApp("订单_企业贷");
                        conditions.setQiYeDai(1);
                        continue;
                    }
                    if (i == 4) {
                        functionCountService.addFunctionCountForApp("订单_证件贷");
                        conditions.setZhengJianDai(1);
                        continue;
                    }
                    if (i == 5) {
                        functionCountService.addFunctionCountForApp("订单_优房贷");
                        conditions.setYouFangDai(1);
                        continue;
                    }
                    if (i == 6) {
                        functionCountService.addFunctionCountForApp("订单_优车贷");
                        conditions.setYouCheDai(1);
                        continue;
                    }
                    if (i == 7) {
                        functionCountService.addFunctionCountForApp("订单_微粒贷");
                        conditions.setWeiLiDai(1);
                        continue;
                    }
                    if (i == 8) {
                        functionCountService.addFunctionCountForApp("订单_保单贷");
                        conditions.setBaoDanDai(1);
                        continue;
                    }
                }

            }
        } else {
            conditions.setIsChooseType(2); // 没有传订单类型，isChooseType设为2
        }
        return conditions;

    }

    // 挂标签用的,并在此判断是否要对最新的单子进行打折
    public ResponseLoanUserTransferList changeTagsNew(ResponseLoanUserTransferList loanUser, long zuiXingMillis,
                                                      long mianFeiMillis) {

        long time = System.currentTimeMillis() - loanUser.getOrderTime();
        Integer disCountState = loanUser.getDiscountedState();
        if (time > mianFeiMillis) {
            // 5元抢单
            loanUser.setPriceStatus(3);
        } else if (disCountState == 1) {//api设置
            // 6折促销
            loanUser.setPriceStatus(2);
        } else if (time > 0 && time < zuiXingMillis) {
            // 最新
            loanUser.setPriceStatus(1);
        } else if (time > zuiXingMillis && time < mianFeiMillis) {
            // 6折促销
            loanUser.setPriceStatus(2);
        }

        return loanUser;
    }

    // 抢单的查询订单详情
    public ResponseLoanUser queryOrderByOrderNo(RequestOrderByUserId requestOrderByUserId) {
        // 用户浏览单子送积分
        this.scanInfoForCredit(requestOrderByUserId);

        long orderNo = requestOrderByUserId.getOrderNo();
        String userId = requestOrderByUserId.getUserId();

        ResponseLoanUser loanUserTransform = new ResponseLoanUser();
        ResponseLoanUserInitial loanUser = orderMapper.queryOrderByOrderNo(orderNo);
        // 把城市编码转为对应的字符串传到前端
        loanUserTransform.setLoanLocation(hotCityMapper.getCityNameByCityCode(loanUser.getLoanLocation()));

        loanUserTransform.setLoanOldcity(hotCityMapper.getCityNameByCityCode(loanUser.getLoanOldcity()));
        if (StringUtils.isNotBlank(loanUser.getHouseAddress())) {
            loanUserTransform.setHouseAddress(hotCityMapper.getCityNameByCityCode(loanUser.getHouseAddress()));
        }
        if (StringUtils.isNotBlank(loanUser.getCarAddress())) {
            loanUserTransform.setCarAddress(hotCityMapper.getCityNameByCityCode(loanUser.getCarAddress()));
        }
        String loanPhone = "";
        String loanPhoneAttribution = loanUser.getLoanPhoneAttribution();
        if (loanUser == null) {
            return null;
        } else {

            if (loanUser.getLoanPhone() != null) {
                // 进去单子详情时，查询手机归属地是否为空，为空的话，就调三方接口查询出归属地，并保存到数据库
                if (StringUtils.isBlank(loanPhoneAttribution)) {
                    loanPhone = loanUser.getLoanPhone();
                    loanPhoneAttribution = phoneAttributionUtils.getPhoneAttribution(loanPhone);
                    orderMapper.setPhoneAttribution(loanPhoneAttribution, orderNo);
                }
                // loanUser.setLoanPhone(loanUser.getLoanPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                //loanUserTransform.setLoanPhone(loanUser.getLoanPhone().replaceAll("(\\d{3})\\d{8}", "$1********"));
                loanUserTransform.setLoanPhone("1**********");
                loanUserTransform.setLoanPhoneAttribution(loanPhoneAttribution);
            }
            // 返回姓名
            String loanSex = loanUser.getLoanSex();
            if (loanSex.equals("男")) {
                loanUserTransform.setLoanName(loanUser.getLoanName().substring(0, 1) + "先生");
            } else {
                loanUserTransform.setLoanName(loanUser.getLoanName().substring(0, 1) + "女士");
            }

            // 查询订单价格
            this.setOrderPrice(loanUser, loanUserTransform);

            // 对选出的订单字段进行转换
            this.setFieldTransForm(loanUser, loanUserTransform);

            return loanUserTransform;
        }
    }

    /**
     * @Author:Tina
     * @Description:把数据库取出的编号转为对应的字符串
     * @Date:2018年7月12日下午1:47:19
     * @Param:
     * @Return:void
     */
    private void setFieldTransForm(ResponseLoanUserInitial loanUser, ResponseLoanUser loanUserTransform) {
        loanUserTransform.setLoanMoney(loanUser.getLoanMoney());
        loanUserTransform.setLoanTerm(loanUser.getLoanTerm());
        loanUserTransform.setLoanAge(loanUser.getLoanAge());
        loanUserTransform.setLoanMarried(loanUserMapper.getValueByCode(loanUser.getLoanMarried()));
        loanUserTransform.setOrderTime(loanUser.getOrderTime());
        loanUserTransform.setOrderNo(loanUser.getOrderNo());
        Integer job = loanUser.getJob();
        // 职业信息
        if (job != null) {
            if (job == 2001) {
                loanUserTransform.setMonthlyIncome(loanUser.getMonthlyIncome());
                loanUserTransform.setWorkYear(loanUserMapper.getValueByCode(loanUser.getWorkYear()));
                loanUserTransform.setCompanyType(loanUserMapper.getValueByCode(loanUser.getCompanyType()));
                loanUserTransform.setWagePaymentForm(loanUserMapper.getValueByCode(loanUser.getWagePaymentForm()));

            } else if (job == 2002) {
                loanUserTransform.setMonthlyIncome(loanUser.getMonthlyIncome());
                loanUserTransform.setWagePaymentForm(loanUserMapper.getValueByCode(loanUser.getWagePaymentForm()));
            } else if (job == 2003) {
                loanUserTransform.setRegisterTime(loanUserMapper.getValueByCode(loanUser.getRegisterTime()));
                String companyMonthlyIncome = loanUser.getCompanyMonthlyIncome();
                loanUserTransform.setCompanyMonthlyIncome(companyMonthlyIncome);
            }
            loanUserTransform.setJob(loanUserMapper.getValueByCode(loanUser.getJob()));
            loanUserTransform.setPersonalGongjijin(loanUserMapper.getValueByCode(loanUser.getPersonalGongjijin()));
            loanUserTransform.setPersonalShebao(loanUserMapper.getValueByCode(loanUser.getPersonalShebao()));

        }
        // 资产信息
		/*Integer personalCar = loanUser.getPersonalCar();
		if(personalCar != null){
			if (!loanUser.getPersonalCar().equals(1404)) {
				Integer carPledge = loanUser.getCarPledge();
				if ("是".equals(loanUserMapper.getValueByCode(carPledge))) {
					loanUserTransform.setCar(loanUserMapper.getValueByCode(personalCar) + "("
							+ loanUserTransform.getCarAddress() + ")" + "接受抵押");
				} else if ("否".equals(loanUserMapper.getValueByCode(carPledge))) {
					loanUserTransform.setCar(loanUserMapper.getValueByCode(personalCar) + "("
							+ loanUserTransform.getCarAddress() + ")" + "不接受抵押");
				} else {
					loanUserTransform.setCar(loanUserMapper.getValueByCode(personalCar));
				}
				loanUserTransform.setCarAssessment(loanUserMapper.getValueByCode(loanUser.getCarAssessment()));
			}else{
				loanUserTransform.setCar("有");
			}
		}else{
			loanUserTransform.setCar("无");
		}*/
        Integer carProperty = loanUser.getCarProperty();
        Integer personalCar = loanUser.getPersonalCar();
        Integer carAssessment = loanUser.getCarAssessment();
        if (carProperty != null) {
            loanUserTransform.setCarProperty(loanUserMapper.getValueByCode(loanUser.getCarProperty()));
        }
        if (personalCar != null) {
            loanUserTransform.setCar(loanUserMapper.getValueByCode(loanUser.getPersonalCar()));
        }
        if (carAssessment != null) {
            loanUserTransform.setCarAssessment(loanUserMapper.getValueByCode(loanUser.getCarAssessment()));
        }
		/*Integer personalHouse = loanUser.getPersonalHouse();
		if(personalHouse != null){
			if (!loanUser.getPersonalHouse().equals(1104)) {
				Integer housePledge = loanUser.getHousePledge();
				if ("是".equals(loanUserMapper.getValueByCode(housePledge))) {
					loanUserTransform.setHouse(loanUserMapper.getValueByCode(personalHouse) + "("
							+ loanUserTransform.getHouseAddress() + ")" + "接受抵押");
				} else if ("否".equals(loanUserMapper.getValueByCode(housePledge))) {
					loanUserTransform.setHouse(loanUserMapper.getValueByCode(personalHouse) + "("
							+ loanUserTransform.getHouseAddress() + ")" + "不接受抵押");
				} else {
					loanUserTransform.setHouse(loanUserMapper.getValueByCode(personalHouse));
				}
				loanUserTransform.setHouseAssessment(loanUserMapper.getValueByCode(loanUser.getHouseAssessment()));
			}else{
				loanUserTransform.setHouse("有");
			}
		}else{
			loanUserTransform.setHouse("无");
		}*/
        Integer houseProperty = loanUser.getHouseProperty();
        Integer personalHouse = loanUser.getPersonalHouse();
        Integer houseAssessment = loanUser.getHouseAssessment();
        if (houseProperty != null) {
            loanUserTransform.setHouseProperty(loanUserMapper.getValueByCode(loanUser.getHouseProperty()));
        }
        if (personalHouse != null) {
            loanUserTransform.setHouse(loanUserMapper.getValueByCode(loanUser.getPersonalHouse()));
        }
        if (houseAssessment != null) {
            loanUserTransform.setHouseAssessment(loanUserMapper.getValueByCode(loanUser.getHouseAssessment()));
        }


        // 负债
        loanUserTransform.setLoanDebt(loanUser.getLoanDebt());

        //借呗
        if (loanUser.getJiebei() != null) {
            loanUserTransform.setJiebei(loanUserMapper.getValueByCode(loanUser.getJiebei()));
        } else {
            loanUserTransform.setJiebei("暂未填写");
        }
        //学历
        loanUserTransform.setDegree(loanUserMapper.getValueByCode(loanUser.getDegree()));
        // 保单
        loanUserTransform.setLoanChit(loanUserMapper.getValueByCode(loanUser.getLoanChit()));
        if (loanUser.getChitSituation() != null) {
            loanUserTransform.setChitSituation(loanUserMapper.getValueByCode(loanUser.getChitSituation()));
        }
        // 信用卡使用状况
        loanUserTransform.setCreditCardStatus(loanUserMapper.getValueByCode(loanUser.getCreditCardStatus()));
        // 微粒贷
        String webank = loanUser.getWebank();
        //if(!webank.equals("无") && !webank.equals("暂未填写") && !webank.contains("万")){
        //loanUserTransform.setWebank(loanUser.getWebank() + "万");
        //}else{
        loanUserTransform.setWebank(loanUser.getWebank());
        //}
        // 芝麻信用
        loanUserTransform.setZmxy(loanUser.getZmxy());
        // 单子状态：0未被抢,1已被抢；或者是跟单状态：0表示跟单中1表示已完成2表示跟单失败
        loanUserTransform.setStatus(loanUser.getStatus());
        //补充说明
        String finalTags = "";
        String initialTags = loanUser.getTags();
        if (StringUtils.isNotBlank(initialTags) && initialTags.startsWith("220")) {
            String[] tags = initialTags.split(",");
            for (String tag : tags) {
                finalTags += loanUserMapper.getValueByCode(Integer.parseInt(tag.trim())) + ",";

            }
            loanUserTransform.setTags(finalTags.substring(0, finalTags.length() - 1));
        } else {
            loanUserTransform.setTags(initialTags);
        }
    }

    /**
     * @Author:Tina
     * @Description:查询订单价格
     * @Date:2018年7月12日下午1:42:14
     * @Param:
     * @Return:void
     */
    private void setOrderPrice(ResponseLoanUserInitial loanUser, ResponseLoanUser loanUserTransform) {
        int zuiXing = 0;
        int mianFei = 0;

        List<OrderTime> orderTimeList = this.queryAllStatus();
        if (orderTimeList != null && orderTimeList.size() > 0) {
            for (OrderTime orderTime : orderTimeList) {
                if (orderTime.getId() == 2) {
                    zuiXing = orderTime.getOrderTime();
                } else if (orderTime.getId() == 3) {
                    mianFei = orderTime.getOrderTime();
                }
            }
            long zuiXingMillis = zuiXing * 60L * 60 * 1000;
            long mianFeiMillis = mianFei * 60L * 60 * 1000;

            long time = System.currentTimeMillis() - loanUser.getOrderTime();
            double orderAmount = loanUser.getOrderAmount();
            loanUserTransform.setOrderAmount(orderAmount);
            int disCountState = loanUser.getDiscountedState();
            if (time > mianFeiMillis) {
                // 5元
                loanUserTransform.setPrice(5.0);
            } else if (disCountState == 1) {//针对API设置打折
                // 六折促销
                loanUserTransform.setPrice(Integer.parseInt(new DecimalFormat("######0").format(orderAmount * 0.6)));
            } else if (time > 0 && time < zuiXingMillis) {
                long now = System.currentTimeMillis();
                // 表示该单子处于最新发布,查看是否对该单子进行打折
                double discount = orderMapper.isDiscountForZuiXin(now);
                if (discount != 0) {
                    loanUserTransform
                            .setPrice(Integer.parseInt(new DecimalFormat("######0").format(orderAmount * discount)));
                } else {
                    loanUserTransform.setPrice(orderAmount);
                }
                //查看是否对限定城市进行打折
                HashMap<String, Object> limitCityAndDiscount = orderMapper.isDiscountForLimitCity(now);
                //Object limitCity = limitCityAndDiscount.get("limitCity");
                if (limitCityAndDiscount != null && null != limitCityAndDiscount.get("limitCity") && limitCityAndDiscount.get("limitCity").toString().contains(loanUser.getLoanLocation())) {
                    Object limitDiscount = limitCityAndDiscount.get("discount");
                    if (limitDiscount != null && (float) limitCityAndDiscount.get("discount") != 0) {
                        loanUserTransform
                                .setPrice(Integer.parseInt(new DecimalFormat("######0").format(orderAmount * (float) limitCityAndDiscount.get("discount"))));
                    }


                }

            } else if (time > zuiXingMillis && time < mianFeiMillis) {
                // 六折促销
                loanUserTransform.setPrice(Integer.parseInt(new DecimalFormat("######0").format(orderAmount * 0.6)));
            }
        }

    }

    /**
     * @Author:Tina
     * @Description:浏览单子加积分
     * @Date:2018年7月12日下午1:33:41
     * @Param:
     * @Return:void
     */
    private void scanInfoForCredit(RequestOrderByUserId requestOrderByUserId) {
        long orderNo = requestOrderByUserId.getOrderNo();
        String userId = requestOrderByUserId.getUserId();
        if (userId != null) {
            // 使用redis来实现 浏览单子：每单1积分，每日上限15积分
            // 设置key，value
            String reKey = "userClickOrder_" + userId + "_" + DateUtil.getDay();
            List revalue = new ArrayList();
            long dayMis = 0;
            try {
                RequestCreditDetail requestCreditDetail = new RequestCreditDetail();
                requestCreditDetail.setUserId(userId);
                requestCreditDetail.setType(2);
                requestCreditDetail.setCreditValue(1);
                requestCreditDetail.setCreditPathway("浏览单子");
                requestCreditDetail.setCreditDetail("浏览单子");
                // 获取当前时间与当天最后时间相差的毫秒值
                dayMis = sdfDay.parse(DateUtil.getDay()).getTime() + 1000 * 60 * 60 * 24 - 1
                        - System.currentTimeMillis();
                // 查看今天有没有此人是否点过首页的订单
                if (redisCacheManager.hasKey(reKey)) { // 如果点过
                    revalue = (List) redisCacheManager.get(reKey);
                    if (!revalue.contains(orderNo) && revalue.size() < 15) {
                        // 如果是第一次点击该订单，并且点击的订单在15以内，就给用户加1积分
                        creditMapper.addCreditDetail(requestCreditDetail);

                        // 先查积分总表里是否存在，不存在，先插入一条数据，存在的话，就更新
                        Integer isGetCredit = creditMapper.queryIsEverGetCredit(userId);
                        if (isGetCredit != null) {
                            // 更新用户积分
                            creditMapper.updateCreditWallet(userId, 1);
                        } else {
                            // 插入一条新的记录
                            creditMapper.addCreditWallet(userId, 1);
                        }

                        // 把此订单号加到value里
                        revalue.add(orderNo);
                        redisCacheManager.set(reKey, revalue, dayMis / 1000);
                    }
                } else { // 没有点击过首页的订单
                    // 为用户加1积分
                    creditMapper.addCreditDetail(requestCreditDetail);
                    // 先查积分总表里是否存在，不存在，先插入一条数据，存在的话，就更新
                    Integer isGetCredit = creditMapper.queryIsEverGetCredit(userId);
                    if (isGetCredit != null) {
                        // 更新用户积分
                        creditMapper.updateCreditWallet(userId, 1);
                    } else {
                        // 插入一条新的记录
                        creditMapper.addCreditWallet(userId, 1);
                    }

                    revalue.add(orderNo);
                    // 设置此人当天的key，value
                    redisCacheManager.set(reKey, revalue, dayMis / 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //查询用户是否由于抢单太频繁被加入黑名单
    public long queryIsAllowToGrabOrder(String userId, String userName, long orderNo) {

        long currentTime = System.currentTimeMillis();
        Long result = userMapper.isGrabDisabledByUserId(userId, currentTime);
        if (result != null) {
            return result;
        } else {
			/*ResponseGrabLimit responseGrabLimit = userMapper.queryGrabLimitPram();
			int flag = 0;
			String reason = "";
			// 对用户抢单进行计数，如果1小时抢了N 次,限制用户不能抢单
			String countKey = "userGrabCount_" + userId;
			int countValue = 1;
			if (!redisCacheManager.hasKey(countKey)) {
				redisCacheManager.set(countKey, countValue, 60 * 60);// 有效时间为 1 小时
			} else {
				countValue = (int) redisCacheManager.get(countKey)+1;
				if (countValue < responseGrabLimit.getLimitHourCount()) {
					redisCacheManager.set(countKey, countValue , redisCacheManager.getExpire(countKey));
				} else {
					flag = 1;
					reason = "1小时抢" + responseGrabLimit.getLimitHourCount() + "单";
					redisCacheManager.set(countKey, 0, redisCacheManager.getExpire(countKey));// 有效时间为 1 小时
				}
			}

			//或者，抢单间隔连续小于等于x秒且大于等于m单
			if(flag == 0){
				//查看上次抢单的时间
				//Long lastGrabTime = orderMapper.getLastGrabTime(userId);
				//查看单子的生成时间
				Long orderTime = orderMapper.getOrderTimeByOrderNo(orderNo);
				if(orderTime != null ){
					String timeIntervalKey = "userGrabTimeInterval_"+userId;
					//如果订单生成时间小于指定的值，就继续计数，否则重新计数
					if(currentTime - orderTime <= responseGrabLimit.getLimitSecond()*1000){

						if(!redisCacheManager.hasKey(timeIntervalKey)){
							//如果上次抢单间隔没有在指定值内
							redisCacheManager.set(timeIntervalKey, 0, 60 * 60);
						}else{
							int timeIntervalCount = (int) redisCacheManager.get(timeIntervalKey)+1;
							if(timeIntervalCount < responseGrabLimit.getLimitSecondCount()){
								redisCacheManager.set(timeIntervalKey, timeIntervalCount, 60 * 60);
							}else{
								flag = 1;
								reason = "连续" + responseGrabLimit.getLimitSecondCount() + "单抢单间隔小于"+responseGrabLimit.getLimitSecond()+"秒";
								redisCacheManager.set(timeIntervalKey, 0, 60 * 60);
							}
						}
					}else{
						redisCacheManager.set(timeIntervalKey, 0, 60 * 60);
					}

				}
			}

			//或者 当日内，抢不同城市的单子，当抢第6个城市的单子时触发
			if(flag == 0){
				String city = loanUserMapper.getLoanCityByOrderNo(orderNo);
				if(StringUtils.isNotBlank(city)){
					// 获取当前时间与当天最后时间相差的毫秒值
					long dayMis = 0;
					try {
						dayMis = sdfDay.parse(DateUtil.getDay()).getTime() + 1000 * 60 * 60 * 24 - 1
								- System.currentTimeMillis();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					HashSet<String> cityList = new HashSet<String>();
					String grabOtherCityKey = "grabOtherCity_"+userId;
					if(!redisCacheManager.hasKey(grabOtherCityKey)){
						cityList.add(city);
						redisCacheManager.set(grabOtherCityKey,cityList,dayMis/1000);
					}else{
						cityList = (HashSet<String>) redisCacheManager.get(grabOtherCityKey);
						cityList.add(city);
						if(cityList.size() > responseGrabLimit.getLimitCity()-2){
							redisCacheManager.set(grabOtherCityKey,new HashSet<String>(),redisCacheManager.getExpire(grabOtherCityKey));
							flag = 1;
							reason = "当日抢第"+responseGrabLimit.getLimitCity()+"个城市的单子";
						}else{
							redisCacheManager.set(grabOtherCityKey,cityList,redisCacheManager.getExpire(grabOtherCityKey));
						}

					}

				}
			}


			if(flag == 1){
				long endTime = System.currentTimeMillis()+responseGrabLimit.getLimitHour()*60L*60*1000;
				//限制此用户抢单
				Integer limitResult = userMapper.limitUserGrabOrder(userId,reason,System.currentTimeMillis(),endTime);
				if(limitResult != null && limitResult == 1){
					//给内部人员发短信
					String appName = "优接单";
					String phone = userMapper.getPhoneNumberByUserId(userId);
					long tpl_id = 2385218;
					String tpl_value = "";
					try {
						tpl_value = URLEncoder.encode("#name#", ENCODING) + "=" + URLEncoder.encode(userName, ENCODING)
								+ "&" + URLEncoder.encode("#phone#", ENCODING) + "="
								+ URLEncoder.encode(phone, ENCODING);

						Map<String, String> params = new HashMap<String, String>();
						params.put("apikey", "4e2bc3e7d93e529e99a25cf43f8a5a90");
						params.put("tpl_id", String.valueOf(tpl_id));
						params.put("tpl_value", tpl_value);
						// 分别对应 开发，老板，运营的手机号
						params.put("mobile", "18896562930,18551220708,18896561077,15950026664,18351615340,15651518503");

						CloseableHttpClient client = HttpClients.createDefault();
						String responseText = "";
						CloseableHttpResponse response = null;
						HttpPost method = new HttpPost("https://sms.yunpian.com/v2/sms/tpl_batch_send.json");
						if (params != null) {
							List<NameValuePair> paramList = new ArrayList<NameValuePair>();
							for (Map.Entry<String, String> param : params.entrySet()) {
								NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
								paramList.add(pair);
							}
							method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
						}
						response = client.execute(method);
						HttpEntity entity = response.getEntity();
						if (entity != null) {
							responseText = EntityUtils.toString(entity, ENCODING);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}*/
            return 0;
        }
    }

    public HashMap<String, Integer> getLotteryChance(String userId, String today) {
        return orderMapper.getLotteryChance(userId, today);
    }

    public String getPhoneByMd5(String channel, String md5, long orderNo, Double buyMoney, String channelBranch, String userId, String buyTime) {
        String url = orderMapper.queryUrlByChannel(channel);
        String phone = null;
        if (url != null) {
            String response = "";
            if ("anxinhua".equals(channel)) {
                response = HttpUtil2.get(url + "?callbackKey=80ae035566e5dcbd&md5=" + md5 + "&buyTime=" + buyTime.substring(0, 10) + "&buyMoney=" + buyMoney);
            } else {
                Map<String, Object> param = new HashMap<>();
                param.put("callbackKey", "80ae035566e5dcbd");
                param.put("md5", md5);
                param.put("buyTime", buyTime);
                param.put("buyMoney", buyMoney);
                response = HttpUtil2.post(url, param);
            }
           // LOG.info(response);
            JSONObject json = JSONObject.parseObject(response);
            if (json != null && json.getInteger("code") == 100) {
                phone = json.getString("phone");
                String message = json.getString("msg");
                if (phone != null) {
                    orderMapper.updatePhoneByOrderNo(phone, orderNo);
                    feedBackToChannelsMapper.log(phone, buyMoney, buyTime, message,
                            channel, channelBranch, orderNo, userId);
                }
            }
        }
        return phone;
    }

    //新接口回调返回的数据
    public Map<String, Object> getMap(String channel, String mobile, long orderNo, Double buyMoney, String channelBranch, String userId, String buyTime) {
        String url = orderMapper.queryUrlByOrderNo(orderNo);
        String apiId = loanUserMapper.queryApiIdByOrderNo(orderNo);
        Map<String, Object> param = new HashMap<>();
        param.put("apiId", apiId);
        param.put("orderNo", orderNo);
        param.put("phone", mobile);
        param.put("orderTime", buyTime);
        param.put("price", buyMoney);
        param.put("url", url);
        return param;
    }

    public String getPhoneByApi(long orderNo, String mobile) {
        FormBody formBody = new FormBody.Builder()
                .add("orderNo", String.valueOf(orderNo))
                .build();
        String phone = mobile;
        String response = HttpUtil2.post("https://loan.stardai.com/api/loan/getPhoneForUJD", formBody);
        if (isNumeric(response)) {
            phone = response;
            orderMapper.updatePhoneByOrderNo(phone, orderNo);
        }
        return phone;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");


        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
