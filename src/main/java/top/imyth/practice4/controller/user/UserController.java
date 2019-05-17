package top.imyth.practice4.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.imyth.practice4.entity.Article;
import top.imyth.practice4.entity.User;
import top.imyth.practice4.entity.combination.FocusUser;
import top.imyth.practice4.entity.combination.PublishedArticle;
import top.imyth.practice4.service.user.UserInfoService;
import top.imyth.practice4.util.JsonResultKeyValueBuildUtil;
import top.imyth.practice4.util.LoginStatusMap;
import top.imyth.practice4.util.ParamCheckUtil;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    @Qualifier("userInfoServiceImpl")
    private UserInfoService userInfoServiceImpl;

    @Autowired
    @Qualifier("paramCheckUtil")
    private ParamCheckUtil paramCheckUtil;

    @Autowired
    @Qualifier("jsonResultKeyValueBuildUtil")
    private JsonResultKeyValueBuildUtil jsonResultKeyValueBuildUtil;

    @PostMapping(value = "register")
    public Map<String, Long> register(@RequestBody Map<String, String> params, HttpSession session) {
        if (params == null || params.get("password") == null || params.get("phoneNumber") == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromLong(-1L);
        }
        Long result = userInfoServiceImpl.register(params);
        if (result > 0) {
            // 注册成功
            session.setAttribute("registerResultUserId", result);
        }
        return jsonResultKeyValueBuildUtil.getResultMapFromLong(result);
    }

    @PostMapping(value = "headImageUpload")
    public Map<String, Integer> headImageUpload(MultipartFile headImageFile, HttpSession session) {
        String path = session.getServletContext().getRealPath("/headImages/");
        if (headImageFile == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(0);
        }
        Long registerResultUserId = Long.valueOf((String) session.getAttribute("registerResultUserId"));
        if (registerResultUserId == null) {
            Long userId = Long.valueOf((String) session.getAttribute("userId"));
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(userInfoServiceImpl.updateHeadImage(headImageFile,
                    userId, path));
        } else {
            session.removeAttribute("registerResultUserId");
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(userInfoServiceImpl.updateHeadImage(headImageFile,
                    registerResultUserId, path));
        }
    }

    @PostMapping(value = "login")
    public Map<String, Long> login(String phoneNumber, String password, HttpSession session) {
        if (phoneNumber == null || password == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromLong(-2L);
        }
        Long result = userInfoServiceImpl.loginCheck(phoneNumber, password);
        if (result > 0L) {
            session.setAttribute("userId", result);

            // 防止多处同时登录
            String currentUserOldSessionId = LoginStatusMap.userIdAndSessionIdMap.get(result);
            if (currentUserOldSessionId != null) {  // 如果存在session
                if (LoginStatusMap.sessionIdAndSessionMap.containsKey(currentUserOldSessionId)) {
                    HttpSession oldSession = LoginStatusMap.sessionIdAndSessionMap.get(currentUserOldSessionId);
                    if (oldSession != null) {
                        oldSession.invalidate();
                    }
                    LoginStatusMap.sessionIdAndSessionMap.remove(currentUserOldSessionId);
                }
            }
            LoginStatusMap.userIdAndSessionIdMap.put(result, session.getId());
        }
        return jsonResultKeyValueBuildUtil.getResultMapFromLong(result);
    }

    @GetMapping(value = "getHeadImage")
    public byte[] getHeadImage(Long userId, HttpSession session) {
        if (userId == null) {
            return null;
        }
        return userInfoServiceImpl.getHeadImageUrlByUserId(userId, session.getServletContext().getRealPath("/headImages/"));
    }

    @GetMapping(value = "getPublishedArticles")
    public List<PublishedArticle> getPublishedArticles(Long userId) {
        if (userId == null) {
            return null;
        }
        return userInfoServiceImpl.getPublishedArticlesByUserId(userId);
    }

    @GetMapping(value = "getFocusUsers")
    public List<FocusUser> getFocusUsers(Long userId) {
        if (userId == null) {
            return null;
        }
        return userInfoServiceImpl.getFocusUsersByUserId(userId);
    }

    @GetMapping(value = "getAllInfo")
    public User getAllInfo(Long userId) {
        if (userId == null) {
            return null;
        }
        return userInfoServiceImpl.getUserInfoByUserId(userId);
    }

    @PostMapping(value = "setMyInfo")
    public Map<String, Integer> setMyInfo(@RequestBody User userInfo) {
        if (userInfo == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(-1);
        }
        return jsonResultKeyValueBuildUtil.getResultMapFromInteger(userInfoServiceImpl.updateUserInfo(userInfo));
    }

    @PostMapping(value = "modifyPwd")
    public Map<String, Integer> modifyPwd(Long userId, String newPwd) {
        if (userId == null || newPwd == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(-1);
        }
        User user = new User();
        user.setPassword(newPwd);
        user.setUserId(userId);
        return jsonResultKeyValueBuildUtil.getResultMapFromInteger(userInfoServiceImpl.updateUserInfo(user));
    }
}
