package top.imyth.practice4.applicationrunner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Order(3)
public class CreateNecessaryDirectory implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {

        String imageRootPath = System.getProperty("user.dir") + File.separator + "articleImages" + File.separator;
        String headImageRootPath = System.getProperty("user.dir") + File.separator + "headImage" + File.separator;
        File file = new File(imageRootPath);
        if (!file.exists() || file.isFile()) {
            file.mkdir();
        }
        File file1 = new File(headImageRootPath);
        if (!file1.exists()) {
            file.mkdir();
        }
        System.out.println("头像根目录已创建imageRootPath: " + imageRootPath);
        System.out.println("贴子图片根目录已创建headImageRootPath: " + headImageRootPath);
    }
}
