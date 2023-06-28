package com.gientech.iot.gateway.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gientech.iot.gateway.bean.TokenUser;
import com.gientech.iot.user.api.pojo.vo.UserLoginVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: Token工具类
 * @author: 白剑民
 * @dateTime: 2022-10-17 16:09:06
 */
public class TokenUtils implements Serializable {

    private static final long serialVersionUID = -3L;

    private static final Long EXPIRATION = 24 * 60 * 60L;

    /**
     * @param user 用户信息
     * @description: 生成 Token 字符串
     * @author: 白剑民
     * @date: 2022-10-17 21:42:47
     * @return: String
     * @version: 1.0
     */
    public static String createToken(UserLoginVO user) {
        // 转换成tokenUser生成token字符串
        TokenUser tokenUser = JSONObject.parseObject(JSON.toJSONString(user), TokenUser.class);
        // Token 的过期时间
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        return Jwts.builder()
                // // 设置 Token 签发者 可选
                // .setIssuer("gientech")
                // 根据用户名设置 Token 的接受者
                .setAudience(user.getUsername())
                // 设置过期时间
                .setExpiration(expirationDate)
                // 设置 Token 生成时间 可选
                .setIssuedAt(new Date())
                // 通过 claim 方法设置一个 key = user，value = userId 的值
                .claim("user", JSON.toJSONString(tokenUser))
                // 设置加密密钥和加密算法，注意要用私钥加密且保证私钥不泄露
                .signWith(RsaUtils.getPrivateKey(), SignatureAlgorithm.RS256).compact();
    }

    /**
     * @param token Token 字符串
     * @description: 验证 Token ，并获取到用户名和用户权限信息
     * @author: 白剑民
     * @date: 2022-10-17 21:42:18
     * @return: User
     * @version: 1.0
     */
    public static TokenUser validationToken(String token) {
        try {
            // 解密 Token，获取 Claims 主体
            Claims claims = Jwts.parserBuilder()
                    // 设置公钥解密，以为私钥是保密的，因此 Token 只能是自己生成的，如此来验证 Token
                    .setSigningKey(RsaUtils.getPublicKey()).build().parseClaimsJws(token).getBody();
            if (claims == null) {
                return null;
            }
            // 验证 Token 有没有过期 过期时间
            Date expiration = claims.getExpiration();
            // 判断是否过期 过期时间要在当前日期之后
            if (!expiration.after(new Date())) {
                return null;
            }
            Object userObject = claims.get("user");
            return JSONObject.parseObject((userObject.toString()), TokenUser.class);
        } catch (Exception e) {
            return null;
        }
    }
}
