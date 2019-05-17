package top.imyth.practice4.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.imyth.practice4.util.DateAndStringConverter;
import top.imyth.practice4.util.JsonResultKeyValueBuildUtil;
import top.imyth.practice4.util.ParamCheckUtil;

@Configuration
public class UtilConfiguration {

    @Bean("paramCheckUtil")
    public ParamCheckUtil initParamCheckUtil() {
        System.out.println("ParamCheckUtil装配完成");
        return new ParamCheckUtil();
    }

    @Bean("jsonResultKeyValueBuildUtil")
    public JsonResultKeyValueBuildUtil initJsonResultKeyValueBuildUtil() {
        System.out.println("JsonResultKeyValueBuildUtil装配完成");
        return new JsonResultKeyValueBuildUtil();
    }

    @Bean("dateAndStringConverter")
    public DateAndStringConverter initDateAndStringConverter() {
        return new DateAndStringConverter();
    }
}
