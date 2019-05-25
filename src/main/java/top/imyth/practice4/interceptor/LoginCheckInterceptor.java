package top.imyth.practice4.interceptor;

import com.google.gson.Gson;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("userId") == null) {
            System.out.println("拦截到" + request.getRemoteAddr() + "，未登录，不允许访问");
            Map<String, String> notLogin = new HashMap<>(1);
            notLogin.put("result", "not_login");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(new Gson().toJson(notLogin));
            return false;
        }
        return true;
    }
}
