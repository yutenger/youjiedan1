package com.stardai.manage.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.stardai.manage.request.RequestCompanyCooperation;
import com.stardai.manage.response.ResponsePopupInfo;
import java.util.HashMap;
import java.util.List;

@Repository
public interface EventMapper {

    //查询当前有哪些活动要弹窗
    List<Integer> queryEventsIndate(@Param("currentTime") Long currentTime);

    //查询要弹窗的活动的url,图片url以及优先度
    List<HashMap<String,String>> queryUrlAndPriority(@Param("ids") List<Integer> ids);

    //新增一条返利数据
    void addEventRebate(@Param("sharedId") String userId);

    //查询返利总额
    Integer queryRebate(@Param("userId") String userId);

	

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
}
