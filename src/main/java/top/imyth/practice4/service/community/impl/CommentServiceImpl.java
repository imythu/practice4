package top.imyth.practice4.service.community.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imyth.practice4.dao.ArticleMapper;
import top.imyth.practice4.dao.CommentMapper;
import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.Comment;
import top.imyth.practice4.entity.combination.CommentForShow;
import top.imyth.practice4.observermode.Observable;
import top.imyth.practice4.service.community.CommentService;

import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private Observable observable;

    @Override
    public CommentForShow publishComment(Comment comment) {
        Date date = new Date();

        comment.setGmtCreate(date);
        comment.setGmtModified(date);
        comment.setCommentTime(date);
        if (comment.getReplyToUserId() != null && comment.getReplyToUserId().equals(comment.getUserId())) {
            comment.setReplyToUserId(null);
        }
        if (commentMapper.insertSelective(comment) < 0) {
            return null;
        }
        Long commentId = comment.getCommentId();
        System.out.println("评论id"+commentId);
        Long targetId = articleMapper.selectUserIdByArticleId(comment.getArticleId());
        if (comment.getReplyToUserId() == null) {
            Article article = articleMapper.selectByPrimaryKey(comment.getArticleId());
            article.setGmtModified(null);
            observable.sendMessage(article, targetId);
        } else {
            observable.sendMessage(comment, targetId);
        }
        return commentMapper.selectCommentsByCommentId(commentId);
    }
}
