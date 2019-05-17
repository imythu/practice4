package top.imyth.practice4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.imyth.practice4.util.JsonResultKeyValueBuildUtil;

import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @Autowired
    @Qualifier("jsonResultKeyValueBuildUtil")
    private JsonResultKeyValueBuildUtil jsonResultKeyValueBuildUtil;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Map<String, Integer> globalExceptionHandle(Exception e) {
        e.printStackTrace();
        return jsonResultKeyValueBuildUtil.getResultMapFromInteger(500);
    }
}
