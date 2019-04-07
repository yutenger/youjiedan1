package com.stardai.manage.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil2 {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil2.class);

    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    private static OkHttpClient okHttpClient =
            new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();

    public static String post(String url,Object obj){
        RequestBody requestBody = RequestBody.create(JSON, JSONObject.toJSONString(obj));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 200){
                return response.body().string();
            }else {
                return null;
            }
        } catch (Exception e) {
            log.error("网络异常1:" + e.getMessage());
            return null;
        }
    }

    public static String get(String url){
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200){
                return response.body().string();
            }else {
                return null;
            }
        } catch (Exception e) {
            log.error("网络异常2:" + e.getMessage());
            return null;
        }
    }
    public static String getHeads(String url, Map<String,String> head){
        Request.Builder build= new Request.Builder().url(url);
        for (Map.Entry<String, String> entry : head.entrySet()) {
            build.addHeader(entry.getKey(),entry.getValue());
        }
            Request request=build.build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.code() == 200){
                return response.body().string();
            }else {
                return null;
            }
        } catch (Exception e) {
            log.error("网络异常2:" + e.getMessage());
            return null;
        }
    }

    public static void getAsync(String url){
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                log.error("网络异常3:" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("返回:" + response.body().string());
            }
        });
    }
    public static void postAsync(String url,Object obj){
        RequestBody requestBody = RequestBody.create(JSON,JSONObject.toJSONString(obj));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
 
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    log.error("网络异常4:" + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    log.info("返回:" + response.body().string());
                }
            });
    }
    public static String post(String url, FormBody formBody){
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 200){
                return response.body().string();
            }else {
                return null;
            }
        } catch (Exception e) {
            log.error("网络异常5:",e);
            return null;
        }
    }
}
