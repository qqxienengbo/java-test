package com.example.testdpi.Util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.testdpi.entity.User;
import com.example.testdpi.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

//用于给登录的用户生成token
@Component
public class JwtTokenUtils {
    private static UserService staticuserService;
    private static final Logger log= LoggerFactory.getLogger(JwtTokenUtils.class);

    @Resource
    private UserService userService;

    @PostConstruct
    public void setUserService(){
        staticuserService=userService;
    }

    //    生成token
    public static String genToken(String userId,String password){
        return JWT.create().withAudience(userId)//将userId保存到token里面，作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(),8))//8小时后，token过期
                .sign(Algorithm.HMAC256(password));//以password作为token的密钥(签名)
    }

    //    获取当前登录的用户信息
    public static User getCurrenUser(){
        String token=null;
        try {
            HttpServletRequest request=((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token=request.getHeader("token");
            if (StrUtil.isBlank(token)){
                token=request.getParameter("token");
            }
            if (StrUtil.isBlank(token)){
                log.error("获取当前登录的token失败，token：{}",token);
                return null;

            }

//            解析token，获取用户的id
            String userId= JWT.decode(token).getAudience().get(0);
            return staticuserService.findById(Integer.valueOf(userId));
        }catch (Exception e){
            log.error("获取当前登录的用户信息失败，token:{}",token,e);
            return null;
        }
    }
}
