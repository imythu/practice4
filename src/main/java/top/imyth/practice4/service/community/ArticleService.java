package top.imyth.practice4.service.community;

import org.springframework.web.multipart.MultipartFile;
import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.combination.ArticleForShow;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ArticleService {

    List<ArticleForShow> getNewestArticles(Integer endArticleId);

    List<ArticleForShow> getPopularArticles();

    Integer saveArticleAndImage(Long userId, Article article, MultipartFile[] imageFiles) throws IOException;

    byte[] getArticleImageBytesByArticleId(String url) throws IOException;

    Integer focusArticle(Long userId, Long articleId);
    List<ArticleForShow> getArticleForShowList(List<Article> articleList);

    Integer unFocusArticle(Long userId, Long articleId);

    Map<String, List<Long>> getMyFocusArticlesId(Long userId);

    List<ArticleForShow> getMyFocusArticles(Long userId);
}
