package top.imyth.practice4.controller.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.imyth.practice4.configuration.RedisConfiguration;
import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.combination.ArticleForShow;
import top.imyth.practice4.service.community.ArticleService;
import top.imyth.practice4.util.JsonResultKeyValueBuildUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/community/")
public class ArticleController {

    @Autowired
    private JsonResultKeyValueBuildUtil jsonResultKeyValueBuildUtil;

    @Autowired
    private ArticleService articleService;

    @Autowired
    @Qualifier("articleRedisTemplate")
    private RedisTemplate<String, ArticleForShow> articleRedisTemplate;

    @GetMapping("getNewestArticles")
    public List<ArticleForShow> getNewestArticles(Integer endArticleId) {

        if (endArticleId == null || endArticleId < 0) {
            return articleRedisTemplate.opsForList().range(RedisConfiguration.NEWEST_TEN_ARTICLES, 0, -1);
        }

        return articleService.getNewestArticles(endArticleId);
    }

    @GetMapping("getPopularArticles")
    public List<ArticleForShow> getPopularArticles() {
        return articleService.getPopularArticles();
    }

    @PostMapping("publishArticle")
    public Map<String, Integer> publishArticle(Long userId, String articleTitle, String articleContent, MultipartFile[] imageFiles) throws IOException {
        // 参数检查
        if (userId == null || articleTitle == null || imageFiles == null || articleContent == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(-1);
        }
        Article article = new Article();
        article.setArticleContent(articleContent);
        article.setArticleTitle(articleTitle);
        Date currentDate = new Date();
        article.setGmtCreate(currentDate);
        article.setGmtModified(currentDate);
        return jsonResultKeyValueBuildUtil.getResultMapFromInteger(articleService.saveArticleAndImage(userId, article, imageFiles));
    }

    @GetMapping("getArticleImage/{url}")
    public void getArticleImageByArticleId(HttpServletResponse response, @PathVariable("url") String url) throws IOException {
        response.getOutputStream().write(articleService.getArticleImageBytesByArticleId(url));
    }

    @PostMapping("focusArticle")
    public Map<String, Integer> focusArticle(Long userId, Long articleId) {
        if (userId == null || articleId == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(-1);
        }
        return jsonResultKeyValueBuildUtil.getResultMapFromInteger(articleService.focusArticle(userId, articleId));
    }

    @PostMapping("unFocusArticle")
    public Map<String, Integer> unFocusArticle(Long userId, Long articleId) {
        if (userId == null || articleId == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(-1);
        }
        return jsonResultKeyValueBuildUtil.getResultMapFromInteger(articleService.unFocusArticle(userId, articleId));
    }

    @GetMapping("getMyFocusArticlesId")
    public Map<String, List<Long>> getMyFocusArticlesId(Long userId) {
        if (userId == null) {
            return null;
        }
        return articleService.getMyFocusArticlesId(userId);
    }
    @GetMapping("getMyCollectionArticles")
    public List<ArticleForShow> getMyCollectionArticles(Long userId) {
        if (userId == null) {
            return null;
        }
        return articleService.getMyFocusArticles(userId);
    }
}
