package top.imyth.practice4.service.community.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.imyth.practice4.configuration.RedisConfiguration;
import top.imyth.practice4.dao.*;
import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.combination.ArticleForShow;
import top.imyth.practice4.service.community.ArticleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleImageMapper articleImageMapper;

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private UserCollectionArticleMapper userCollectionArticleMapper;
    @Autowired
    private UserArticleMapper userArticleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    @Qualifier("articleRedisTemplate")
    private RedisTemplate<String, ArticleForShow> articleRedisTemplate;

    @Override
    public List<ArticleForShow> getNewestArticles(Integer endArticleId) {

        if (endArticleId == null || endArticleId < 0) {
            return articleRedisTemplate.opsForList().range(RedisConfiguration.NEWEST_TEN_ARTICLES, 0, -1);
        }

        List<Article> articleList = articleMapper.selectNewestArticles(endArticleId);
        if (articleList == null) {
            return new ArrayList<>(0);
        }
        List<ArticleForShow> articleForShowList = new ArrayList<>(articleList.size());
        for (Article article : articleList) {
            ArticleForShow articleForShow = new ArticleForShow();
            articleForShow.setArticle(article);
            articleForShow.setImageList(imageMapper.selectImagesByArticleId(article.getArticleId()));
            articleForShow.setCommentList(commentMapper.selectCommentsByArticleId(article.getArticleId()));
            articleForShow.setReplyNum(articleForShow.getCommentList().size());
            articleForShow.setFocusNumber(userCollectionArticleMapper.selectArticleCollectionNumber(article.getArticleId()));
            articleForShow.setUserId(userArticleMapper.selectUserIdByArticleId(article.getArticleId()));
            articleForShow.setUserNickname(userMapper.selectUserNicknameByUserID(articleForShow.getUserId()));
            articleForShowList.add(articleForShow);
        }
        return articleForShowList;
    }

    @Override
    public List<ArticleForShow> getPopularArticles() {
        return articleRedisTemplate.opsForList().range(RedisConfiguration.POPULAR_ARTICLE_LIST_KEY, 0, -1);
    }
}
