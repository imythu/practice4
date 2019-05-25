package top.imyth.practice4.entity.combination;

import java.util.Date;

public class CommentForShow {
    private String commentContent;
    private String replyToUserNickname;
    private String myNickname;
    private Date commentPublishedTime;
    private Long myUserId;
    private Long replyToUserId;
    private Long commentId;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getReplyToUserNickname() {
        return replyToUserNickname;
    }

    public void setReplyToUserNickname(String replyToUserNickname) {
        this.replyToUserNickname = replyToUserNickname;
    }

    public String getMyNickname() {
        return myNickname;
    }

    public Date getCommentPublishedTime() {
        return commentPublishedTime;
    }

    public void setCommentPublishedTime(Date commentPublishedTime) {
        this.commentPublishedTime = commentPublishedTime;
    }

    public Long getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(Long myUserId) {
        this.myUserId = myUserId;
    }

    public Long getReplyToUserId() {
        return replyToUserId;
    }

    public void setReplyToUserId(Long replyToUserId) {
        this.replyToUserId = replyToUserId;
    }

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
    }
}
