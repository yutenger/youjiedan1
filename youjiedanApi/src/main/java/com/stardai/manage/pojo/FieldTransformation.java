package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "yjd_loan_db")
public class FieldTransformation {
    @Id
    private Integer id;
    private String name;
    private String type;
    private String typeOut;
    private Integer code;
    private Integer onlyCode;
    private String value;
    private Integer score;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getTypeOut() {
		return typeOut;
	}
	public void setTypeOut(String typeOut) {
		this.typeOut = typeOut;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getOnlyCode() {
		return onlyCode;
	}
	public void setOnlyCode(Integer onlyCode) {
		this.onlyCode = onlyCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "FieldTransformation [id=" + id + ", name=" + name + ", type=" + type + ", typeOut=" + typeOut
				+ ", code=" + code + ", onlyCode=" + onlyCode + ", value=" + value + ", score=" + score + "]";
	}
	
	
   
}
