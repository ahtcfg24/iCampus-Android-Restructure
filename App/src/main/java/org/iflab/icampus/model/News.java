package org.iflab.icampus.model;

import java.util.List;

/**
 * 描述新闻的模型
 * Created at 2015/10/1 23:02.
 */
public class News {

    private String doctitle;//文档标题
    private String docreltime;//文档时间
    private String dochtmlcon;//文章内容
    private String docid;//文章id
    private String docchannel;//栏目id
    private String doctype;//文档类型
    private String subdoctitle;//文档子标题
    private String docauthor;//文档作者
    private String docsourcename;//文档来源部门
    private String opertime;//不知这是什么时间
    private String cruser;//不知道这是什么人
    private String crtime;//不知道这又是什么时间
    private String docabstract;//文章简介
    private String pcurl;//新闻来源网页链接
    private List<NewsRes> newsResList;//存放资源的列表

    @Override
    public String toString() {
        return "News{" +
                "doctitle='" + doctitle + '\'' +
                ", docreltime='" + docreltime + '\'' +
                ", dochtmlcon='" + dochtmlcon + '\'' +
                ", docid='" + docid + '\'' +
                ", docchannel='" + docchannel + '\'' +
                ", doctype='" + doctype + '\'' +
                ", subdoctitle='" + subdoctitle + '\'' +
                ", docauthor='" + docauthor + '\'' +
                ", docsourcename='" + docsourcename + '\'' +
                ", opertime='" + opertime + '\'' +
                ", cruser='" + cruser + '\'' +
                ", crtime='" + crtime + '\'' +
                ", docabstract='" + docabstract + '\'' +
                ", pcurl='" + pcurl + '\'' +
                ", newsResList=" + newsResList +
                '}';
    }

    public List<NewsRes> getNewsResList() {
        return newsResList;
    }

    public void setNewsResList(List<NewsRes> newsResList) {
        this.newsResList = newsResList;
    }


    public String getPcurl() {
        return pcurl;
    }

    public void setPcurl(String pcurl) {
        this.pcurl = pcurl;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDocchannel() {
        return docchannel;
    }

    public void setDocchannel(String docchannel) {
        this.docchannel = docchannel;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getDoctitle() {
        return doctitle;
    }

    public void setDoctitle(String doctitle) {
        this.doctitle = doctitle;
    }

    public String getSubdoctitle() {
        return subdoctitle;
    }

    public void setSubdoctitle(String subdoctitle) {
        this.subdoctitle = subdoctitle;
    }

    public String getDocauthor() {
        return docauthor;
    }

    public void setDocauthor(String docauthor) {
        this.docauthor = docauthor;
    }

    public String getDocsourcename() {
        return docsourcename;
    }

    public void setDocsourcename(String docsourcename) {
        this.docsourcename = docsourcename;
    }

    public String getDocreltime() {
        return docreltime;
    }

    public void setDocreltime(String docreltime) {
        this.docreltime = docreltime;
    }

    public String getOpertime() {
        return opertime;
    }

    public void setOpertime(String opertime) {
        this.opertime = opertime;
    }

    public String getCruser() {
        return cruser;
    }

    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    public String getCrtime() {
        return crtime;
    }

    public void setCrtime(String crtime) {
        this.crtime = crtime;
    }

    public String getDocabstract() {
        return docabstract;
    }

    public void setDocabstract(String docabstract) {
        this.docabstract = docabstract;
    }

    public String getDochtmlcon() {
        return dochtmlcon;
    }

    public void setDochtmlcon(String dochtmlcon) {
        this.dochtmlcon = dochtmlcon;
    }


}
