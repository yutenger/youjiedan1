package com.stardai.manage.request;

/**
 * @program: ujiedan
 * @Date: 2018/9/6 14:31
 * @Author: Tina
 * @Description:展业海报
 */
public class RequestPosterList {

    //1：热门推荐，2：历史纪录或最新
    private int type;
    private String userId;
    //用户搜索的关键字
    private String keyWords;
    //海报编码
    private String bgCode;

    //海报分类，1000：全部 1001：励志，1002：表情，1003：创意，1004：问候，1005：搞笑，1006：温情 1007：招聘
    private int sortCode;

    private int page = 0;

    private int pageSize = 10;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public String getBgCode() {
        return bgCode;
    }

    public void setBgCode(String bgCode) {
        this.bgCode = bgCode;
    }

    public int getSortCode() {
        return sortCode;
    }

    public void setSortCode(int sortCode) {
        this.sortCode = sortCode;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "RequestPosterList{" +
                "type=" + type +
                ", userId='" + userId + '\'' +
                ", keyWords='" + keyWords + '\'' +
                ", bgCode='" + bgCode + '\'' +
                ", sortCode=" + sortCode +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
