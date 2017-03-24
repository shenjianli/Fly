package com.shenjianli.fly.model;

import java.io.Serializable;

/**
 * Created by edianzu on 2016/8/31.
 *
 * 实现Serializable接口，以支持序列化，这样这个对象可以使用Intent来进行传递
 *
 */
public class Quote implements Serializable{

    /**
     * 标题
     内容
     背景图片
     标题颜色
     内容颜色
     app图片
     app名字
     倒计时（毫秒）
     */

    private String title;
    private String content;
    private String bgImgUrl;
    private String titleColor;
    private String contentColor;
    private String appImgUrl;
    private String appName;

    public long getCountTime() {
        return countTime;
    }

    public void setCountTime(long countTime) {
        this.countTime = countTime;
    }

    private long countTime;


    public Quote(){
        title = "";
        content = "";
        bgImgUrl = "";
        titleColor = "";
        contentColor = "";
        appImgUrl = "";
        appName = "";
        countTime = 5;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBgImgUrl() {
        return bgImgUrl;
    }

    public void setBgImgUrl(String bgImgUrl) {
        this.bgImgUrl = bgImgUrl;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getContentColor() {
        return contentColor;
    }

    public void setContentColor(String contentColor) {
        this.contentColor = contentColor;
    }

    public String getAppImgUrl() {
        return appImgUrl;
    }

    public void setAppImgUrl(String appImgUrl) {
        this.appImgUrl = appImgUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    @Override
    public String toString() {
        return "Quote{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", bgImgUrl='" + bgImgUrl + '\'' +
                ", titleColor='" + titleColor + '\'' +
                ", contentColor='" + contentColor + '\'' +
                ", appImgUrl='" + appImgUrl + '\'' +
                ", appName='" + appName + '\'' +
                ", countTime='" + countTime + '\'' +
                '}';
    }
}
