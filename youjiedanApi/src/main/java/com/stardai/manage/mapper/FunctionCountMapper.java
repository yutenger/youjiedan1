package com.stardai.manage.mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionCountMapper {

	/**
	 * @Author:Tina
	 * @Description:TODO
	 * @Date:2018年5月3日下午2:39:24
	 * @Param:
	 * @Return:void
	 */
	void addFunctionCount(@Param("functionName") String functionName);

	//查看今天是否添加过
	Integer queryIsClickToday(@Param("functionName")String functionName, @Param("day")String day);

	//更新当天的点击量
	void updateFunctionCount(@Param("functionName")String functionName, @Param("day")String day);
}
