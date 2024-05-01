package com.xiancai.lora.utils;



import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Map;

public class JWTUtil {
    private static final String SING="QLMFSKLDFMKLJ@#";
    //1.生成token  head.payload.sing
    public static String createToken(Map<String,Object> map){
        //1.创建验证对象  with header可以省略，省略的话就默认用系统自带的
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR,1);//默认7天过期
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)->{
            if(v instanceof Integer){
                builder.withClaim(k,(Integer)v);
            }else if(v instanceof String){
                builder.withClaim(k,(String) v);
            }
        });
        builder.withExpiresAt(instance.getTime());
        String token = builder.sign(Algorithm.HMAC256(SING));
//        String token = JWT.create()
//                .withClaim("userId", 21)//payload
//                .withClaim("username", "wjk")
//                .withExpiresAt(instance.getTime())//指定令牌过期时间
//                .sign(Algorithm.HMAC256(SING));//签名
        System.out.println(token);
        return  token;
    }

    //验证token合法性

    public static DecodedJWT verify(String token){
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
            return verify;
        } catch (JWTVerificationException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
    public static DecodedJWT getDecodedToken(HttpServletRequest request){
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtil.verify(token);
        return verify;
    }

    //验证身份的
    public static Result authenticated(HttpServletRequest request){
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtil.verify(token);
        String type = verify.getClaim("type").asString();
        if(type.equals("user")){
            return Result.no_permissions(false);
        }
        return null;
    }
}
