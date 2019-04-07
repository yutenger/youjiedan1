package com.stardai.manage.service;

import com.stardai.manage.mapper.BaseMapper;
import com.stardai.manage.utils.VeDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * yax
 * 基本sql执行service层
 */
@Service
public class BaseService {
    @Autowired
    private BaseMapper baseMapper;
   public  Map<String,Object> queryBySql(String sql,Object... params) {
       sql=paramConvert(sql,params);
        return baseMapper.queryBySql(sql);
    }
    public List<Map<String,Object>> queryListBySql(String sql, Object... params){
        sql=paramConvert(sql,params);
        return baseMapper.queryListBySql(sql);
    }
    public  <T> T queryBySql(String sql,Class<T> cla,Object... params) throws Exception {
        sql=paramConvert(sql,params);
        Map<String, Object> map = baseMapper.queryBySql(sql);
        if(map==null){
            return null;
        }
        if(isBaseClass(cla)){
            for(Map.Entry<String, Object> entry : map.entrySet()){
                return entry.getValue()==null?null:(T)entry.getValue();
            }
        }
        T t = cla.newInstance();
        Field[] fs=cla.getDeclaredFields();
        for(Field f:fs){
            String key1=f.getName();
            Object value=map.get(key1);
            if(value==null){
                value=map.get(convert(key1));
            }
            if(value!=null){
                f.setAccessible(true);
                if(value instanceof java.sql.Date ){
                    f.set(t,value.toString());
                }else if(value instanceof java.sql.Timestamp){
                    f.set(t, VeDate.dateToStr((java.sql.Timestamp)value,"yyyy-MM-dd HH:mm:ss"));
                }else {
                    f.set(t, value);
                }
            }
        }
        return t;
    }
    public  <T> List<T> queryListBySql(String sql,Class<T> cla,Object... params) throws Exception {
        sql=paramConvert(sql,params);
        List<Map<String, Object>> list = baseMapper.queryListBySql(sql);
        if(list==null){
            return null;
        }
        List<T> temp=new ArrayList<>();
        for(Map<String, Object> map:list){
            if(isBaseClass(cla)){
                for(Map.Entry<String, Object> entry : map.entrySet()){
                    temp.add(entry.getValue()==null?null:(T)entry.getValue());
                    continue;
                }
            }
            T t = cla.newInstance();
            Field[] fs=cla.getDeclaredFields();
            for(Field f:fs){
                String key1=f.getName();
                Object value=map.get(key1);
                if(value==null){
                    value=map.get(convert(key1));
                }
                if(value!=null){
                    f.setAccessible(true);
                    if(value instanceof java.sql.Date ){
                        f.set(t,value.toString());
                    }else if(value instanceof java.sql.Timestamp){
                        f.set(t, VeDate.dateToStr((java.sql.Timestamp)value,"yyyy-MM-dd HH:mm:ss"));
                    }else {
                        f.set(t, value);
                    }
                }
            }
            temp.add(t);
        }

        return temp;
    }
    public <T> T queryBasicTypeBySql(String sql,Class<T> cla,Object... params){
        sql=paramConvert(sql,params);
        Object obj=baseMapper.queryBasicTypeBySql(sql);
        if(obj==null){
            return null;
        }
        return (T)obj;
    }
    public void updateBySql(String sql,Object... params) {
        sql=paramConvert(sql,params);
        baseMapper.updateBySql(sql);
    }
    public void insertBysql(String sql,Object... params) {
        sql=paramConvert(sql,params);
        baseMapper.insertBysql(sql);
    }
    public void deleteBySql(String sql,Object... params)  {
        sql=paramConvert(sql,params);
        baseMapper.deleteBySql(sql);
    }
    private  String convert(String name){
       char[] chs=name.toCharArray();
       for(int i=0;i<chs.length;i++){
           char ch=chs[i];
           if(ch>='A'&&ch<='Z'){
               name=name.replaceFirst(String.valueOf(ch),"_"+String.valueOf(ch).toLowerCase());
           }
       }
       return name;
    }
    private boolean isBaseClass(Class clz){
       if(clz==int.class || clz==String.class||clz==long.class||clz==double.class||clz==float.class||clz==Integer.class||clz==Long.class||clz==Double.class||clz==Float.class){
           return true;
       }
       return false;
    }
    private String paramConvert(String sql,Object... params) {
       if(params!=null) {
           for (Object param : params) {
               if (param != null) {
                   if (param instanceof String) {
                       sql = sql.replaceFirst("\\?", "'" + param + "'");
                   } else {
                       sql = sql.replaceFirst("\\?", "" + param + "");
                   }
               }
           }
       }
        return sql;
    }
}
