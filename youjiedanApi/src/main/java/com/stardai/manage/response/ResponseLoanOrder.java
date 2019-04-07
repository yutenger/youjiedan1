package com.stardai.manage.response;


public class ResponseLoanOrder {

    private String userId;

    //抢单时间
    private String orderTime;

    //提交时间
    private String submitTime;

    private int status;

    private String role;

    //放款时间
    private String orderSuccessTime;

    //信贷经理姓名
    private String userName;

    //信贷经理手机号
    private String mobileNumber;

    //信贷经理所在公司
    private String companyName;
    //贷款申请时间
    private String loanOrderTime;

    public String getLoanOrderTime() {
        return loanOrderTime;
    }

    public void setLoanOrderTime(String loanOrderTime) {
        this.loanOrderTime = loanOrderTime;
    }

    public String getOrderSuccessTime() {
        return orderSuccessTime;
    }

    public void setOrderSuccessTime(String orderSuccessTime) {
        this.orderSuccessTime = orderSuccessTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "ResponseLoanOrder{" +
                "userId='" + userId + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", submitTime='" + submitTime + '\'' +
                ", status=" + status +
                ", role='" + role + '\'' +
                ", orderSuccessTime='" + orderSuccessTime + '\'' +
                ", userName='" + userName + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", companyName='" + companyName + '\'' +
                ", loanOrderTime='" + loanOrderTime + '\'' +
                '}';
    }
}
