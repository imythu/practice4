package top.imyth.practice4.util;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class LoginStatusMap implements HttpSessionListener {
    public static final ConcurrentHashMap<Long, String> userIdAndSessionIdMap = new ConcurrentHashMap<>(1000);
    public static final ConcurrentHashMap<String, HttpSession> sessionIdAndSessionMap = new ConcurrentHashMap<>(1000);
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        sessionIdAndSessionMap.put(session.getId(), session);
        System.out.println(session.getId() + " 会话已创建");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println(se.getSession().getId() + " 会话已清除");
    }
}
