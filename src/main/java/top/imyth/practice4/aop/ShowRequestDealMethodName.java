package top.imyth.practice4.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class ShowRequestDealMethodName {
    private static final Logger logger = LoggerFactory
        .getLogger(ShowRequestDealMethodName.class);

    @Pointcut("execution(public * top.imyth.practice4.controller..*.*(..))")
    public void point() {}

    @Before("point()")
    public void showRequestDealMethodName(JoinPoint joinPoint) {
        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        //代理的是哪一个方法
        System.out.println("方法："+signature.getName());
        //AOP代理类的名字
        System.out.println("方法所在包:"+signature.getDeclaringTypeName());
        //AOP代理类的类（class）信息
        signature.getDeclaringType();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] strings = methodSignature.getParameterNames();
        System.out.println("参数名："+ Arrays.toString(strings));
        System.out.println("参数值ARGS : " + Arrays.toString(joinPoint.getArgs()));
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        // 记录下请求内容
        logger.debug("请求URL : " + req.getRequestURL().toString());
        logger.debug("HTTP_METHOD : " + req.getMethod());
        logger.debug("IP : " + req.getRemoteAddr());
        logger.debug("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    }

    @AfterReturning(returning = "ret", pointcut = "point()")
    public void showMethodResult(Object ret) {
        // 处理完请求，返回内容
        logger.debug("方法的返回值 : " + ret);
    }
}
