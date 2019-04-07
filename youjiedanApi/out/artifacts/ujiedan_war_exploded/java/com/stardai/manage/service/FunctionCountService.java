package com.stardai.manage.service;

import java.io.UnsupportedEncodingException;
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
			functionName = new String(functionName.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		functionCountMapper.addFunctionCount(functionName);
		
	}
	
	public void addFunctionCountForH5(String functionName) {
		try {
			functionName = new String(functionName.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		functionCountMapper.addFunctionCount(functionName);
		
	}

	
}
