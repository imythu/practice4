package top.imyth.practice4.entity.combination;

import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.Image;

import java.util.List;

public class ArticleForShow {
    /** article用户ID*/
    private Long userId;

    /** article用户昵称*/
    private String userNickname;
    private Article article;
    private List<Image> imageList;
    private List<CommentForShow> commentList;
    private Integer replyNum = 0;
    private Integer focusNumber = 0;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public List<CommentForShow> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentForShow> commentList) {
        this.commentList = commentList;
    }

    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    public Integer getFocusNumber() {
        return focusNumber;
    }

    public void setFocusNumber(Integer focusNumber) {
        this.focusNumber = focusNumber;
    }

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
        this.userNickname = userNickname;
    }
}
