package org.iflab.icampus.model;

import java.io.Serializable;

/**
 * 描述用户的类
 * Created by hcjcch on 2015/3/7.
 */
/*实现Serializable才可以把这个对象存储到文件中*/
public class User implements Serializable {
    private String userName;
    private String realName;
    private String userType;
    private String email;
    private String avatar;
    private String idCard;
    private String department;

    public User() {}

    public User(String userName, String realName, String userType, String email, String avatar, String idCard, String department) {
        this.userName = userName;
        this.realName = realName;
        this.userType = userType;
        this.email = email;
        this.avatar = avatar;
        this.idCard = idCard;
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", userType='" + userType + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", idCard='" + idCard + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }


}