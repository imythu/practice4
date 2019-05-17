package top.imyth.practice4;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
@MapperScan("top.imyth.practice4.dao")
public class Practice4Application {

	public static void main(String[] args) {
		System.out.println(new File("").getAbsolutePath());
		SpringApplication.run(Practice4Application.class, args);
	}

}
