package com.stardai.manage.service;

import com.google.common.collect.Maps;
import com.stardai.manage.mapper.HotCityMapper;
import com.stardai.manage.mapper.LoanUserMapper;
import com.stardai.manage.request.RequestCreditScore;
import com.stardai.manage.request.RequestLoanUserInfo;
import com.stardai.manage.response.ResponseLoanOrder;
import com.stardai.manage.utils.IdWorker;
import com.stardai.manage.utils.IsNativeUtils;
import com.stardai.manage.utils.JiguangPush;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author yutenger
 * @date 2019/03/29
 */
@Service
@SuppressWarnings("all")
public class LoanUserServiceNew {

    @Autowired
    private LoanUserMapper loanUserMapper;

    @Autowired
    private HotCityMapper hotCityMapper;
    protected static final Logger LOG = LoggerFactory.getLogger(LoanUserServiceNew.class);
    private IdWorker idWorker = new IdWorker(0, 8);

    // 将传过来的借款人信息保存到优接单的数据库(新接口调用)
    public long insertLoanInfoNew(RequestLoanUserInfo loanUserInfo) {
        specialValueConversion(loanUserInfo);
        int amount = loanUserInfo.getAmount();
        String api_url = loanUserInfo.getApiUrl();
        String phoneMD5 = loanUserInfo.getPhoneMD5();
        String phone = loanUserInfo.getPhone();
        if (phone == null) {
            loanUserInfo.setPhone(phoneMD5);
        }
        int amountInterval = 0;
        //计算贷款金额所属空间，用作推送
        if (amount > 30) {
            amountInterval = 2204;
        } else if (amount > 10) {
            amountInterval = 2203;
        } else if (amount > 5) {
            amountInterval = 2202;
        } else {
            amountInterval = 2201;
        }

        loanUserInfo.setAmount(amount * 10000);
        // 判断借款人是不是本地人
        String location = loanUserInfo.getLocation();
        String household = loanUserInfo.getHousehold();
        Integer isNative;

        if (location.equals(household)) {
            // 本地户籍
            isNative = 2301;
        } else {
            // 非本地户籍
            isNative = 2302;
        }
        loanUserInfo.setIsNative(isNative);

        //查询用户是否符合资质
        loanUserInfo = this.isQualified(loanUserInfo);
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
        String webank = loanUserInfo.getWebank();
        if (!"无".equals(webank) && !"".equals(webank)) {
            webank = "2101";
        } else {
            webank = "2102";
        }


        loanUserMapper.insertLoanInfoNew(loanUserInfo);


        if (loanUserInfo.getUjdStatus() == 0) {
            // 先生成推送要显示的消息
            String message = this.createMessage(loanUserInfo, location);
            // 给关注这个城市的信贷经理发推送
            JiguangPush.jPushOrders(location, message, orderNo, amountInterval, webank, isNative);
        }

        return orderNo;
    }

