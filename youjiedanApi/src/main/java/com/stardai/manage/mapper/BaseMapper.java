package com.stardai.manage.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * yax
 */
@Repository
public interface BaseMapper {
   Map<String,Object> queryBySql(@Param("sql") String sql);
   List<Map<String,Object>> queryListBySql(@Param("sql") String sql);
   Object queryBasicTypeBySql(@Param("sql") String sql);
   void updateBySql(@Param("sql") String sql);
   void insertBysql(@Param("sql") String sql);
   void deleteBySql(@Param("sql") String sql);
}
