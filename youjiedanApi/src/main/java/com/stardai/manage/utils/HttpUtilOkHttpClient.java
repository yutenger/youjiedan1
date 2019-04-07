package com.stardai.manage.utils;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * @program: ujiedan
 * @Date: 2018/7/31 16:22
 * @Author: Tina
 * @Description:
 */
public class HttpUtilOkHttpClient {
    private static final Logger log = LoggerFactory.getLogger(HttpUtilOkHttpClient.class);

    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    private static OkHttpClient okHttpClient =
            new OkHttpClient.Builder()
                    .connectTimeout(10,TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS)
                    .writeTimeout(10,TimeUnit.SECONDS)
                    .build();

    public static String post(String url,Object obj){
        RequestBody requestBody = RequestBody.create(JSON,GsonUtil.toString(obj));
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
            log.error("网络异常1:",e);
            return null;
        }
    }

    public static String post(String url,FormBody formBody){
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.code() == 200){
                return response.body().string();
            }else {
                log.error("网络异常,http code:" + response.code());
                return null;
            }
        } catch (Exception e) {
            log.error("网络异常5:",e);
            return null;
        }
    }


    public static void postAsync(String url,Object obj){
        RequestBody requestBody = RequestBody.create(JSON,GsonUtil.toString(obj));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("网络异常4", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //   System.out.println("返回:" + String.valueOf(response));
            }
        });
    }



    public static void postAsync2(String url,String json){
        RequestBody requestBody = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("网络异常4",e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //  System.out.println("返回:" + String.valueOf(response));
            }
        });
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
            log.error("网络异常2", e);
            return null;
        }
    }




    public static void getAsync(String url){
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("网络异常3" , e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // System.out.println("返回:" + String.valueOf(response));
            }
        });
    }

    public static String getTest(String url){
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new X509TrustManager(){
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    System.out.println("*****************");
                    for(X509Certificate certificate:x509Certificates){
                        System.out.println(certificate);
                    }
                    System.out.println(s);
                    System.out.println("*****************");
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    System.out.println("------------------");
                    for(X509Certificate certificate:x509Certificates){
                        System.out.println(certificate);
                    }
                    System.out.println(s);
                    System.out.println("------------------");
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    System.out.println("getAcceptedIssuers");
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sc.getSocketFactory())
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            System.out.println("+++++++++++++++++");
                            System.out.println(s);
                            System.out.println(sslSession.getPeerHost() + "," + sslSession.getProtocol());
                            System.out.println("+++++++++++++++++");
                            return true;
                        }
                    })
                    .build();
            Request request = new Request.Builder().url(url).build();
            Call call = httpClient.newCall(request);

            Response response = call.execute();
            System.out.println(response);
            if (response.code() == 200){
                return response.body().string();
            }else {
                return null;
            }
        } catch (Exception e) {
            log.error("网络异常2", e);
            return null;
        }
    }


    //有些测试地址https证书无效的,这个方法可以信任所有证书,仅供测试使用
    public static String postTest(String url,Object model){
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,  new TrustManager[] { new X509TrustManager(){
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sc.getSocketFactory())
                    .hostnameVerifier((s, sslSession) -> true)
                    .build();


            FormBody.Builder builder = new FormBody.Builder();
            Field[] declaredFields = model.getClass().getDeclaredFields();
            for (Field field:declaredFields){
                field.setAccessible(true);
                Object o = field.get(model);
                if (o != null){
                    builder.add(field.getName(),o.toString());
                }
            }
            FormBody formBody = builder.build();
            System.out.println(formBody.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            try {
                Response response = httpClient.newCall(request).execute();
                System.out.println(response);
                if (response.code() == 200){
                    return response.body().string();
                }else {
                    return null;
                }
            } catch (Exception e) {
                log.error("网络异常1:",e);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
