package top.imyth.practice4.dao;

import org.apache.ibatis.annotations.Param;
import top.imyth.practice4.entity.UserArticle;

public interface UserArticleMapper {
    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("articleId") Long articleId);

    int insert(UserArticle record);

    int insertSelective(UserArticle record);

    UserArticle selectByPrimaryKey(@Param("userId") Long userId, @Param("articleId") Long articleId);

    int updateByPrimaryKeySelective(UserArticle record);

    int updateByPrimaryKey(UserArticle record);
}