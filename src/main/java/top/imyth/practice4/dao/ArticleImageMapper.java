package top.imyth.practice4.dao;

import org.apache.ibatis.annotations.Param;import top.imyth.practice4.entity.ArticleImage;
import top.imyth.practice4.entity.Image;

import java.util.List;

public interface ArticleImageMapper {
    int insert(ArticleImage record);

    int insertSelective(ArticleImage record);
}