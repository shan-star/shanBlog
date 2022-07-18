package com.shan.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    //秘钥[c部分]
    private static final String jwtToken = "123456shan!@yes";

    //创建token
    public static String createToken(Long userId){
        //将userId封装成map[B部分（载荷）]
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken[设置A部分]
                .setClaims(claims) // body数据，要唯一，自行设置[设置B部分]
                .setIssuedAt(new Date()) // 设置签发时间，保证每次的token都是不一样
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000));//设置过期时间：一天的有效时间
        String token = jwtBuilder.compact();
        return token;
    }

    // 解析秘钥、验证token合法性
    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    //测试token
//    public static void main(String[] args) {
//        String token = JWTUtils.createToken(100L);
//        System.out.println(token);
//        Map<String, Object> map = JWTUtils.checkToken(token);
//        System.out.println(map.get("userId"));
//    }
}
