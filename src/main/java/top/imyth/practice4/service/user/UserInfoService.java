package top.imyth.practice4.service.user;

import org.springframework.web.multipart.MultipartFile;
import top.imyth.practice4.entity.User;
import top.imyth.practice4.entity.combination.FocusUser;
import top.imyth.practice4.entity.combination.PublishedArticle;

import java.util.List;
import java.util.Map;

public interface UserInfoService {
    /**
     * 登录检查
     * @param phoneNumber 用户登录名，手机号码
     * @param password 登录密码
     * @return 成功返回用户ID，密码错误返回0，用户不存在返回-1
     */
    Long loginCheck(String phoneNumber, String password);

    /**
     * 注册用户
     * @param userInfo 包含基本的用户信息
     * @return 数据库添加用户成功用户ID，用户注册用的电话号码已存在返回0
     */
    Long register(Map<String, String> userInfo);

    /**
     * 通过用户ID获取用户头像byte数组
     * @param userId 用户ID
     * @param realPath
     * @return 返回头像URL地址
     */
    byte[] getHeadImageUrlByUserId(Long userId, String realPath);

    /**
     * 通过用户ID获取完整的用户信息，括号前为 key ，括号中为 Java 类型
     * 包括用户标识字段userId(Long)、用户昵称userNickname(String)、个性签名userSignature(String)、
     * 用户出生时间birthday(Date)、
     * 用户性别1男0女gender(Boolean)、用户邮箱地址email(String)、用户手机号phoneNumber(String)、
     * 最后一次登录时间userLastLoginTime(Date)、用户注册时间registerTime(Date)
     * @param userId 用户ID
     * @return 返回用户信息
     */
    User getUserInfoByUserId(Long userId);

    /**
     * 通过用户ID获取已发布的贴子
     * @param userId 用户ID
     * @return 用户已发布贴子集合，为空则List容量为0
     */
    List<PublishedArticle> getPublishedArticlesByUserId(Long userId);

    /**
     * 通过用户ID获取已关注的人的用户信息，括号前为 key ，括号中为 Java 类型
     * 包括用户标识字段userId(Long)、用户昵称userNickname(String)、个性签名userSignature(String)
     * @param userId 用户ID
     * @return 返回已关注的人的用户信息，为空则List容量为0
     */
    List<FocusUser> getFocusUsersByUserId(Long userId);

    /**
     * 更新用户信息
     * @param user 包含要更新的字段信息
     * @return 成功返回1，失败返回0
     */
    Integer updateUserInfo(User user);

    /**
     * 更新头像图片
     * @param multipartFile 包含图片文件的对象
     * @param userId
     * @param fileRootPath
     * @return 成功返回1，失败返回0
     */
    Integer updateHeadImage(MultipartFile multipartFile, Long userId, String fileRootPath);

    int focusUser(Long myId, Long focusId);

    int cancelFocusUser(Long myId, Long focusId);
}
