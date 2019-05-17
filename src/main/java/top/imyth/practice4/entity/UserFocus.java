package top.imyth.practice4.entity;

import java.util.Date;

public class UserFocus {
    /** 我的id*/
    private Long myUserId;

    /** 我关注的人的id*/
    private Long focusUserId;

    /** 记录插入时间*/
    private Date gmtCreate;

    /** 记录修改时间*/
    private Date gmtModified;

    public Long getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(Long myUserId) {
        this.myUserId = myUserId;
    }

    public Long getFocusUserId() {
        return focusUserId;
    }

    public void setFocusUserId(Long focusUserId) {
        this.focusUserId = focusUserId;
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
        sb.append(", myUserId=").append(myUserId);
        sb.append(", focusUserId=").append(focusUserId);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append("]");
        return sb.toString();
    }
}