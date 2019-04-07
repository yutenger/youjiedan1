package com.stardai.manage.mapper;


import org.apache.ibatis.annotations.Param;
import java.util.HashMap;

/**
 * 反馈给渠道
 *
 * @author jokery
 * @create 2018-01-19 9:44
 **/

public interface FeedBackToChannelsMapper {
    //查询要不要返回数据,返回调用接口的路径
    String queryToggleSwitch(@Param("channel") String channel, @Param("channelBranch") String channelBranch);

    //查询手机
    HashMap<String,String> queryMobile(@Param("orderNo") Long orderNo);

    //查云禾的接口
    String queryAPI();

    //记录本次反馈
    void log(@Param("mobile") String mobile, @Param("buy_money") Double buy_money,
             @Param("buy_time") String buy_time, @Param("message") String message,
             @Param("channel") String channel, @Param("channelBranch") String channelBranch);
}
