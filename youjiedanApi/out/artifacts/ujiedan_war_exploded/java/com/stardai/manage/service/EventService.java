package com.stardai.manage.service;

import com.stardai.manage.mapper.EventMapper;
import com.stardai.manage.mapper.ShareMapper;
import com.stardai.manage.request.RequestCompanyCooperation;
import com.stardai.manage.response.ResponsePopupInfo;
import com.stardai.manage.response.ResponseShareEvent;
import com.stardai.manage.response.ResponseShareUser;
import com.stardai.manage.utils.Base64Image;
import com.stardai.manage.utils.OssClientConstants;
import com.stardai.manage.utils.TwoDimensionCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jokery
 * @create 2018-01-23 13:16
 **/

@Service
@SuppressWarnings("all")

public class EventService {
	
	@Autowired
	private Base64Image base;
	
    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ShareMapper shareMapper;

    //查询当前有哪些活动要弹窗
    public List<Integer> queryEventsIndate(Long currentTime) {
        return this.eventMapper.queryEventsIndate(currentTime);
    }

    //查询要弹窗的活动的url,图片url以及优先度
    public List<HashMap<String,String>> queryUrlAndPriority(List<Integer> eventIDs) {
        return this.eventMapper.queryUrlAndPriority(eventIDs);
    }

    /**
     * @Author : jokery
     * @Description : 查询邀请的人数,获赠的星币,以及返利的总额
     * @Date : 2018/1/26 15:27
     * @Param :userId
     */
    public ResponseShareEvent checkShare(String userId,HttpServletRequest request) {
        ResponseShareUser shareUser = this.shareMapper.updateSharePerson(userId);
        ResponseShareEvent shareEvent = null;
        //设置分享出去的链接
        String url = shareMapper.queryShareUrl(2);
        url = url + "?parameter=" + userId;
        String twoDimensionUrl = "";
        
        try {
			if (shareUser == null) {
				//根据分享出去的链接生成二维码,把二维码图片传到oss上，获取oss上的链接
				twoDimensionUrl = this.getTwoDimensionCodeUrl(userId,url,request);
				
			    // 没有数据添加一条
			    this.shareMapper.addSharePerson(userId, url,twoDimensionUrl);
			    this.eventMapper.addEventRebate(userId);
			    shareEvent = new ResponseShareEvent(
			            0,0,url,30,0);
			}else {
			    //先把分享链接设进去
			    shareUser.setShareUrl(url);
			    //查询返利总额
			    Integer rebate = this.eventMapper.queryRebate(userId);
			    if (rebate == null) {
			        //没有数据添加一条
			        this.eventMapper.addEventRebate(userId);
			        shareEvent = new ResponseShareEvent(shareUser, 0);
			    } else {
			        shareEvent = new ResponseShareEvent(shareUser, rebate);
			    }
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        return shareEvent;
    }

    
    /**
	 * @throws Exception 
	 * @Author:Tina
	 * @Description:把分享的url转化成二维码图片，并把图片上传到oss获得一个二维码图片的url
	 * @Date:2018年5月29日上午9:31:01
	 * @Param:
	 * @Return:String
	 */
	private String getTwoDimensionCodeUrl(String userId,String url, HttpServletRequest request) throws Exception {
		//链接生成二维码之后，获取二维码图片的base64编码
		String twoDimensionBase64Code = TwoDimensionCode.createQRImageBuffer(url,200,200);
		//把图片上传到阿里云oss上，并返回图片的链接
		String imgurl = base.GenerateImageForTwoDimension(twoDimensionBase64Code, request, OssClientConstants.TWODIMENSIONCODE,userId);
		return imgurl;
	}
	

	/**
	 * @Author:Tina
	 * @Description:添加企业合作的信息
	 * @Date:2018年5月9日下午1:27:09
	 * @Param:
	 * @Return:Integer
	 */
	public Integer addCompanyCooperation(RequestCompanyCooperation rcc) {
		//一个号码7天内只能申请一次企业合作
		String phone = eventMapper.queryIsCooperation(rcc.getMobileNumber());
		//7天内没申请过企业合作，就插入一条新的信息
		if(phone == null ){
			return eventMapper.addCompanyCooperation(rcc);
		}
		return 2;
	}

	/**
	 * @Author:Tina
	 * @Description:根据活动id,查询活动的具体信息
	 * @Date:2018年5月10日下午4:18:29
	 * @Param:
	 * @Return:List<HashMap<String,String>>
	 */
	public ResponsePopupInfo queryEventInfo(int eventId) {
		return eventMapper.queryEventInfo(eventId);
	}

	/**
	 * @Author:Tina
	 * @Description:查询当前版本需要的弹屏内容（安卓）
	 * @Date:2018年5月7日下午2:07:53
	 * @Param:
	 * @Return:List<Integer>
	 */
	public  List<ResponsePopupInfo> queryPopUpIndateForAndroid(Long currentTime,String versionCode) {
		return this.eventMapper.queryPopUpIndateForAndroid(currentTime,versionCode);
	}
	
	/**
	 * @Author:Tina
	 * @Description:获取安卓端版本更新信息
	 * @Date:2018年5月10日下午5:32:23
	 * @Param:
	 * @Return:List<ResponsePopupInfo>
	 */
	public ResponsePopupInfo getAndroidVersionInfo() {
		
		return eventMapper.getAndroidVersionInfo();
	}

	/**
	 * @Author:Tina
	 * @Description:查询当前版本需要的弹屏内容（iOS）
	 * @Date:2018年5月11日上午9:41:52
	 * @Param:
	 * @Return:List<ResponsePopupInfo>
	 */
	public List<ResponsePopupInfo> queryPopUpIndateForiOS(Long currentTime, String versionId) {
		return eventMapper.queryPopUpIndateForiOS(currentTime,versionId);
	}

	/**
	 * @Author:Tina
	 * @Description:获取iOS端版本更新信息
	 * @Date:2018年5月11日上午9:42:21
	 * @Param:
	 * @Return:ResponsePopupInfo
	 */
	public ResponsePopupInfo getiOSVersionInfo() {
		return eventMapper.getiOSVersionInfo();
	}

	/**
	 * @Author:Tina
	 * @Description:当要弹屏的活动是邀请时，去系统公告里查priority=123的数据
	 * @Date:2018年5月11日下午2:26:09
	 * @Param:
	 * @Return:ResponsePopupInfo
	 */
	public ResponsePopupInfo queryInventEventInfo() {
		return eventMapper.queryInventEventInfo();
	}

	/**
	 * @Author:Tina
	 * @Description:根据版本code获取ios版本
	 * @Date:2018年6月9日下午12:44:00
	 * @Param:
	 * @Return:String
	 */
	public String getIosVersionByVersionCode(String versionCode) {
		
		return eventMapper.getIosVersionByVersionCode(versionCode);
	}

	/**
	 * @Author:Tina
	 * @Description:根据ios版本获取安卓的版本号
	 * @Date:2018年6月9日下午12:48:43
	 * @Param:
	 * @Return:String
	 */
	public String getVersionCodeByIosVersion(String iosVersion) {
		return eventMapper.getVersionCodeByIosVersion(iosVersion);
	}
	//查询发现页面的轮播图列表
    public List<HashMap<String,Object>> queryCarouselList() {
		return eventMapper.queryCarouselList();
    }
}
