package top.imyth.practice4.service.user.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import top.imyth.practice4.dao.ArticleMapper;
import top.imyth.practice4.dao.UserMapper;
import top.imyth.practice4.entity.User;
import top.imyth.practice4.entity.combination.FocusUser;
import top.imyth.practice4.entity.combination.PublishedArticle;
import top.imyth.practice4.service.user.UserInfoService;
import top.imyth.practice4.util.DateAndStringConverter;

import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("userInfoServiceImpl")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    @Qualifier("dateAndStringConverter")
    private DateAndStringConverter dateAndStringConverter;

    @Override
    public Long loginCheck(String phoneNumber, String password) {
        Map<String, Object> resultMap = userMapper.selectPasswordByPhoneNumber(phoneNumber);
        if (resultMap == null || resultMap.size() == 0) {
            return -1L;
        }
        if (resultMap.get("password").toString().equals(password)) {
            User user = new User();
            user.setUserLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(user);
            return (Long) resultMap.get("userId");
        }
        return 0L;
    }

    @Override
    public Long register(Map<String, String> userInfo) {
        User user = new User();
        user.setUserNickname(userInfo.get("userNickname") == null ? "myth" : userInfo.get("userNickname"));
        user.setUserSignature(userInfo.get("userSignature"));
        user.setHeadImageUrl("getHeadImage?userId=0");  // 0表示使用默认头像
        try {
            user.setBirthday(dateAndStringConverter.getDateFromString(userInfo.get("birthday")));
        } catch (ParseException e) {
            e.printStackTrace();
            return 500L;
        }
        user.setGender(Boolean.valueOf(userInfo.get("gender")));
        user.setEmail(userInfo.get("email"));
        user.setPhoneNumber(userInfo.get("phoneNumber"));
        user.setPassword(userInfo.get("password"));
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        return (long) userMapper.insertSelective(user);
    }

    @Override
    public byte[] getHeadImageUrlByUserId(Long userId, String realPath) {
        if (userId != null) {
            File headImageFile = new File(realPath + File.separator + "headImage_" + userId);
            if (!headImageFile.exists()) {
                try {
                    headImageFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "defaultHead.jpg");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return "500".getBytes();
                }
            }
            ByteBuf byteBuf = Unpooled.buffer();
            try {
                FileInputStream fileInputStream = new FileInputStream(headImageFile);
                byte[] bytes = new byte[1024];
                while (fileInputStream.available() > 0) {
                    fileInputStream.read(bytes);
                    byteBuf.writeBytes(bytes);
                }
                return byteBuf.array();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public User getUserInfoByUserId(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return null;
        }
        // 敏感信息不能传出
        user.setGmtModified(null);
        user.setPassword(null);
        return user;
    }

    @Override
    public List<PublishedArticle> getPublishedArticlesByUserId(Long userId) {
        List<PublishedArticle> articleList;
        articleList = articleMapper.selectPublishedArticleByUserId(userId);
        return articleList;
    }

    @Override
    public List<FocusUser> getFocusUsersByUserId(Long userId) {
        return userMapper.selectFocusUserByUserId(userId);
    }

    @Override
    public Integer updateUserInfo(User user) {
        user.setPassword(null);
        user.setPhoneNumber(null);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Integer updateHeadImage(MultipartFile multipartFile, Long userId, String fileRootPath) {
        if (!multipartFile.isEmpty()) {
            String fileName = "headImage_" + userId;
            File rootDirectory = new File(fileRootPath);
            if (!rootDirectory.exists()) {
                if (!rootDirectory.mkdir()) {
                    return 500;
                }
            }
            try {
                multipartFile.transferTo(new File(fileRootPath + File.separator + fileName));
                return 1;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }
}
