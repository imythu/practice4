package top.imyth.practice4.controller.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyth.practice4.entity.combination.ArticleForShow;
import top.imyth.practice4.service.community.ArticleService;

import java.util.List;

@RestController
@RequestMapping("/community/")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("getNewestArticles")
    public List<ArticleForShow> getNewestArticles(Integer endArticleId) {
        return articleService.getNewestArticles(endArticleId);
    }

    @GetMapping("getPopularArticles")
    public List<ArticleForShow> getPopularArticles() {
        return articleService.getPopularArticles();
    }
}
