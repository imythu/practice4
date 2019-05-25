package top.imyth.practice4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.io.File;
import java.util.TimeZone;

// 两个任选其一
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
//@SpringBootApplication

@MapperScan("top.imyth.practice4.dao")
@ServletComponentScan
public class Practice4Application {

	public static void main(String[] args) {
		SpringApplication.run(Practice4Application.class, args);
	}
}
