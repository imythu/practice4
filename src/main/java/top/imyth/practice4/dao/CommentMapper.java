package top.imyth.practice4.dao;

import org.apache.ibatis.annotations.Param;import top.imyth.practice4.entity.Comment;
import top.imyth.practice4.entity.combination.CommentForShow;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Long commentId);

    Long insert(Comment record);

    Long insertSelective(Comment record);

    Comment selectByPrimaryKey(Long commentId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<CommentForShow> selectCommentsByArticleId(@Param("articleId")Long articleId);

    CommentForShow selectCommentsByCommentId(Long commentId);
}