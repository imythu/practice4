package top.imyth.practice4.configuration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
public class RedisConfiguration {
    @Bean("popularArticleRedisTemplate")
    public RedisTemplate<Long, Integer> initRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Long, Integer> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<Integer> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Integer.class);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        System.out.println("自定义热点贴子RedisTemplate加载完毕！");
        return redisTemplate;
    }
}
