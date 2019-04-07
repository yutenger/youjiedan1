package com.stardai.manage.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.stardai.manage.pojo.OrderCityList;

/**
 * @author jdw
 * @date 2017/10/16
 */
@SuppressWarnings("all")
public interface HotCityMapper {

	
	//修改关注的城市
	Integer updateCitiesByUserId(@Param("city") String city, @Param("userId") String userId);

	/**
	 * @Author:Tina
	 * @Description:获取城市列表
	 * @Date:2018年7月4日下午8:44:37
	 * @Param:
	 * @Return:List<OrderCityList>
	 */
	List<OrderCityList> queryCityList();
	
	/**
	 * @Author:Tina
	 * @Description:根据城市编码查询城市名称
	 * @Date:2018年7月4日下午6:26:17
	 * @Param:
	 * @Return:String
	 */
	String getCityNameByCityCode(String cityCode);

	/**
	 * @Author:Tina
	 * @Description:根据城市名称批量获取城市编码
	 * @Date:2018年7月5日下午1:33:22
	 * @Param:
	 * @Return:List <HashMap<String, Object>>
	 */
	List <HashMap<String, Object>> getCityCodeAndNameByCityCode(@Param("cityCodes") ArrayList cityCodes);

	

	/**
	 * @Author:Tina
	 * @Description:前端定位的城市获取的编码是计算到县区的，这里要把区编码转成对应的城市编码
	 * @Date:2018年7月11日下午4:15:01
	 * @Param:
	 * @Return:String
	 */
	String getCityCodeTransform(String cityCode);

    String getCityCodeByCityName(String cityName);
}
