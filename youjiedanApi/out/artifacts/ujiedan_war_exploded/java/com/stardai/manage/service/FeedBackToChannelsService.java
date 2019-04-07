package com.stardai.manage.service;

import com.alibaba.fastjson.JSON;
import com.stardai.manage.mapper.FeedBackToChannelsMapper;
import com.stardai.manage.response.ResponseFeedBackToChannels;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;

/**
 * 将单子被抢的信息反馈给第三方
 *
 * @author jokery
 * @create 2018-01-19 9:08
 **/

@Service
@SuppressWarnings("all")

public class FeedBackToChannelsService {
    @Autowired
    private FeedBackToChannelsMapper feedBackToChannelsMapper;

    public String queryToggleSwitch(String channel,String channleBranch) {
        String queryToggleSwitch = this.feedBackToChannelsMapper.queryToggleSwitch(channel,channleBranch);
        return queryToggleSwitch;
    }

    public HashMap<String,String> queryMobile(Long orderNo) {
        HashMap<String,String> mobile = this.feedBackToChannelsMapper.queryMobile(orderNo);
        return mobile;
    }

    public void feedback(ResponseFeedBackToChannels feedback,String url,
        String channel,String channelBranch) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String parameters = JSON.toJSONString(feedback);
        //查询要调用的接口
        //String url = this.queryAPI();
        //创建http post请求,输入url
        HttpPost httpPost = new HttpPost(url);
        //模拟浏览器进行访问
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        //构造一个form表单的实体
        StringEntity stringEntity = new StringEntity(parameters, ContentType.APPLICATION_JSON);
        // 将请求实体设置到httpPost对象中   报文
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = null;
        try {
            // 执行请求    敲回车
            response = httpclient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            //判断返回状态是不是200
            if (response.getStatusLine().getStatusCode() == 200) {
                String message = EntityUtils.toString(response.getEntity(), "UTF-8");
                //将反馈记录保存到数据库
                this.log(feedback.getMobile(),feedback.getBuy_money(),feedback.getBuy_time(),message,
                        channel,channelBranch);
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }
    }

    private void log(String mobile, Double buy_money, String buy_time, String message,
    String channel,String channelBranch) {
        this.feedBackToChannelsMapper.log(mobile,buy_money,buy_time,message,
                channel,channelBranch);
    }

    private String queryAPI() {
        String url = this.feedBackToChannelsMapper.queryAPI();
        return url;
    }
}
