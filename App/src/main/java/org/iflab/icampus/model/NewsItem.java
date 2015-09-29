package org.iflab.icampus.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 描述新闻条目的类
 * Created at 2015/9/28 23:05.
 */
public class NewsItem  implements Serializable{
    private static final long serialVersionUID = 1L;
    private String passageId;
    private String title;
    private String preview;

    @Override
    public String toString() {
        return "NewsItem{" +
                "isDownloading=" + isDownloading +
                ", bitmap=" + bitmap +
                ", detailUrl='" + detailUrl + '\'' +
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

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setIsDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
    }

    private String author;
    private String updateTime;
    private String icon;
    private String detailUrl;
    private Bitmap bitmap = null;
    private boolean isDownloading = true;
}
