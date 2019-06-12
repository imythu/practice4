package top.imyth.practice4.service.community.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import top.imyth.practice4.configuration.RedisConfiguration;
import top.imyth.practice4.dao.*;
import top.imyth.practice4.entity.*;
import top.imyth.practice4.entity.combination.ArticleForShow;
import top.imyth.practice4.service.community.ArticleService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleImageMapper articleImageMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private UserCollectionArticleMapper userCollectionArticleMapper;

    @Autowired
    private UserArticleMapper userArticleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Qualifier("articleRedisTemplate")
    private RedisTemplate<String, ArticleForShow> articleRedisTemplate;

    @Override
    public List<ArticleForShow> getNewestArticles(Integer endArticleId) {

        List<Article> articleList = articleMapper.selectNewestArticles(endArticleId);
        if (articleList == null) {
            return new ArrayList<>(0);
        }
        return getArticleForShowList(articleList);
    }

    @Override
    public List<ArticleForShow> getArticleForShowList(List<Article> articleList) {
        List<ArticleForShow> articleForShowList = new ArrayList<>(articleList.size());
        for (Article article : articleList) {
            ArticleForShow articleForShow = new ArticleForShow();
            articleForShow.setArticle(article);
            articleForShow.setImageList(imageMapper.selectImagesByArticleId(article.getArticleId()));
            articleForShow.setCommentList(commentMapper.selectCommentsByArticleId(article.getArticleId()));
            articleForShow.setReplyNum(articleForShow.getCommentList().size());
            articleForShow.setFocusNumber(userCollectionArticleMapper.selectArticleCollectionNumber(article.getArticleId()));
            articleForShow.setUserId(userArticleMapper.selectUserIdByArticleId(article.getArticleId()));
            articleForShow.setUserNickname(userMapper.selectUserNicknameByUserID(articleForShow.getUserId()));
            articleForShowList.add(articleForShow);
        }
        return articleForShowList;
    }

    @Override
    public Integer unFocusArticle(Long userId, Long articleId) {

        int result = userCollectionArticleMapper.deleteByPrimaryKey(userId, articleId);

        if (result > 0) {
            cacheNewestTenArticles();
        }

        return result;
    }

    @Override
    public Map<String, List<Long>> getMyFocusArticlesId(Long userId) {
        List<Long> articlesIdList = userCollectionArticleMapper.selectArticlesIdByUserId(userId);
        Map<String, List<Long>> stringListMap = new HashMap<>(1);
        stringListMap.put("result", articlesIdList);
        return stringListMap;
    }

    @Override
    public List<ArticleForShow> getMyFocusArticles(Long userId) {
        return getArticleForShowList(articleMapper.selectCollectionArticlesByUserId(userId));
    }

    @Override
    public List<ArticleForShow> getPopularArticles() {
        return articleRedisTemplate.opsForList().range(RedisConfiguration.POPULAR_ARTICLE_LIST_KEY, 0, -1);
    }

    @Override
    public Integer saveArticleAndImage(Long userId, Article article, MultipartFile[] imageFiles) throws IOException {
        int result = articleMapper.insert(article);
        if (result <=0 ) {
            throw new RuntimeException("insert article failed");
        }
        String rootPath = System.getProperty("user.dir") + File.separator + "articleImages";
        if (imageFiles.length != 0) {
            for (MultipartFile imageFile : imageFiles) {
                // 图片有一张为null 就算上传失败， 抛出异常回滚
                if (imageFile == null || imageFile.isEmpty()) {
                    throw new RuntimeException("image is null");
                } else {
                    String md5 = DigestUtils.md5DigestAsHex(imageFile.getInputStream());
                    String fileName = rootPath + File.separator + md5;
                    Image image = new Image();
                    Date nowDate = new Date();
                    image.setGmtCreate(nowDate);
                    image.setGmtModified(nowDate);
                    image.setImageUrl(md5);

                    imageMapper.insert(image);

                    ArticleImage articleImage = new ArticleImage();
                    articleImage.setGmtCreate(nowDate);
                    articleImage.setGmtModified(nowDate);
                    articleImage.setImageId(image.getImageId());
                    articleImage.setArticleId(article.getArticleId());

                    articleImageMapper.insert(articleImage);

                    File file = new File(fileName);
                    System.out.println(fileName);
                    if (!file.exists()) {
                        if (file.createNewFile()) {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            outputStream.write(imageFile.getBytes());
                            outputStream.close();
                        } else {
                            throw new RuntimeException("image save error");
                        }
                    }
                }
            }
        }
        UserArticle userArticle = new UserArticle();
        Date now = new Date();
        userArticle.setArticleId(article.getArticleId());
        userArticle.setUserId(userId);
        userArticle.setGmtCreate(now);
        userArticle.setGmtModified(now);
        int lastResult = userArticleMapper.insert(userArticle);
        if (lastResult > 0) {
            cacheNewestTenArticles();
        }
        return lastResult;
    }

    @Override
    public void cacheNewestTenArticles() {
        System.out.println("开始运行最新10条贴子缓存");
        List<Article> articles = articleMapper.selectNewestArticles(articleMapper.selectNewestArticleId() + 1);
        List<ArticleForShow> articleForShowList = getArticleForShowList(articles);
        articleRedisTemplate.delete(RedisConfiguration.NEWEST_TEN_ARTICLES);
        // 一次性push
        articleRedisTemplate.opsForList().rightPushAll(RedisConfiguration.NEWEST_TEN_ARTICLES, articleForShowList);
        System.out.println("最新10条贴子缓存完毕");
    }

    @Override
    public byte[] getArticleImageBytesByArticleId(String url) throws IOException {
        ByteBuf byteBuf = Unpooled.buffer();
        String fileName = System.getProperty("user.dir") + File.separator + "articleImages" + File.separator + url;
        File imageFile = new File(fileName);
        if (!(imageFile.exists())) {
            imageFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "img_load_fail.jpg");
        }
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        byte[] bytes = new byte[1024];
        while (fileInputStream.available() > 0) {
            fileInputStream.read(bytes);
            byteBuf.writeBytes(bytes);
        }
        return byteBuf.array();
    }

    @Override
    public Integer focusArticle(Long userId, Long articleId) {
        UserCollectionArticle userCollectionArticle = new UserCollectionArticle();
        userCollectionArticle.setArticleId(articleId);
        userCollectionArticle.setUserId(userId);
        Date now = new Date();
        userCollectionArticle.setGmtCreate(now);
        userCollectionArticle.setGmtModified(now);

        cacheNewestTenArticles();

        return userCollectionArticleMapper.insert(userCollectionArticle);
    }
}
