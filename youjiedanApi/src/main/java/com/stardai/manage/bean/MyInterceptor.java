package com.stardai.manage.bean;

import com.alibaba.fastjson.JSON;
import com.stardai.manage.constants.Constants;
import com.stardai.manage.service.UserService;
import com.stardai.manage.utils.JiguangSMS;
import com.sun.tools.internal.jxc.ap.Const;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @program: ujiedan
 * @Date: 2018/7/25 16:30
 * @Author: Tina
 * @Description:
 */

@Component
public class MyInterceptor implements HandlerInterceptor{


    protected static final Logger LOG = LoggerFactory.getLogger(MyInterceptor.class);
    @Autowired
    private UserService userService;

    /**
     * 微服务间接口访问密钥验证
     *
     * @author xiaochangwei
     */


        private Logger logger = LoggerFactory.getLogger(getClass());

        @Autowired
        StringRedisTemplate stringRedisTemplate;

        @Override
        public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
                throws Exception {
        }

        @Override
        public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
                throws Exception {
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj)
                throws Exception {

           String url = request.getRequestURL().toString();
           if(url.contains(".html") || url.contains(".js") || url.contains(".css") || url.contains(".png")|| url.contains(".txt")) {
               return true;
           }
            //header方式
            String token = request.getHeader("MianZeToken");
            String userId = request.getHeader("userId");
            String path = request.getServletPath();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");

            String method = request.getMethod();
            if (method.equals("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                if (!path.equals("/MZuser/registerForNewEdition") && !path.equals("/MZuser/login") && !path.equals("/MZuser/send")
                        && !path.contains("MZevent") && !path.contains("MZversion")&& !path.contains("MZshare") && !path.contains("MZFunctionCount")
                        && !path.contains("MZloanUserNew") && !path.contains("efund")&& !path.contains("forgetPassword") && !path.contains("findPd")
                        && !path.contains("loginH5") && !path.contains("sendForPassword")&& !path.contains("reset")&& !path.contains("Oss")
                         && !path.equals("/MZcity/queryCityList")  && !path.contains("webHooks")&& !path.contains("payRecharge")
                        && !path.equals("/MZorder/queryOrdersByManyConditions")&&!path.equals("/MZorder/getFloatButtonInfo") && !path.contains("error")&& !path.contains("ossToken")&& !path.contains("pay")
                        ) {
                    if (StringUtils.isBlank(token)) {
                        LOG.error("参数1错误--" +path);
                        response.getWriter().write(JSON.toJSONString(new ResponseModel(2, "参数1错误", null)));
                        return false;
                    }
                    if (StringUtils.isBlank(userId) || "null".equals(userId)) {
                        LOG.error("参数2错误--" +path);
                        response.getWriter().write(JSON.toJSONString(new ResponseModel(2, "参数2错误", null)));
                        return false;
                    }
                    else {
                        String password = userService.getPasswordByUserId(userId);
                        if (password == null) {
                            LOG.error("该user_id不存在--" +path);
                            response.getWriter().write(JSON.toJSONString(new ResponseModel(2, "该user_id不存在", null)));
                            return false;
                        }
                        //从数据库取到token
                        String savedToken = userService.getTokenByUserId(userId);
                        if (!token.equals(savedToken)) {
                            LOG.info("您的账号在其它设备上登录，如非本人操作，请及时修改密码！--" +path);
                            response.getWriter().write(JSON.toJSONString(new ResponseModel(3, "您的账号在其它设备上登录，如非本人操作，请及时修改密码！", null)));
                            return false;
                        }
                        //查看是否把此用户加入到黑名单
                        HashMap<String,Integer> punishCodeAndStatus = userService.getPunishCodeByUserId(userId);
                        Integer punishCode = punishCodeAndStatus.get("punishCode");
                        Integer punishmentStatus =  punishCodeAndStatus.get("punishmentStatus");
                        if(punishmentStatus == 0){
                            return true;
                        }else{
                            if(punishCode.equals(Constants.PutInBlackForVerifyCodeLogin)){
                                LOG.error("您的账号存在异常，请通过短信验证码登录！--" +path);
                                response.getWriter().write(JSON.toJSONString(new ResponseModel(4, "您的账号存在异常，请通过短信验证码登录！", null)));
                                return false;
                            }else if(punishCode.equals(Constants.PutInBlackForCancelApprove)){
                                LOG.error("账号被取消实名认证，请返回登录界面重新登录！--" +path);
                                response.getWriter().write(JSON.toJSONString(new ResponseModel(4, "账号异常，请返回登录界面重新登录！", null)));
                                return false;
                            }else if(punishCode.equals(Constants.PutInBlackForForbidLogin)){
                                LOG.error("账号被封号，请返回登录界面重新登录！--" +path);
                                response.getWriter().write(JSON.toJSONString(new ResponseModel(4, "系统监测到您的账号存在违反优接单注册及使用协议的行为，该账号已被禁止登录。如有疑问，请与客服取得联系。", null)));
                                return false;
                            }else {
                               return true;
                            }

                        }

                    }
                }
            }
            return true;
        }

}