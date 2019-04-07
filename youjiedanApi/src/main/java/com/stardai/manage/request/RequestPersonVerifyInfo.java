/**
 * 
 */
package com.stardai.manage.request;

/**
 * @author Tina
 * 2018年6月20日
 */
public class RequestPersonVerifyInfo {
	
	private String userId;

	private String image; //图片信息
	
	private String idCardNumber; //	身份证号码

	//个人认证时会传身份证正反面照片
	private String idCardFrontImage; //	国徽面照片链接

	private String idCardBackImage; //	个人头像面照片链接
	
	private String name; //姓名
	
	private String accessToken;

	private String sex; //性别

	private String address;//户籍地

	private String res; //三元素认证的结果

	private String resmsg;//三元素认证的成功或者失败的信息


	private int type;//0 为用户个人认证时人脸识别，1 为已认证用户更改手机号时人脸识别

	//人脸识别的错误码
	private int errorCode;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdCardFrontImage() {
		return idCardFrontImage;
	}

	public void setIdCardFrontImage(String idCardFrontImage) {
		this.idCardFrontImage = idCardFrontImage;
	}

	public String getIdCardBackImage() {
		return idCardBackImage;
	}

	public void setIdCardBackImage(String idCardBackImage) {
		this.idCardBackImage = idCardBackImage;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public String getResmsg() {
		return resmsg;
	}

	public void setResmsg(String resmsg) {
		this.resmsg = resmsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "RequestPersonVerifyInfo{" +
				"userId='" + userId + '\'' +
				", image='" + image + '\'' +
				", idCardNumber='" + idCardNumber + '\'' +
				", idCardFrontImage='" + idCardFrontImage + '\'' +
				", idCardBackImage='" + idCardBackImage + '\'' +
				", name='" + name + '\'' +
				", accessToken='" + accessToken + '\'' +
				", sex='" + sex + '\'' +
				", address='" + address + '\'' +
				", res='" + res + '\'' +
				", resmsg='" + resmsg + '\'' +
				", type=" + type +
				", errorCode=" + errorCode +
				'}';
	}
}
