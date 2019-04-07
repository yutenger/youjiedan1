package com.stardai.manage.pojo;

/**
 * ${DESCRIPTION}
 *
 * @author yax
 * @create 2019-01-16 10:31
 **/
public class ShellScreenMaterial {
    private String title;
    private String type;
    private String property;
    private String imgUrl;
    private String linkType;
    private String linkUrl;
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public String toString() {
        return "ShellScreenMaterial{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", property='" + property + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", linkType='" + linkType + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }
}
