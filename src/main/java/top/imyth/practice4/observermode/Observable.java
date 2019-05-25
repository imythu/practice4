package top.imyth.practice4.observermode;

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

    public Observable() {
        this.observerMap = new ConcurrentHashMap<>();
    }

    public void sendMessage(Object message, Long target) {
        System.out.println("通知目标"+target);
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
            }
        } else {
            System.out.println("通知的用户不在线");
        }
    }

    public void register(Long userId, Observer observer) {
        System.out.println("注册id" + userId);
        observerMap.put(userId, observer);
    }
    public void unRegister(Long userId) {
        observerMap.remove(userId);
        System.out.println(userId+"已经下线");
    }
}