    //字段值-1,-2 过滤(临时处理)
    public void specialValueConversion(Object loanUser) {
        try {
            Class clz = loanUser.getClass();
            Field[] fields = clz.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Object value = f.get(loanUser);
                String name = f.getName();
                if (!"channelBranch".equals(name) && value != null) {
                    if ("-1".equals(value) || "-2".equals(value) || value.equals(-1) || value.equals(-2)) {
                        f.set(loanUser, null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.info(e.getMessage());
        }
    }

    //评估单子的资质
    private RequestLoanUserInfo isQualified(RequestLoanUserInfo loanUserInfo) {


        String idCard = loanUserInfo.getIdCard();
        Integer amount = loanUserInfo.getAmount();
        String city = loanUserInfo.getLocation();
        Integer isHigh = 1;
        Integer age = loanUserInfo.getAge();
        if (age < 22 || age > 55) {
            isHigh = 2;
        }
		/*if(amount==10000){
			isHigh=2;
		}*/
        //查询是否属于接单地区
        Integer count = loanUserMapper.queryIsInCity(city);
        if (count == 0) {
            isHigh = 2;
        }
        boolean flag = false;//10选1不满足
        Integer workYear = loanUserInfo.getWorkYear();
        Integer income = loanUserInfo.getIncome();
        if (workYear != null && workYear == 3 && income != null && (income == 1 || income == 2)) {
            flag = true;
        }
        Integer sheBao = loanUserInfo.getSheBao();
        if (sheBao != null && sheBao == 4) {
            flag = true;
        }
        Integer gongJiJin = loanUserInfo.getGongJiJin();
        if (gongJiJin != null && gongJiJin == 4) {
            flag = true;
        }
        Integer registerTime = loanUserInfo.getRegisterTime();
        if (registerTime != null && registerTime > 1) {
            flag = true;
        }
        Integer houseProperty = loanUserInfo.getHouseProperty();
        if (houseProperty != null && houseProperty > 1) {
            flag = true;
        }
        Integer carProperty = loanUserInfo.getCarProperty();
        if (carProperty != null && carProperty > 1) {
            flag = true;
        }
        String webank = loanUserInfo.getWebank();
        if (null != webank && !"".equals(webank)) {
            flag = true;
        }
        Integer chit = loanUserInfo.getChit();
        if (chit != null && chit == 2) {
            flag = true;
        }
        Integer status = loanUserInfo.getStatus();
        if (status != null && (status == 2 || status == 3)) {
            flag = true;
        }
        //本地户籍
        if (loanUserInfo.getIsNative() == 2301) {
            flag = true;
        }
        if (!flag) {
            isHigh = 2;
        }
        loanUserInfo.setConformQualification(isHigh);
        return loanUserInfo;
    }

    private String queryOldCity(String idCard) {
        //根据身份证号前几位获取此人所在的户籍城市
        String cityCode = loanUserMapper.getCityCodeByIdCard(idCard.substring(0, 6));
        String oldCity = "";
        String cityName = "";
        if (StringUtils.isBlank(cityCode)) {
            oldCity = IsNativeUtils.isNative(idCard);
            if ("身份证号非法".equals(oldCity)) {
                cityCode = "000000";
            } else {
                cityName = this.unifyLocations(oldCity);
                //根据城市名称获取城市编码
                cityCode = hotCityMapper.getCityCodeByCityName(cityName);
                if (StringUtils.isBlank(cityCode)) {
                    cityCode = "000000";
                }
            }
            if (!"000000".equals(cityCode)) {
                //把没存在数据库里的身份证号前几位存起来
                loanUserMapper.setCardNoAndCityCode(idCard.substring(0, 6), cityCode, cityName);
            }
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
            // 如果申请时间在期限之内，把ujdStatus设为3,信贷经理端不显示
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
        String city = hotCityMapper.getCityNameByCityCode(location) + ",";
        String job = loanUserMapper.getValueByCode(loanUserInfo.getJob()) + ",";
        String loanAmount = "贷款" + loanUserInfo.getAmount() / 10000 + "万元,";
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
        List<RequestCreditScore> result = loanUserMapper.queryCreditScoreNew();
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

                Integer onlyCode = (Integer) loanUserInfoMap.get(field);
                if (onlyCode != null && r.getOnlyCode().equals(onlyCode)) {
                    score += r.getScore();
                }
            }
        }
        // 有无负债是用户填的数字,比较灵活,单独拿出来判断
        if (loanUserInfo.getDebt() != null && loanUserInfo.getDebt() > 0) {

            score += isDebt;
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
        int price2 = loanUserInfo.getAmount() / 30000;
        price2 = price2 == 0 ? 1 : price2;
        // 根据借款期限算价格组成部分3,最低1分
        int price3 = loanUserInfo.getTerm() / 12;
        price3 = price3 == 0 ? 1 : price3;
        // 算出最终价格
        price = price1 + price2 + price3;
        price = price > 50 ? 50 : price;
        int conformQualification = loanUserInfo.getConformQualification();
        if (conformQualification == 2) {
            //如果是差资质单子，查询给差资质单子打的折扣，并对当前单子打折
            Double discount = loanUserMapper.getDiscountForLowQualification(System.currentTimeMillis());
            if (discount != null && discount != 0) {
                loanUserInfo.setPrice(new Double(price * discount).intValue());
            } else {
                loanUserInfo.setPrice(price);
            }
        } else {
            loanUserInfo.setPrice(price);
        }
        loanUserInfo.setScore(score);

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
        // 职业
        if (loanUserInfo.getJob() == 1) {
            loanUserInfo.setJob(loanUserInfo.getJob() + 2000);
        } else {
            loanUserInfo.setJob(loanUserInfo.getJob() + 2001);
        }
        // 单位性质
        if (loanUserInfo.getCompanyType() != null && loanUserInfo.getCompanyType() != 0) {
            loanUserInfo.setCompanyType(loanUserInfo.getCompanyType() + 3000);
        } else {
            loanUserInfo.setCompanyType(null);
        }
        // 工资发放形式
        if (loanUserInfo.getIncome() != null && loanUserInfo.getIncome() != 0) {
            loanUserInfo.setIncome(loanUserInfo.getIncome() + 4000);
        } else {
            loanUserInfo.setIncome(null);
        }
        // 工作年限
        if (loanUserInfo.getWorkYear() != null && loanUserInfo.getWorkYear() != 0) {
            loanUserInfo.setWorkYear(loanUserInfo.getWorkYear() + 5000);
        } else {
            loanUserInfo.setWorkYear(null);
        }
        // 企业注册时间
        if (loanUserInfo.getRegisterTime() != null && loanUserInfo.getRegisterTime() != 0) {
            loanUserInfo.setRegisterTime(loanUserInfo.getRegisterTime() + 6000);
        } else {
            loanUserInfo.setRegisterTime(null);
        }
        // 社保
        if (loanUserInfo.getSheBao() != null) {
            if (loanUserInfo.getSheBao() == 0) {
                loanUserInfo.setSheBao(loanUserInfo.getSheBao() + 7005);
            } else if (loanUserInfo.getSheBao() == 1) {
                loanUserInfo.setSheBao(loanUserInfo.getSheBao() + 7000);
            } else if (loanUserInfo.getSheBao() == 2) {
                loanUserInfo.setSheBao(loanUserInfo.getSheBao() + 7004);
            } else {
                loanUserInfo.setSheBao(loanUserInfo.getSheBao() + 6999);
            }
        } else {
            loanUserInfo.setSheBao(7005);
        }
        // 公积金
        if (loanUserInfo.getGongJiJin() != null) {
            if (loanUserInfo.getGongJiJin() == 0) {
                loanUserInfo.setGongJiJin(loanUserInfo.getGongJiJin() + 8005);
            } else if (loanUserInfo.getGongJiJin() == 1) {
                loanUserInfo.setGongJiJin(loanUserInfo.getGongJiJin() + 8000);
            } else if (loanUserInfo.getGongJiJin() == 2) {
                loanUserInfo.setGongJiJin(loanUserInfo.getGongJiJin() + 8004);
            } else {
                loanUserInfo.setGongJiJin(loanUserInfo.getGongJiJin() + 7999);
            }

        } else {
            loanUserInfo.setGongJiJin(8005);
        }
       /* if (loanUserInfo.getHouseProperty() != null && loanUserInfo.getHouseProperty() != 0) {
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
            } else {
                loanUserInfo.setHouse(null);
                loanUserInfo.setHouseAssessment(null);
            }
            loanUserInfo.setHouseProperty(houseProperty + 9000);

        } else {
            loanUserInfo.setHouse(null);
            loanUserInfo.setHouseAssessment(null);
        }*/
        //测试
        Integer houseProperty = loanUserInfo.getHouseProperty();
        Integer house = loanUserInfo.getHouse();
        Integer houseAssessment = loanUserInfo.getHouseAssessment();
        if (houseProperty != null) {
            loanUserInfo.setHouseProperty(houseProperty + 9000);
            /*if (houseProperty == 4) {// 有房产
                // 房产设为有
                loanUserInfo.setHouse(1104);
            }*/
        }
        if (house != null) {
            loanUserInfo.setHouse(house + 1100);
        }
        if (houseAssessment != null) {
            loanUserInfo.setHouseAssessment(houseAssessment + 1200);
        }

       /* if (loanUserInfo.getCarProperty() != null && loanUserInfo.getCarProperty() != 0) {
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
            } else {
                loanUserInfo.setCar(null);
            }
            loanUserInfo.setCarProperty(carProperty + 1300);

        } else {
            loanUserInfo.setCar(null);
        }*/
        //测试
        Integer carProperty = loanUserInfo.getCarProperty();
        Integer car = loanUserInfo.getCar();
        Integer carAssessment = loanUserInfo.getCarAssessment();
        if (carProperty != null) {
            loanUserInfo.setCarProperty(carProperty + 1300);
           /* if (carProperty == 4) {// 有车产
                // 车产设为有
                loanUserInfo.setCar(1404);
            }*/
        }
        if (car != null) {
            loanUserInfo.setCar(car + 1400);
        }
        if (carAssessment != null) {
            loanUserInfo.setCarAssessment(carAssessment + 1500);
        }
        //借呗
        Integer jiebei = loanUserInfo.getJiebei();
        if (jiebei != null) {
            loanUserInfo.setJiebei(jiebei + 10000);
        } else {
            loanUserInfo.setJiebei(10000);
        }
        //学历
        Integer degree = loanUserInfo.getDegree();
        if (degree != null) {
            if (degree == 0) {
                loanUserInfo.setDegree(degree + 10015);
            } else {
                loanUserInfo.setDegree(degree + 10009);
            }

        }
        if (degree == null) {
            loanUserInfo.setDegree(100015);
        }
        //负债
        if (loanUserInfo.getDebt() == null) {
            loanUserInfo.setDebt(0);
        }
        // 保单
        if (loanUserInfo.getChit() != null) {
            if (loanUserInfo.getChit() == 0) {
                loanUserInfo.setChit(loanUserInfo.getChit() + 1702);
            } else if (loanUserInfo.getChit() == 2) {
                loanUserInfo.setChit(loanUserInfo.getChit() + 1698);
            } else if (loanUserInfo.getChit() == 1) {
                loanUserInfo.setChit(loanUserInfo.getChit() + 1700);
            } else if (loanUserInfo.getChit() == 3) {
                loanUserInfo.setChit(loanUserInfo.getChit() + 1700);
            } else if (loanUserInfo.getChit() == 4) {
                loanUserInfo.setChit(loanUserInfo.getChit() + 1700);
            }
            //保单情况
            if (loanUserInfo.getPolicySituation() != null) {
                loanUserInfo.setPolicySituation(loanUserInfo.getPolicySituation() + 2600);
            }
        }
        if (loanUserInfo.getChit() == null) {
            loanUserInfo.setChit(0);
        }
        // 信用卡使用状况
        if (loanUserInfo.getStatus() != null) {
            if (loanUserInfo.getStatus() == 0) {
                loanUserInfo.setStatus(loanUserInfo.getStatus() + 1806);
            } else if (loanUserInfo.getStatus() == 1) {
                loanUserInfo.setStatus(loanUserInfo.getStatus() + 1800);
            } else if (loanUserInfo.getStatus() == 2) {
                loanUserInfo.setStatus(loanUserInfo.getStatus() + 1800);
            } else if (loanUserInfo.getStatus() == 3) {
                loanUserInfo.setStatus(loanUserInfo.getStatus() + 1800);
            } else if (loanUserInfo.getStatus() == 4) {
                loanUserInfo.setStatus(loanUserInfo.getStatus() + 1800);
            } else if (loanUserInfo.getStatus() == 5) {
                loanUserInfo.setStatus(loanUserInfo.getStatus() + 1800);
            }
        } else {
            loanUserInfo.setStatus(null);
        }


        // 补充说明
        if (loanUserInfo.getTags() != null) {


            String[] tags = loanUserInfo.getTags().split(",");

            int tagsInt[] = new int[tags.length];
            for (int i = 0; i < tags.length; i++) {
                tagsInt[i] = Integer.parseInt(tags[i]) + 2200;

            }
            String tag = Arrays.toString(tagsInt);
            loanUserInfo.setTags(tag.substring(1, tag.length() - 1));
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
        Long loanOrderTime = loanUserMapper.queryLoanOrderTime(orderNo);
        if (loanOrderTime != null) {
            Date dt = new Date(loanOrderTime);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String loanOrderTimeStr = df.format(dt);
            loanOrder.setLoanOrderTime(loanOrderTimeStr);
        }

        if (loanOrder != null) {
            long oderTime = Long.parseLong(loanOrder.getOrderTime());
            Date date = new Date(oderTime);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String oderTimeStr = format.format(date);
            loanOrder.setOrderTime(oderTimeStr);
        }
        if (loanOrder == null) {
            loanOrder = loanUserMapper.queryIsGrabByEnterprise(orderNo);
            if (loanOrder != null) {
                String orderTime = loanOrder.getSubmitTime().substring(0, 19);
                loanOrder.setOrderTime(orderTime);
                loanOrder.setStatus(0);
            }
        }
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

    // 根据信用分算卖多少星币
    public Integer getPrice(int score) {
        Integer price = this.loanUserMapper.queryPrice(score);
        return price;
    }


    public String queryLoanPhoneByOrderNo(Long orderNo) {
        return loanUserMapper.queryLoanPhoneByOrderNo(orderNo);
    }

    // 设置此单已被抢
    public Integer updateLoanUserStatus(long orderNo, int status) {
        Integer isGrabbed = loanUserMapper.updateLoanUserStatus(orderNo, status);
        return isGrabbed;
    }

    //Api 针对打折状态设置
    public Integer updateLoanUserDiscountState(long orderNo, int discountedState) {
        return loanUserMapper.updateLoanUserDiscountState(orderNo, discountedState);
    }
}
