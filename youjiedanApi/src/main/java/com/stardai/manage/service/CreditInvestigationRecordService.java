package com.stardai.manage.service;

import com.stardai.manage.mapper.CreditInvestigationRecordMapper;
import com.stardai.manage.pojo.CreditInvestigationRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class CreditInvestigationRecordService {
    @Autowired
    private CreditInvestigationRecordMapper creditInvestigationRecordMapper;
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void saveCreditInvestigationRecord(String openId, String mobileNumber, String idCard, String name, String orderNo ,String payShow){
        creditInvestigationRecordMapper.saveCreditInvestigationRecord(openId,mobileNumber,idCard,name,orderNo,payShow);
    }
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    public void updateCreditInvestigationRecordByOrderNo( String orderNo, String payStatus,String transactionNo,String chargeId){
        creditInvestigationRecordMapper.updateCreditInvestigationRecordByOrderNo(orderNo,payStatus,transactionNo,chargeId);
    }
    public CreditInvestigationRecord queryCreditInvestigationRecordByOrderNo(String orderNo){
       return  creditInvestigationRecordMapper.queryCreditInvestigationRecordByOrderNo(orderNo);
    }
    public Map<String,Object> queryPayAmount(){
        return  creditInvestigationRecordMapper.queryPayAmount();
    }
}
