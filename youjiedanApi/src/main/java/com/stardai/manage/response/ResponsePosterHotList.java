package com.stardai.manage.response;

/**
 * @program: ujiedan
 * @Date: 2018/9/6 14:39
 * @Author: Tina
 * @Description:
 */
public class ResponsePosterHotList {

    /**
     *图片代码，图片的唯一标识
     */
    private String bgCode;
    /**
     * 图片分组
     */
    private int bgGroup;
    /**
     * 海报名称
     */
    private String bgName;
    /**
     * 背景缩略图
     */
    private String bgThumb;
    /**
     * 背景原图
     */
    private String bgOrigin;
    /**
         * 使用次数
     */
    private int bgCount;

    public String getBgCode() {
        return bgCode;
    }

    public void setBgCode(String bgCode) {
        this.bgCode = bgCode;
    }

    public int getBgGroup() {
        return bgGroup;
    }

    public void setBgGroup(int bgGroup) {
        this.bgGroup = bgGroup;
    }

    public String getBgName() {
        return bgName;
    }

    public void setBgName(String bgName) {
        this.bgName = bgName;
    }

    public String getBgThumb() {
        return bgThumb;
    }

    public void setBgThumb(String bgThumb) {
        this.bgThumb = bgThumb;
    }

    public String getBgOrigin() {
        return bgOrigin;
    }

    public void setBgOrigin(String bgOrigin) {
        this.bgOrigin = bgOrigin;
    }

    public int getBgCount() {
        return bgCount;
    }

    public void setBgCount(int bgCount) {
        this.bgCount = bgCount;
    }

    @Override
    public String toString() {
        return "ResponsePosterHotList{" +
                "bgCode=" + bgCode +
                ", bgGroup=" + bgGroup +
                ", bgName='" + bgName + '\'' +
                ", bgThumb='" + bgThumb + '\'' +
                ", bgOrigin='" + bgOrigin + '\'' +
                ", bgCount=" + bgCount +
                '}';
    }
}
