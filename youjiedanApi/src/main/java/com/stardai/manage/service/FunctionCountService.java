package com.stardai.manage.service;

import java.io.UnsupportedEncodingException;

import com.stardai.manage.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stardai.manage.mapper.FunctionCountMapper;


/**
 * @author Tina
 * @date 创建时间：2018年5月3日 上午9:44:15
 * @类说明
 */

@Service
@SuppressWarnings("all")
public class FunctionCountService {

	@Autowired
	private FunctionCountMapper functionCountMapper;

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年5月3日下午2:38:52
	 * @Param:
	 * @Return:void
	 */
	public void addFunctionCountForApp(String functionName) {
		try {
			String replace = functionName;
			//这是刷新首页订单列表时后台统计的，不需要转码
			if(functionName.contains("订单_")|| functionName.contains("折扣_")){
				replace = "0";
			}
			switch (replace){
				case "0":
					break;
				case "1":
					functionName = "邀请同行";
					break;
				case "2":
					functionName = "联系客服";
					break;
				case "3":
					functionName = "常见问题";
					break;
				case "4":
					functionName = "订单推送";
					break;
				case "5":
					functionName = "活动弹屏";
					break;
				case "6":
					functionName = "进入活动";
					break;
				case "7":
					functionName = "邀请好友";
					break;
				//2.2版本之前传的是汉字，需要转码，2.2版本之后传的是代号，不需要转码
				default:
					functionName = new String(functionName.getBytes("ISO-8859-1"), "UTF-8");
					break;

			}
			//查看今天有没有点击过
			Integer count = functionCountMapper.queryIsClickToday(functionName,DateUtil.getDay());
			//今天点击过
			if(count != null){
				functionCountMapper.updateFunctionCount(functionName,DateUtil.getDay());
			}else{
				functionCountMapper.addFunctionCount(functionName);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addFunctionCountForH5(String functionName) {
		/*try {
			functionName = new String(functionName.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}*/
		functionCountMapper.addFunctionCount(functionName);
		
	}

	
}
