package top.imyth.practice4;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;

// 两个任选其一
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
//@SpringBootApplication

@MapperScan("top.imyth.practice4.dao")
@ServletComponentScan
@EnableTransactionManagement
public class Practice4Application {

	public static void main(String[] args) {
		String imageRootPath = System.getProperty("user.dir") + File.separator + "articleImages" + File.separator;
		String headImageRootPath = System.getProperty("user.dir") + File.separator + "headImage" + File.separator;
		File file = new File(imageRootPath);
		if (!file.exists()) {
			file.mkdir();
		}
		File file1 = new File(headImageRootPath);
		if (!file1.exists()) {
			file.mkdir();
		}
		SpringApplication.run(Practice4Application.class, args);
	}
}
