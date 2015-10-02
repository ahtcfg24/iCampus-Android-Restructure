package org.iflab.icampus.model;

import java.io.Serializable;

/**
 * 描述新闻条目的类
 * Created at 2015/9/28 23:05.
 */
public class NewsItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String passageId;//不知道有什么卵用的东东
    private String title;//新闻标题
    private String preview;//新闻预览
    private String author;//这个属性似乎每篇都是空的
    private String updateTime;//新闻跟新时间
    private String icon;//新闻的第一张图
    private String detailURl;//新闻详情的链接

    @Override
    public String toString() {
        return "NewsItem{" +
                ", detailURl='" + detailURl + '\'' +
                ", icon='" + icon + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", author='" + author + '\'' +
                ", preview='" + preview + '\'' +
                ", title='" + title + '\'' +
                ", passageId='" + passageId + '\'' +
                '}';
    }

    public String getPassageId() {
        return passageId;
    }

    public void setPassageId(String passageId) {
        this.passageId = passageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDetailURl() {
        return detailURl;
    }

    public void setDetailURl(String detailURl) {
        this.detailURl = detailURl;
    }


}
