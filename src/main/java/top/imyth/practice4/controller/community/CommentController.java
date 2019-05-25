package top.imyth.practice4.controller.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyth.practice4.entity.Comment;
import top.imyth.practice4.entity.combination.CommentForShow;
import top.imyth.practice4.service.community.CommentService;
import top.imyth.practice4.util.JsonResultKeyValueBuildUtil;

@RestController
@RequestMapping("/community/")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    @Qualifier("jsonResultKeyValueBuildUtil")
    private JsonResultKeyValueBuildUtil jsonResultKeyValueBuildUtil;

    @PostMapping("publishComment")
    public CommentForShow publishComment(@RequestBody Comment comment) {
        if (comment == null) {
            return null;
        }
        return commentService.publishComment(comment);
    }
}
