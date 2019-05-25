package top.imyth.practice4.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.imyth.practice4.entity.User;
import top.imyth.practice4.entity.combination.FocusUser;
import top.imyth.practice4.entity.combination.PublishedArticle;
import top.imyth.practice4.service.user.UserInfoService;
import top.imyth.practice4.util.JsonResultKeyValueBuildUtil;
import top.imyth.practice4.util.LoginStatusMap;
import top.imyth.practice4.util.ParamCheckUtil;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    public Map<String, Integer> headImageUpload(MultipartFile headImageFile, HttpSession session, Long userId) {
        String path = System.getProperty("user.dir")+"/headImage/";
        if (headImageFile == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(-2);
        }
        if (session.getAttribute("registerResultUserId") == null) {
//            if (session.getAttribute("userId") == null) {
//                return jsonResultKeyValueBuildUtil.getResultMapFromInteger(-1);
//            }
            Long finalUserId = (Long) session.getAttribute("userId") == null ? userId: (Long) session.getAttribute("userId");
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(userInfoServiceImpl.updateHeadImage(headImageFile,
                    finalUserId, path));
        } else {
            Long registerResultUserId = Long.valueOf((String) session.getAttribute("registerResultUserId"));
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
//            System.out.println("上次登录sessionId：" + currentUserOldSessionId);
            // 如果存在session，说明在其他地方登录过
            if (currentUserOldSessionId != null) {
                // 判断一下，避免NPE
                if (LoginStatusMap.sessionIdAndSessionMap.containsKey(currentUserOldSessionId)) {
//                    System.out.println("不为null");
                    HttpSession oldSession = LoginStatusMap.sessionIdAndSessionMap.get(currentUserOldSessionId);
                    if (oldSession != null) {
                        // session无效化
//                        System.out.println("session无效化");
                        oldSession.invalidate();
//                        System.out.println("强制下线");
                    }
                    // 移除 无效sessionId和无效session
//                    System.out.println("移除 无效sessionId" +currentUserOldSessionId + "和无效session");
//                    LoginStatusMap.sessionIDAndUserIdMap.remove(currentUserOldSessionId);
//                    LoginStatusMap.sessionIdAndSessionMap.remove(currentUserOldSessionId);
                }
            }
            // 添加sessionId和userId的互相对应关系
//            System.out.print("添加sessionId和userId的互相对应关系");
            LoginStatusMap.sessionIDAndUserIdMap.put(session.getId(), result);
//            System.out.println(session.getId() + " --- " + result);
            // 如果登录时当前用户已有session，userId会覆盖之前的记录
            LoginStatusMap.userIdAndSessionIdMap.put(result, session.getId());
        }
        return jsonResultKeyValueBuildUtil.getResultMapFromLong(result);
    }

    @GetMapping("isOnline")
    public Map<String, String> isOnline(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromString("不在线");
        }
        return jsonResultKeyValueBuildUtil.getResultMapFromString("在线");
    }

    @RequestMapping("loginOut")
    public Map<String, Integer> loginOut(HttpSession session) {
        String sessionId = session.getId();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(1);
        }
        session.invalidate();
        LoginStatusMap.userIdAndSessionIdMap.remove(userId);
        LoginStatusMap.sessionIDAndUserIdMap.remove(sessionId);
        LoginStatusMap.sessionIdAndSessionMap.remove(sessionId);
        return jsonResultKeyValueBuildUtil.getResultMapFromInteger(1);
    }

    @GetMapping(value = "getHeadImage")
    public void getHeadImage(Long userId, HttpSession session, HttpServletResponse response) throws IOException {
        if (userId == null) {
            return;
        }
        System.out.println("收到请求图片url");
        byte[] bytes =  userInfoServiceImpl.getHeadImageUrlByUserId(userId, System.getProperty("user.dir")+"/headImage/");
//        System.out.println(Arrays.toString(bytes));
        //设置response头信息
        //禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg; charset=utf-8");
        response.getOutputStream().write(bytes);
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
        userInfo.setPhoneNumber(null);
        userInfo.setPassword(null);
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

    @PostMapping("focusSomeOne")
    public Map<String, Integer> focusSomeOne(Long myId, Long focusId) {
        if (myId == null || focusId == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(-1);
        }
        int result = userInfoServiceImpl.focusUser(myId, focusId);
        return jsonResultKeyValueBuildUtil.getResultMapFromInteger(result);
    }

    @PostMapping("cancelFocusSomeone")
    public Map<String, Integer> cancelFocusSomeone(Long myId, Long focusId) {
        if (myId == null || focusId == null) {
            return jsonResultKeyValueBuildUtil.getResultMapFromInteger(-1);
        }
        return jsonResultKeyValueBuildUtil.getResultMapFromInteger(userInfoServiceImpl.cancelFocusUser(myId, focusId));
    }
}
