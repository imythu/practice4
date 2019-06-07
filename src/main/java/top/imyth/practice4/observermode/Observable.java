package top.imyth.practice4.observermode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.Comment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class Observable {
    private ConcurrentMap<Long, Observer> observerMap;
    @Autowired
    GsonHttpMessageConverter gsonHttpMessageConverter;

    private static final Logger logger = LoggerFactory.getLogger(Observable.class);

    public Observable() {
        this.observerMap = new ConcurrentHashMap<>();
    }

    public void sendMessage(Object message, Long target) {
        logger.info("通知目标"+target);
        Observer observer = observerMap.get(target);
        if (observer != null) {
            if (message instanceof Article) {
                Map<String, Article> map = new HashMap<>(1);
                map.put("articleMessage", (Article) message);
                observer.notifySendMessage(gsonHttpMessageConverter.getGson().toJson(map));
            }
            if (message instanceof Comment) {
                Map<String, Comment> map = new HashMap<>(1);
                map.put("commentMessage", (Comment) message);
                observer.notifySendMessage(gsonHttpMessageConverter.getGson().toJson(map));
            } else {
                observer.notifySendMessage(gsonHttpMessageConverter.getGson().toJson(message));
            }
        } else {
            logger.info("通知的用户不在线");
        }
    }

    public void register(Long userId, Observer observer) {
        logger.info("注册id" + userId);
        observerMap.put(userId, observer);
    }
    public void unRegister(Long userId) {
        observerMap.remove(userId);
        logger.info(userId+"已经下线");
    }
}
