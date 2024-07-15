package com.example.testdpi.Util;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.testdpi.entity.User;
import com.example.testdpi.exception.CustomException;
import com.example.testdpi.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//jwt拦截器:验证前端返回的token
@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger log= LoggerFactory.getLogger(JwtInterceptor.class);
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler){
//        1.从http的header中获取token
        String token=request.getHeader("token");
        if (StrUtil.isBlank(token)){
//            如果没拿到，再去参数里面拿一次试试 /api/user?token=xxx&pageNum=1&pageSize=5
            token=request.getParameter("token");
        }
//        2.开始执行认证
        if (StrUtil.isBlank(token)){
            throw new CustomException("无token,请重新登录！");
        }
//        获取token中的userId，之前生成token时，将userId保存到token里面，作为载荷
        String userId;
        User user;
        try {
//            解码拿到userId
            userId= JWT.decode(token).getAudience().get(0);
//            根据token中的userId查询数据库
            user=userService.findById(Integer.parseInt(userId));
        }catch (Exception e){
            String errMsg="token验证失败，请重新登录！";
            log.error(errMsg+",token="+token,e);
            throw new CustomException(errMsg);
        }
        if (user==null){
            throw new CustomException("用户不存在，请重新登录！");
        }

        try {
//            利用用户密码加签验证token
            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            jwtVerifier.verify(token);//验证token
        }catch (JWTVerificationException e){
            throw new CustomException("token验证失败，请重新登录！");
        }
        return true;
    }

}
