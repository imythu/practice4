package top.imyth.practice4.dao;

import org.apache.ibatis.annotations.Param;import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.combination.PublishedArticle;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {
    int deleteByPrimaryKey(Long articleId);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Long articleId);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<PublishedArticle> selectPublishedArticleByUserId(@Param("userId")Long userId);

    List<Article> selectNewestArticles(@Param("endArticleId")Integer endArticleId);

    Integer selectNewestArticleId();

    List<Article> selectPopularArticles();

    Long selectUserIdByArticleId(@Param("articleId")Long articleId);
}