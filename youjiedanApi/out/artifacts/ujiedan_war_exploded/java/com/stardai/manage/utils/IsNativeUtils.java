package com.stardai.manage.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据身份证号,判断是否是本地人
 *
 * @author jokery
 * @create 2018-01-02 11:23
 **/

public class IsNativeUtils {

    /**
     * @param idCard 借款用户的身份证号
     * @return string 户籍所在地
     */
    public static String isNative(String idCard) {
        ObjectMapper MAPPER = new ObjectMapper();

        String host = "http://17.api.apistore.cn";
        String path = "/IDcard";
        String method = "GET";
        String appcode = "cdd71003b13e4fa49d29a957b44dd28e";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE cdd71003b13e4fa49d29a957b44dd28e
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("cardno",idCard);

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            String jsonTree = EntityUtils.toString(response.getEntity());
            /*
            * 返回示例
            * {
              "error_code": 0,
              "reason": "查询成功",
              "result": {
                "isok": 1,
                "cardNo": "511011xxxxx",
                "province": "四川省",
                "city": "内江市",
                "region": "东兴区",
                "area": "四川省内江市东兴区",
                "sex": "男",
                "birthday": "1989-01-27",
                "length": 18,
                "checkBit": "X",
                "addrCode": "511011"
              },
              "ordersign": "201706202300121433899561025"
            }
            * */
            JsonNode jsonNode = MAPPER.readTree(jsonTree);
            if (Integer.parseInt(jsonNode.get("result").get("isok").toString()) == 1) {
                ArrayList<String> municipality = new ArrayList<>();
                municipality.add("北京市");
                municipality.add("上海市");
                municipality.add("天津市");
                municipality.add("重庆市");
                String provinceWithQuotes = jsonNode.get("result").get("province").toString();
                String province = provinceWithQuotes.substring(1,provinceWithQuotes.length() - 1);
                if (municipality.contains(province)) {
                    return province;
                }else {
                    String oldCityWithQuotes = jsonNode.get("result").get("city").toString();
                    String oldcity = oldCityWithQuotes.substring(1,oldCityWithQuotes.length() - 1);
                    if ("省直辖县级行政区划".equals(oldcity)
                            || "省直辖行政单位".equals(oldcity)) {
                        oldCityWithQuotes = jsonNode.get("result").get("region").toString();
                        oldcity = oldCityWithQuotes.substring(1,oldCityWithQuotes.length() - 1);
                    }
                    return oldcity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "身份证号非法";
    }
}
