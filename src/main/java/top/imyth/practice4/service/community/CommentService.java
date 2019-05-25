package top.imyth.practice4.service.community;

import top.imyth.practice4.entity.Comment;
import top.imyth.practice4.entity.combination.CommentForShow;

public interface CommentService {
    CommentForShow publishComment(Comment comment);
}
