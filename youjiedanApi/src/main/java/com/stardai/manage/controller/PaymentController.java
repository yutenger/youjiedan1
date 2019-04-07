package com.stardai.manage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.stardai.manage.bean.ResponseModel;
import com.stardai.manage.pojo.CreditInvestigationRecord;
import com.stardai.manage.service.CreditInvestigationRecordService;
import com.stardai.manage.utils.*;
import com.pingplusplus.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * author yax
 * 支付接口controller
 */
@Controller
@RequestMapping("/pay")
public class PaymentController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PayPingUtil payPingUtil;
    @Autowired
    private CreditInvestigationRecordService creditInvestigationRecordService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    //private static String url="https://apiloan.stardai.com/creditdata/api/query"; 测试地址
    private static String url="https://loan.stardai.com/creditdata/api/query";
    private static String salt="zx";
    /**
     * 拉起微信或者支付宝插件
     * @param openId  微信openId
     * @param mobileNumber 手机号
     * @param idCard 身份证号
     * @param name 姓名
     * @param channel 渠道
     * @return
     */
    @RequestMapping("/payPub")
    @ResponseBody
    public ResponseModel pingPay(@RequestParam(required = false) String openId, String mobileNumber, String idCard, String name, String channel){
        String orderNo=String.valueOf(IdGen.get().nextId());
        Map<String, Object> initialMetadata=new HashMap<>();
        initialMetadata.put("orderNo",orderNo);
        initialMetadata.put("mobileNumber",mobileNumber);
        initialMetadata.put("idCard",idCard);
        initialMetadata.put("name",name);
        Map<String, Object> extra = new HashMap<String, Object>();
        if(!StringUtils.isEmpty(openId)) {
            extra.put("open_id", openId);
        }
        String ip= IpUtil.getIpAddr(request);
        Map<String,Object> pay=creditInvestigationRecordService.queryPayAmount();
        Integer payAmount= (Integer) pay.get("pay_amount");
        String payShow= (String) pay.get("pay_show");
        try {
            Charge charge=payPingUtil.createCharge(payAmount,"信用评估","信用评估",channel,ip,orderNo,extra,initialMetadata);
            creditInvestigationRecordService.saveCreditInvestigationRecord(openId,mobileNumber,idCard,name,orderNo,payShow);
            return ResponseModel.success(charge);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return ResponseModel.fail();
        }
    }

    /**
     * 接受支付回调
     * @param response
     */
    @RequestMapping("/payCallback")
    public void payCallback(HttpServletResponse response)  {
        PrintWriter pw=null;
        try {
            pw=response.getWriter();
            request.setCharacterEncoding("UTF8");
            /*
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                System.out.println(key + " " + value);
            }*/
           String signatureString= request.getHeader("x-pingplusplus-signature");
            // 获得 http body 内容
            BufferedReader reader = request.getReader();
            StringBuffer buffer = new StringBuffer();
            String string;
            while ((string = reader.readLine()) != null) {
                buffer.append(string);
            }
            reader.close();
            String data=buffer.toString();
            if(!payPingUtil.verifyData(data,signatureString,payPingUtil.getPubKey())){ //验签
                response.setStatus(500);
                return;
            }
            // 解析异步回调数据
            Event event = Webhooks.eventParse(data);
            String chargeId=event.getId();
            EventData eventData = event.getData();
            PingppObject pingppObject = eventData.getObject();
            JSONObject jobj = JSON.parseObject(pingppObject.toString());
            String transactionNo = jobj.getString("transaction_no");
            String orderNo = jobj.getString("order_no");
            CreditInvestigationRecord creditInvestigationRecord=creditInvestigationRecordService.queryCreditInvestigationRecordByOrderNo(orderNo);
            if(creditInvestigationRecord==null){
                response.setStatus(200);
                return;
            }
            String payStatus = creditInvestigationRecord.getPayStatus();
            if(!"0".equals(payStatus)){
                response.setStatus(200);
                return;
            }
            if ("charge.succeeded".equals(event.getType())) {        //支付成功
                payStatus = "1";
                //把订单数据传给征信服务器
                creditInvestigationRecord.setUserId(creditInvestigationRecord.getOpenId());
                creditInvestigationRecord.setFromUJD(3);
                HttpUtil2.post(url,creditInvestigationRecord);
            }else if ("refund.succeeded".equals(event.getType())){    //支付失败
                payStatus="2";
            }
            creditInvestigationRecordService.updateCreditInvestigationRecordByOrderNo(orderNo, payStatus, transactionNo,chargeId);
            response.setStatus(200);
            pw.print("success");
        }catch(Exception e){
            logger.info("错误信息:"+e.getMessage());
            pw.print("success");
            response.setStatus(500);
        }finally{
            if(pw!=null){
                pw.flush();
                pw.close();
            }
        }
    }
    @ResponseBody
    @RequestMapping("/getSignature")
    public ResponseModel getSignature(String param){
        try {
            String encodeStr= MD5Util.md5(param,salt);
            return ResponseModel.success(encodeStr);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return ResponseModel.fail();
        }
    }
    @ResponseBody
    @RequestMapping("/getPayAmount")
    public ResponseModel getPayAmount(){
        try {
            Map<String,Object> pay=creditInvestigationRecordService.queryPayAmount();
            String payShow= (String) pay.get("pay_show");
            return ResponseModel.success(payShow);
        }catch (Exception e){
            logger.info(e.getMessage());
            return ResponseModel.fail("网络异常");
        }
    }
}
