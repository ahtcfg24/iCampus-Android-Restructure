package org.iflab.icampus.model;

/**
 * Created by hcjcch on 2015/3/7.
 */
public class User {
    private String userName;
    private String realName;
    private String userType;
    private String email;
    private String avatar;
    private String idCard;

    public User(String userName, String realName, String userType, String email, String avatar, String idCard) {
        this.userName = userName;
        this.realName = realName;
        this.userType = userType;
        this.email = email;
        this.avatar = avatar;
        this.idCard = idCard;
    }

    public User() {

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

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", userType='" + userType + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", idCard='" + idCard + '\'' +
                '}'+"\n";
    }
}