package top.imyth.practice4.entity;

import java.util.Date;

public class User {
    /** 用户ID*/
    private Long userId;

    /** 用户昵称*/
    private String userNickname;

    /** 用户签名*/
    private String userSignature;

    /** 用户头像图片路径*/
    private String headImageUrl;

    /** 用户出生时间*/
    private Date birthday;

    /** 用户性别1男0女*/
    private Boolean gender;

    /** 用户邮箱地址*/
    private String email;

    /** 用户手机号，用作登录账号*/
    private String phoneNumber;

    /** 用户密码*/
    private String password;

    /** 最后一次登录时间*/
    private Date userLastLoginTime;

    /** 记录插入时间*/
    private Date gmtCreate;

    /** 用户修改时间*/
    private Date gmtModified;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname == null ? null : userNickname.trim();
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature == null ? null : userSignature.trim();
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl == null ? null : headImageUrl.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getUserLastLoginTime() {
        return userLastLoginTime;
    }

    public void setUserLastLoginTime(Date userLastLoginTime) {
        this.userLastLoginTime = userLastLoginTime;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userNickname=").append(userNickname);
        sb.append(", userSignature=").append(userSignature);
        sb.append(", headImageUrl=").append(headImageUrl);
        sb.append(", birthday=").append(birthday);
        sb.append(", gender=").append(gender);
        sb.append(", email=").append(email);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", password=").append(password);
        sb.append(", userLastLoginTime=").append(userLastLoginTime);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append("]");
        return sb.toString();
    }
}