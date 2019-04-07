package com.stardai.manage.response;

/**
 * @program: ujiedan
 * @Date: 2018/7/27 15:46
 * @Author: Tina
 * @Description:
 */
public class ResponseUserCardAndName {
    // 信贷经理姓名
    private String userName;

    // 身份证号
    private String idCard;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Override
    public String toString() {
        return "ResponseUserCardAndName{" +
                "userName='" + userName + '\'' +
                ", idCard='" + idCard + '\'' +
                '}';
    }
}
