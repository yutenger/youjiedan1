package com.stardai.manage.response;

import java.util.HashMap;
import java.util.List;

/**
 * 封装信贷经理的实名认证信息
 *
 * @author jokery
 * @create 2018-03-12 13:25
 **/

public class ResponseApproveData {
    private String companyCity;
    //城市编码和对应的名称
    private String companyCityAndCode;
    private String companyName;
    private String companyAddress;
    private String companyBranch;
    //1 选择的公司列表  2 自己添加的公司
    private String companyType;

    //用于返回已经上传的照片的链接地址,暂时作废
    //private String companyImages;
    //private ArrayList<String> photoPersonal = null;

    private String createTime;
    
    private String popupReason; //用户认证失败弹出的信息
    private String certificationPhone; //用户填写的实名手机号

    private Integer punishCode;//惩罚措施：0 无惩罚； 1001 只能验证码登录，1002 取消公司认证， 1003 封号

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyBranch() {
        return companyBranch;
    }

    public void setCompanyBranch(String companyBranch) {
        this.companyBranch = companyBranch;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyCityAndCode() {
		return companyCityAndCode;
	}

	public void setCompanyCityAndCode(String companyCityAndCode) {
		this.companyCityAndCode = companyCityAndCode;
	}

	public String getPopupReason() {
		return popupReason;
	}

	public void setPopupReason(String popupReason) {
		this.popupReason = popupReason;
	}

	public String getCertificationPhone() {
		return certificationPhone;
	}

	public void setCertificationPhone(String certificationPhone) {
		this.certificationPhone = certificationPhone;
	}

    public Integer getPunishCode() {
        return punishCode;
    }

    public void setPunishCode(Integer punishCode) {
        this.punishCode = punishCode;
    }

    @Override
    public String toString() {
        return "ResponseApproveData{" +
                "companyCity='" + companyCity + '\'' +
                ", companyCityAndCode='" + companyCityAndCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyAddress='" + companyAddress + '\'' +
                ", companyBranch='" + companyBranch + '\'' +
                ", companyType='" + companyType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", popupReason='" + popupReason + '\'' +
                ", certificationPhone='" + certificationPhone + '\'' +
                ", punishCode=" + punishCode +
                '}';
    }
}
