package top.imyth.practice4.dao;

import org.apache.ibatis.annotations.Param;
import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.UserCollectionArticle;

import java.util.List;

public interface UserCollectionArticleMapper {
    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("articleId") Long articleId);

    int insert(UserCollectionArticle record);

    int insertSelective(UserCollectionArticle record);

    UserCollectionArticle selectByPrimaryKey(@Param("userId") Long userId, @Param("articleId") Long articleId);

    int updateByPrimaryKeySelective(UserCollectionArticle record);

    int updateByPrimaryKey(UserCollectionArticle record);

    Integer selectArticleCollectionNumber(@Param("articleId")Long articleId);

    List<Long> selectArticlesIdByUserId(@Param("userId")Long userId);
}