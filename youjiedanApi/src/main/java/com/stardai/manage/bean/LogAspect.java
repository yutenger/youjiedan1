package com.stardai.manage.bean;

import com.stardai.manage.utils.JSONUtils;
import com.stardai.manage.utils.VeDate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import com.stardai.manage.config.Log;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @program: ujiedan
 * @Date: 2018/8/10 16:40
 * @Author: Tina
 * @Description:
 */
@Aspect
@Configuration
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.stardai.manage.config.Log)")
    public void pointCut() {

    }

    @Before("pointCut()")
    public void doBefore(JoinPoint joinPoint) {
    }

    @After("pointCut()")
    public void doAfter() {

    }
    @Around("pointCut()")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        long startTime=System.currentTimeMillis();
        Object obj = proceedingJoinPoint.proceed();
        long endTime=System.currentTimeMillis();
        saveLog(proceedingJoinPoint,obj,endTime-startTime);
        return obj;
    }
    @AfterReturning(returning = "object", pointcut = "pointCut()")
    public void doAfterRetuning(Object object) {

    }
    @AfterThrowing(throwing="ex",pointcut="pointCut()")
    public void doAfterThrowing(Exception ex){
        logger.error(ex.getMessage());
    }

    void saveLog(ProceedingJoinPoint joinPoint,Object result,long time) throws InterruptedException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);
        String operation="";
        if (log != null) {
            // 注解上的描述
            operation=log.value();
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        String params ="";
        String responseMsg=JSONUtils.beanToJson(result);
        // 请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            StringBuilder sb=new StringBuilder("");
            if(args!=null&&args.length!=0){
                for(Object o:args){
                    if(o instanceof HttpServletRequest ||o instanceof HttpServletResponse || o instanceof BindingResult){
                        continue;
                    }
                    sb.append(","+JSONUtils.beanToJson(o)+" ");
                }
                params=sb.toString().substring(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("时间:"+VeDate.getStringDate()+" 类名:"+className+" 方法名:"+methodName+" 操作:"+operation+" 参数:"+params+" 返回结果:"+responseMsg+" 花费时间："+time +"毫秒");
    }

}
