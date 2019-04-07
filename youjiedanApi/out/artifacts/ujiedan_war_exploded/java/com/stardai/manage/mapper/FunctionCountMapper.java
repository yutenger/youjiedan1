package com.stardai.manage.mapper;
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
	void addFunctionCount(String functionName);
}
