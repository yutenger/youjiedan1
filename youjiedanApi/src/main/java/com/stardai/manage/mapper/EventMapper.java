package com.stardai.manage.mapper;


import com.stardai.manage.pojo.AppVersionInfo;
import com.stardai.manage.pojo.CrowdCategoryMapping;
import com.stardai.manage.pojo.ScreenSetting;
import com.stardai.manage.pojo.ShellScreenMaterial;
import com.stardai.manage.request.RequestPerson;
import com.stardai.manage.request.RequestPosterList;
import com.stardai.manage.response.ResponseCouponByGrabOrder;
import com.stardai.manage.response.ResponseLotteryList;
import com.stardai.manage.response.ResponsePosterHotList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.stardai.manage.request.RequestCompanyCooperation;
import com.stardai.manage.response.ResponsePopupInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface EventMapper {

    //查询当前有哪些活动要弹窗
    List<Integer> queryEventsIndate(@Param("currentTime") Long currentTime);

    //查询要弹窗的活动的url,图片url以及优先度
    List<HashMap<String,String>> queryUrlAndPriority(@Param("ids") List<Integer> ids);

	/**
	 * @Author:Tina
	 * @Description:插入一条企业合作信息
	 * @Date:2018年5月9日下午1:30:58
	 * @Param:
	 * @Return:Integer
	 */
	Integer addCompanyCooperation(RequestCompanyCooperation rcc);

	/**
	 * @Author:Tina
	 * @Description:查询是否已添加企业合作
	 * @Date:2018年5月9日下午1:32:02
	 * @Param:
	 * @Return:Integer
	 */
	String queryIsCooperation(String mobileNumber);

	/**
	 * @Author:Tina
	 * @Description:根据活动id,查询活动的具体信息
	 * @Date:2018年5月10日下午4:22:35
	 * @Param:
	 * @Return:HashMap<String,String>
	 */
	ResponsePopupInfo queryEventInfo(int eventId);

	/**
	 * @Author:Tina
	 * @Description:查询当前版本需要的弹屏内容（安卓）
	 * @Date:2018年5月8日上午10:22:53
	 * @Param:
	 * @Return:List<Integer>
	 */
    List<ResponsePopupInfo> queryPopUpIndateForAndroid(@Param("currentTime") Long currentTime, @Param("versionCode") String versionCode);
	
	/**
	 * @Author:Tina
	 * @Description:获取安卓端版本更新信息
	 * @Date:2018年5月10日下午5:32:56
	 * @Param:
	 * @Return:List<ResponsePopupInfo>
	 */
	ResponsePopupInfo getAndroidVersionInfo();

	/**
	 * @Author:Tina
	 * @Description:查询当前版本需要的弹屏内容（iOS）
	 * @Date:2018年5月11日上午9:42:58
	 * @Param:
	 * @Return:List<ResponsePopupInfo>
	 */
	List<ResponsePopupInfo> queryPopUpIndateForiOS(@Param("currentTime") Long currentTime, @Param("versionId") String versionId);

	/**
	 * @Author:Tina
	 * @Description:获取iOS端版本更新信息
	 * @Date:2018年5月11日上午9:43:04
	 * @Param:
	 * @Return:ResponsePopupInfo
	 */
	ResponsePopupInfo getiOSVersionInfo();

	/**
	 * @Author:Tina
	 * @Description:当要弹屏的活动是邀请时，去系统公告里查priority=123的数据
	 * @Date:2018年5月11日下午2:27:34
	 * @Param:
	 * @Return:ResponsePopupInfo
	 */
	ResponsePopupInfo queryInventEventInfo();

	/**
	 * @Author:Tina
	 * @Description:根据版本code获取ios版本
	 * @Date:2018年6月9日下午12:52:09
	 * @Param:
	 * @Return:String
	 */
	String getIosVersionByVersionCode(String versionCode);

	/**
	 * @Author:Tina
	 * @Description:根据ios版本获取安卓对应的的版本号
	 * @Date:2018年6月9日下午12:54:13
	 * @Param:
	 * @Return:String
	 */
	String getVersionCodeByIosVersion(String iosVersion);

	//查询发现页面的轮播图列表
    List<HashMap<String,Object>> queryCarouselList();

	//往奖励表插入一条用户获取奖励的记录
    void addLotteryForUser(@Param("userId")String userId, @Param("couponAmount")int couponAmount,@Param("couponFullreduction")int couponFullreduction,
	@Param("name")String name);

    //用户抽奖获取的奖励列表
	List<ResponseLotteryList> queryLotteryReward(@Param("userId")String userId, @Param("page")int page,@Param("pageSize")int pageSize);

	//查询全部的中奖纪录用作轮播
	List<ResponseLotteryList> queryAllLotteryReward();

	//展业海报首页热门推荐列表
    List<ResponsePosterHotList> queryPosterHotList(RequestPosterList requestPosterList);

    //搜索页面的热门推荐
	ArrayList<String> queryHotSearch();

	//搜索页面的历史纪录
	ArrayList<String> queryHistorySearch(String userId);

	//根据海报名称搜索海报内容
	List<ResponsePosterHotList> queryPosterByKeyWord(RequestPosterList requestPosterList);

	//记录该用户搜索的纪录
	void addSearchRecord(RequestPosterList requestPosterList);

	//清空历史纪录
	Integer setHistoryRecord(String userId);

	//添加海报使用次数
	Integer addUsingCount(RequestPosterList requestPosterList);

	//查看某个分类下的海报图片
    List<ResponsePosterHotList> queryPosterForEachSort(RequestPosterList requestPosterList);

    //查询用户之前是否搜索过这个关键词
	Integer queryIsSearchBefore(RequestPosterList requestPosterList);

	//更新用户的搜索记录
	void updateSearchRecord(RequestPosterList requestPosterList);

	//往数据库添加一条记录
    void addCreditRecord(RequestPerson requestPerson);

    //查询用户10月份活动抢的单子数量
    ResponseCouponByGrabOrder queryCouponByGrabOrder(String userId);

    //查询大数据信用查询需要的星币
    Integer queryCreditSearchCost();

    //查看哪些城市正在打折
    String queryDiscountCity();
    //查询当前用户当日花费多少星币
    int queryDayConsumption(@Param("day") String day,@Param("userId") String userId);
    //查看当前活动是否开始
    Map<String,Object> getActivityInfo(int type);

    //查询活动期间是否进过黑名单
    int queryIsLimit(@Param("start") long start,@Param("end") long end,@Param("userId") String userId);

    void insertSnatchRecord(@Param("userId") String userId);

    void updateSnatchRecord(@Param("userId")String userId,@Param("date") String date,@Param("count") int count,@Param("available5")Integer available5,@Param("available10")Integer available10,@Param("available50")Integer available50);

    int isRegistrationDuringTheEvent(@Param("userId") String userId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    Map<String,Object> getActivityInfoById(int type);

    void addPvUv(@Param("ip") String ip,@Param("buttonType") String buttonType);

    ScreenSetting getScreenSettingsByVersionCode(@Param("versionCode") String versionCode,@Param("osType") String osType);

    ShellScreenMaterial getShellScreenMaterialById(String id);

	AppVersionInfo getVersionInfoByOsType(String osType);

	CrowdCategoryMapping getPropertyMappingByProperty(String property);

	int isNewUser(String userId);

	void addStatisticsPvUv(@Param("eventId") String eventId,@Param("type") String type,@Param("ip") String ip);
}
