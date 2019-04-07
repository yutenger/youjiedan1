package com.stardai.manage.hotLoadClass;

import com.stardai.manage.mapper.EventMapper;
import com.stardai.manage.utils.SpringContextUtil;


/**
 * 热加载类(此类可以热更新)
 *
 * @author yax
 * @create 2019-01-16 14:01
 **/
public class CrowdCategoryMapping {
    public static boolean getExclusiveNewcomers(String userId){
        EventMapper eventMapper=SpringContextUtil.getBean(EventMapper.class);
        if(eventMapper.isNewUser(userId)>0) {
            return false;
        }else{
            return  true;
        }
       }
}
