package top.imyth.practice4.util;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class LoginStatusMap implements HttpSessionListener {
    // userId和sessionId互相查询，用于强制下线
    public static final ConcurrentHashMap<Long, String> userIdAndSessionIdMap = new ConcurrentHashMap<>(1000);
    public static final ConcurrentHashMap<String, HttpSession> sessionIdAndSessionMap = new ConcurrentHashMap<>(1000);

    // sessionId和其session，用于销毁强制下线的时候清除session
    public static final ConcurrentHashMap<String, Long> sessionIDAndUserIdMap = new ConcurrentHashMap<>(1000);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //添加sessionId和session键值对，用于强制下线时调用session的销毁方法
        HttpSession session = se.getSession();
        sessionIdAndSessionMap.put(session.getId(), session);

//        System.out.println(session.getId() + " 会话已创建");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        // session自动过期时清除用于强制下线的键值对资源
        Long userId = sessionIDAndUserIdMap.get(sessionId);
        sessionIDAndUserIdMap.remove(sessionId);
        userIdAndSessionIdMap.remove(userId);
        sessionIdAndSessionMap.remove(sessionId);
//        System.out.println(se.getSession().getId() + " 会话已清除");
    }
}
