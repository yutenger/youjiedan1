package com.stardai.manage.request;



public class RequestCreditScore {
    
   
    private String typeIn;//字段的英文名称
    private Integer onlyCode;//字段的唯一编码
    private Integer score;//字段的分数
	
	public String getTypeIn() {
		return typeIn;
	}
	public void setTypeIn(String typeIn) {
		this.typeIn = typeIn;
	}
	
	public Integer getOnlyCode() {
		return onlyCode;
	}
	public void setOnlyCode(Integer onlyCode) {
		this.onlyCode = onlyCode;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "RequestCreditScore [typeIn=" + typeIn + ", onlyCode=" + onlyCode + ", score=" + score + "]";
	}
	
    
    
}
