package com.stardai.manage.response;

/**
 * 邀请好友的分享链接与文案
 *
 * @author jokery
 * @create 2018-03-19 14:20
 **/

public class ResponseShareUrl {
    private String url;
    private String title;
    private String subtitle;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public String toString() {
        return "ResponseShareUrl{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                '}';
    }
}
