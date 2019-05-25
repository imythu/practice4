package top.imyth.practice4.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.imyth.practice4.entity.combination.ArticleForShow;

@EnableCaching
@Configuration
public class RedisConfiguration {

    public static final String POPULAR_ARTICLE_LIST_KEY = "POPULAR_ARTICLE_LIST_KEY";
    public static final String NEWEST_TEN_ARTICLES = "NEWEST_TEN_ARTICLES";

    @Bean("articleRedisTemplate")
    public RedisTemplate<String, ArticleForShow> initRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ArticleForShow> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<ArticleForShow> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(ArticleForShow.class);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        System.out.println("自定义热点贴子RedisTemplate加载完毕！");
        return redisTemplate;
    }
}
