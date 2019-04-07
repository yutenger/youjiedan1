package com.stardai.manage.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "yjd_loan_credit_score")
public class CreditScore {
    @Id
    private Integer id;
    private String name;
    private String field;
    private String type;
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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "CreditScore{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", field='" + field + '\'' +
                ", type='" + type + '\'' +
                ", score=" + score +
                '}';
    }
}
