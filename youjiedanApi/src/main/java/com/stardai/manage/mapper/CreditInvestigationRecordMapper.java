package com.stardai.manage.mapper;

import com.stardai.manage.pojo.CreditInvestigationRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface CreditInvestigationRecordMapper {
    void saveCreditInvestigationRecord(@Param("openId") String openId, @Param("mobileNumber") String mobileNumber, @Param("idCard") String idCard, @Param("name") String name, @Param("orderNo") String orderNo, @Param("payShow") String payShow);
    void updateCreditInvestigationRecordByOrderNo(@Param("orderNo") String orderNo, @Param("payStatus") String payStatus, @Param("transactionNo") String transactionNo, @Param("chargeId") String chargeId);
    CreditInvestigationRecord queryCreditInvestigationRecordByOrderNo(String orderNo);
    Map<String,Object> queryPayAmount();
}
