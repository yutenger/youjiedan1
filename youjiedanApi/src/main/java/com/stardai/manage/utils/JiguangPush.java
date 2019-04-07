package com.stardai.manage.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 极光推送
 *
 * @author jokery
 * @create 2017-12-26 17:20
 **/
@SuppressWarnings("all")
public class JiguangPush {
    private static final Logger log = LoggerFactory.getLogger(JiguangPush.class);

    private static String masterSecret = "f4e803d130558fece7c93c07";

    private static String appKey = "86b153c4e333a8cd359e4a58";

    //按照tags给关注某城市订单的信贷经理发推送
    public static  void jPushOrders(String city,String message,long orderNo,int amountInterval,String webank,int isNative) {
        log.info("对接收" + city + "订单的用户推送信息");
        PushResult result = pushOrders(String.valueOf(city), message,orderNo,amountInterval,webank,isNative);
        if (result != null && result.isResultOK()) {
            log.info("针对" + city + "的订单信息推送成功！");
        } else {
            log.info("针对" + city + "的订单信息推送失败！");
        }
    }

    /**
     * 极光推送方法(tags)
     *
     * @param city
     * @param message
     * @return PushResult
     */
    public static PushResult pushOrders(String city, String message,long orderNo,int amountInterval,String webank,int isNative) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        PushPayload payload = buildPushObject_android_ios_tags_alert(city, message,orderNo,amountInterval,webank,isNative);
        try {
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            return null;
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            return null;
        }
    }

    /**
     * 生成极光推送对象PushPayload（tags）
     *
     * @param city
     * @param message
     * @return PushPayload
     */
    public static PushPayload buildPushObject_android_ios_tags_alert(
            String city,String message,long orderNo,int amountInterval,String webank,int isNative) {
        HashMap<String, String> extras = new HashMap<>();
        extras.put("orderNo",orderNo + "");
        //获取当前时间,生成推送时间的标签
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        String time = simpleDateFormat.format(date);
        String now = time.substring(9,11);
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(amountInterval+"",webank,isNative+"","1111"))
                        .addAudienceTarget(AudienceTarget.tag_and(city,"1",now)).build())
                /*.setAudience(Audience.tag_and(city,"1",now))*/
                /*.setAudience(Audience.tag(amountInterval+"",webank,isNative+"","1111"))*/
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(message)
                                .addExtras(extras)
                                .setSound("UJDPUSH.wav")
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(message)
                                .addExtras(extras)
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .setTimeToLive(0)
                        .build())
                .build();

    }


    /**
     * 极光推送
     */
    //按照别名发推送
    /* public static void jiguangPushByAlias(String alias, String ALERT) {
        // alias = "123456";//声明别名
        log.info("对别名" + alias + "的用户推送信息");
        PushResult result = push(String.valueOf(alias), ALERT);
        if (result != null && result.isResultOK()) {
            log.info("针对别名" + alias + "的信息推送成功！");
        } else {
            log.info("针对别名" + alias + "的信息推送失败！");
        }
    }  */

    /**
     * 生成极光推送对象PushPayload（采用java SDK）
     *setAudience(Audience.tag_and(alias,alert)).
     * @param alias
     * @param alert
     * @return PushPayload
     */
    public static PushPayload buildPushObject_android_ios_alias_alert(String alias, String alert) {
        return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(alert)).build();
    }

    /**
     * 极光推送方法(采用java SDK)
     *
     * @param alias
     * @param alert
     * @return PushResult
     */
    public static PushResult push(String alias, String alert) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        PushPayload payload = buildPushObject_android_ios_alias_alert(alias, alert);
        try {
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            return null;
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            return null;
        }
    }

}