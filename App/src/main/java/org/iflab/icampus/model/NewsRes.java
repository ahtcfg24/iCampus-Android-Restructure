package org.iflab.icampus.model;


/**
 * 新闻中用到的资源（图片）
 * Created at 2015/10/2 15:05.
 */
public class NewsRes {


    private String resLink;//资源链接
    private String resName;//资源名称
    private String resType;//资源类型

    @Override
    public String toString() {
        return "NewsRes{" +
                "resLink='" + resLink + '\'' +
                ", resName='" + resName + '\'' +
                ", resType='" + resType + '\'' +
                '}';
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    public String getResLink() {
        return resLink;
    }

    public void setResLink(String resLink) {
        this.resLink = resLink;
    }

}