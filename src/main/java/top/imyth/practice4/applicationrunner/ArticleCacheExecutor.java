package top.imyth.practice4.applicationrunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.imyth.practice4.configuration.RedisConfiguration;
import top.imyth.practice4.dao.*;
import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.combination.ArticleForShow;
import top.imyth.practice4.service.community.impl.ArticleServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Order(1)
public class ArticleCacheExecutor implements ApplicationRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    @Qualifier("articleRedisTemplate")
    private RedisTemplate<String, ArticleForShow> articleRedisTemplate;

    @Autowired
    private ArticleServiceImpl articleService;

    public void cachePopularArticle() {
        //
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            System.out.println("开始运行热门贴子缓存");
            List<Article> popularArticleList = articleMapper.selectPopularArticles();
            List<ArticleForShow> articleForShowList = articleService.getArticleForShowList(popularArticleList);
            articleRedisTemplate.delete(RedisConfiguration.POPULAR_ARTICLE_LIST_KEY);
            // 一次性push
            articleRedisTemplate.opsForList().rightPushAll(RedisConfiguration.POPULAR_ARTICLE_LIST_KEY, articleForShowList);
            System.out.println("热门贴子缓存完毕");
        }, 0L, 30L, TimeUnit.MINUTES);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        cachePopularArticle();
        cacheNewestTenArticles();
    }

    private void cacheNewestTenArticles() {

        //
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
            System.out.println("开始运行最新10条贴子缓存");
            List<Article> articles = articleMapper.selectNewestArticles(articleMapper.selectNewestArticleId() + 1);
            List<ArticleForShow> articleForShowList = articleService.getArticleForShowList(articles);
            articleRedisTemplate.delete(RedisConfiguration.NEWEST_TEN_ARTICLES);
            // 一次性push
            articleRedisTemplate.opsForList().rightPushAll(RedisConfiguration.NEWEST_TEN_ARTICLES, articleForShowList);
            System.out.println("最新10条贴子缓存完毕");
        }, 0L, 15L, TimeUnit.SECONDS);
    }
}
