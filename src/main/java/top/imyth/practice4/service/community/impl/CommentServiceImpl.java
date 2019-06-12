package top.imyth.practice4.service.community.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imyth.practice4.dao.ArticleMapper;
import top.imyth.practice4.dao.CommentMapper;
import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.Comment;
import top.imyth.practice4.entity.combination.ArticleForShow;
import top.imyth.practice4.entity.combination.CommentForShow;
import top.imyth.practice4.observermode.Observable;
import top.imyth.practice4.service.community.ArticleService;
import top.imyth.practice4.service.community.CommentService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private Observable observable;
    @Autowired
    private ArticleService articleService;

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

        articleService.cacheNewestTenArticles();

        Long commentId = comment.getCommentId();
        System.out.println("评论id"+commentId);
        Long targetId = comment.getReplyToUserId();
        List<Article> articles = new ArrayList<>(1);
        articles.add(articleMapper.selectByPrimaryKey(comment.getArticleId()));
        ArticleForShow articleForShow = articleService.getArticleForShowList(articles).get(0);
        if (comment.getReplyToUserId() == null) {
            targetId = articleMapper.selectUserIdByArticleId(comment.getArticleId());
        }
        observable.sendMessage(articleForShow, targetId);
        return commentMapper.selectCommentsByCommentId(commentId);
    }
}
