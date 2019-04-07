package com.stardai.manage.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.common.base.Joiner;
import com.stardai.manage.mapper.HotCityMapper;
import com.stardai.manage.pojo.OrderCityList;
import com.stardai.manage.request.RequestCitiesByUserId;

/**
 * @author jdw
 * @date 2017/10/16
 */
@Service
@SuppressWarnings("all")
public class HotCityService {

	@Autowired
	private HotCityMapper hotCityMapper;

	// 修改关注的城市
	public List<HashMap<String,Object>> updateCitiesByUserId(RequestCitiesByUserId cities) {
		String cityTransform = "";
		List<HashMap<String,Object>> cities2 = new ArrayList<HashMap<String,Object>>();
		ArrayList<String> city = cities.getCities();
		ArrayList<String> city1 = new ArrayList<String>();
		for(String c:city){
			// 前端定位的城市获取的编码是计算到县区的，这里要把区编码转成对应的二级城市编码
			cityTransform = hotCityMapper.getCityCodeTransform(c);
			// 如果搜出的二级城市编码是空，说明传过来的就已经是二级城市编码，就直接存到列表里
			if (StringUtils.isNotBlank(cityTransform)) {
				city1.add(cityTransform);
			}else{
				city1.add(c);
			}
		}
		if (city != null && city.size() > 0) {
			// 根据前端传过来的城市编码查出对应的城市名称返回到前端
			List<HashMap<String,Object>> cityNameAndCode = hotCityMapper.getCityCodeAndNameByCityCode(city1);
			//arraylist转为String字符串
			String cityJson = Joiner.on(",").join(city1);
			// 把用户修改的城市信息保存到数据库里
			Integer result = hotCityMapper.updateCitiesByUserId(cityJson,cities.getUserId());
			if (result == 1) {
				return cityNameAndCode;
			} else {
				return cities2;
			}

		} else {
			hotCityMapper.updateCitiesByUserId(null, cities.getUserId());
			return cities2;
		}

	}

	/**
	 * @Author:Tina
	 * @Description: 获取城市列表
	 * @Date:2018年7月4日下午8:43:26
	 * @Param:
	 * @Return:List<OrderCityList>
	 */
	public List<OrderCityList> queryCityList() {

		return hotCityMapper.queryCityList();
	}

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年7月13日下午4:33:29
	 * @Param:
	 * @Return:List<HashMap<String,Object>>
	 */
	public List<HashMap<String, Object>> getCityCodeAndNameByCityCode(ArrayList list) {
		return hotCityMapper.getCityCodeAndNameByCityCode(list);
	}

	//把定位取得的区域编码转成对应的城市编码
	public String setAreaCodeToCityCode(String city) {
		String cityTransform = hotCityMapper.getCityCodeTransform(city);
		// 如果搜出的二级城市编码是空，说明传过来的就已经是二级城市编码
		if (StringUtils.isBlank(cityTransform)) {
			return city;
		} else {
			return cityTransform;
		}
	}
}
