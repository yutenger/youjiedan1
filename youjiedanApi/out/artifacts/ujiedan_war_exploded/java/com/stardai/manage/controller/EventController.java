package com.stardai.manage.controller;

import com.alibaba.fastjson.JSON;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.request.RequestCompanyCooperation;
import com.stardai.manage.response.ResponsePopupInfo;
import com.stardai.manage.response.ResponseShareEvent;
import com.stardai.manage.service.EventService;
import com.stardai.manage.service.VersionService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 活动
 *
 * @author jokery
 * @create 2018-01-23 11:09
 **/

@Controller
@RequestMapping(value = "MZevent")
@SuppressWarnings("all")

public class EventController extends BaseController {

    @Autowired
    private EventService eventService;
    
    
    @Autowired
	private VersionService versionCheckService;
    
    
    /**
     * 
     * @Author:Tina
     * @Description:登录时弹屏设置
     * @Date:2018年5月4日下午4:10:40
     * @Param:
     * @Return:ResponseModel
     */
    @RequestMapping(value = "popupScreenOnLoginAndroid",method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel popupScreenOnLoginAndroid (@RequestBody HashMap<String,String> versionInfo, BindingResult bindingResult) {
        
    	if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}

    	try {
        	String versionCode = versionInfo.get("versionCode");
        	
        	List<ResponsePopupInfo> data = new ArrayList<ResponsePopupInfo>();
            //查询当前系统时间
            Long currentTime = System.currentTimeMillis();
            
            if(StringUtils.isBlank(versionCode)){
            	return ResponseModel.fail("参数错误");
            }
            
            //查询需要弹屏的内容 (1 版本更新 ，2 h5活动，3 邀请同行，4 充值， 5 认证)
            List<ResponsePopupInfo> eventIDs = this.eventService.queryPopUpIndateForAndroid(currentTime,versionCode);
            //如果返回的list为空,则表示当前没有活动
            if (eventIDs != null && eventIDs.size() != 0) {
            	//eventIDs里面有两个值，priority(返回此版本所要弹的屏),eventId(要弹屏活动的id)
            	for(ResponsePopupInfo eventID : eventIDs){ 
            		if(eventID.getPriority() == 1){ //版本更新
            			eventID = eventService.getAndroidVersionInfo();
            			if(eventID == null){
            				return ResponseModel.error("未查询到当前系统下的最新版本号");
            			}
            			eventID.setPriority(1);
            		}else if(eventID.getPriority() == 2){
            			eventID = eventService.queryEventInfo(eventID.getEventId());
            			if(eventID == null){
            				return ResponseModel.error("活动数据异常");
            			}
            			eventID.setPriority(2);
            		}else if(eventID.getPriority() == 3){
            			eventID = eventService.queryInventEventInfo();//如果是邀请同行活动时，就去系统公告表里查询priority=123的数据
            			if(eventID == null){
            				return ResponseModel.error("活动数据异常");
            			}
            			eventID.setPriority(3);
            		}
            		data.add(eventID);
            		 
            	}
            	return ResponseModel.success(data);
            }else {
                return ResponseModel.fail("暂无活动");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
		
    }
    
    @RequestMapping(value = "popupScreenOnLoginiOS",method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel popupScreenOnLoginiOS (@RequestBody HashMap<String,String> versionInfo, BindingResult bindingResult) {
        
    	if (bindingResult.hasErrors()) {
			return ResponseModel.error(bindingResult);
		}
    	try {
        	String versionId = versionInfo.get("versionId");
        	if(StringUtils.isBlank(versionId)){
             	return ResponseModel.fail("参数错误");
             }
        	
        	List<ResponsePopupInfo> data = new ArrayList<ResponsePopupInfo>();
            //查询当前系统时间
            Long currentTime = System.currentTimeMillis();
            //查询需要弹屏的内容(1 版本更新 ，2 h5活动，3 邀请同行，4 充值， 5 认证)
            List<ResponsePopupInfo> eventIDs = this.eventService.queryPopUpIndateForiOS(currentTime,versionId);
            //如果返回的list为空,则表示当前没有活动
            if (eventIDs != null && eventIDs.size() != 0) {
            	//eventIDs里面有两个值，priority(返回此版本所要弹的屏),eventId(要弹屏活动的id)
            	for(ResponsePopupInfo eventID : eventIDs){ 
            		if(eventID.getPriority() == 1){ //版本更新
            			eventID = eventService.getiOSVersionInfo();
            			if(eventID == null){
            				return ResponseModel.error("未查询到当前系统下的最新版本号");
            			}
            			eventID.setPriority(1);
            		}else if(eventID.getPriority() == 2){
            			eventID = eventService.queryEventInfo(eventID.getEventId());
            			if(eventID == null){
            				return ResponseModel.error("活动数据异常");
            			}
            			eventID.setPriority(2);
            		}else if(eventID.getPriority() == 3){
            			eventID = eventService.queryInventEventInfo();//如果是邀请同行活动时，就去系统公告表里查询priority=123的数据
            			if(eventID == null){
            				return ResponseModel.error("活动数据异常");
            			}
            			eventID.setPriority(3);
            		}
            		data.add(eventID);
            	}
            	return ResponseModel.success(data);
            }else {
                return ResponseModel.fail("暂无活动");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.fail("网络异常,请稍后再试");
        }
		
    }

    /**
     * @Author : jokery
     * @Description : 查询金额与邀请的人数,以及返利总额
     * 目前没有返利，1.5.0版本不再调用，调用shareController里面的方法
     * @Date : 2018/1/26 13:54
     * @Param :userId(用户唯一识别代码,登录时返回给前端,由前端传入)
    * @Param :request
    * @Param :response
     */
    @RequestMapping(value = "querySharePerson", method = RequestMethod.GET)
    public void queryShare(@RequestParam("parameter") String userId, HttpServletRequest request,
                           HttpServletResponse response) {
        try {
            ResponseShareEvent shareEvent = this.eventService.checkShare(userId,request);
            String jsonString = JSON.toJSONString(shareEvent);
            response.setCharacterEncoding("UTF-8");
            String callback = request.getParameter("callback");
            response.getWriter().write(callback + "(" + jsonString + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @Author:Tina
     * @Description:添加企业合作的信息
     * @Date:2018年5月9日上午11:26:37
     * @Param:
     * @Return:ResponseModel
     */
    @ResponseBody
	@RequestMapping(value = "addCompanyCooperation", method = RequestMethod.POST)
	public ResponseModel addCompanyCooperation(@RequestBody RequestCompanyCooperation rcc, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ResponseModel.error(bindingResult);
			}
			
			Integer result = eventService.addCompanyCooperation(rcc);
			if (result != null && result == 1) {
				return ResponseModel.success("提交成功");
			} else {
				return ResponseModel.error("请勿重复提交");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseModel.fail("网络异常,请稍后再试");
		}
	}

	//发现界面的轮播图显示
	@ResponseBody
	@RequestMapping(value = "getCarouselImage", method = RequestMethod.GET)
	public ResponseModel getCarouselImage( HttpServletRequest request) {

		List<HashMap<String,Object>> carouselList = eventService.queryCarouselList();
		if( carouselList != null && carouselList.size() > 0){
			return ResponseModel.success(carouselList);
		}
		return ResponseModel.fail("网络异常,请稍后再试");
	}
    
    
}
