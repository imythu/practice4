package top.imyth.practice4.service.community;

import top.imyth.practice4.entity.combination.ArticleForShow;

import java.util.List;

public interface ArticleService {

    List<ArticleForShow> getNewestArticles(Integer endArticleId);

    List<ArticleForShow> getPopularArticles();
}
