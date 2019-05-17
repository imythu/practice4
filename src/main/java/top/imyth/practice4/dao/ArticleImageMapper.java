package top.imyth.practice4.dao;

import top.imyth.practice4.entity.ArticleImage;

public interface ArticleImageMapper {
    int insert(ArticleImage record);

    int insertSelective(ArticleImage record);
}