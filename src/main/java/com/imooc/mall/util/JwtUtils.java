package com.imooc.mall.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.imooc.mall.exception.ImoocException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.vo.CurrentUserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
  @Value("${jwt.secret}")
  private String JWT_SECRET;

  @Value("${jwt.expiration_time}")
  private Long EXPIRATION_TIME;

  @Value("${jwt.prefix}")
  private String prefix;

  public static void main(String[] args) {
  }

  public Map<String, Object> generateToken(CurrentUserVO user) {
    JWTCreator.Builder builder = JWT.create();
    Date expireAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

    String token = builder
        .withClaim("username", user.getUsername())
        .withClaim("userId", user.getId())
        .withClaim("email", user.getEmailAddress())
        .withClaim("role", user.getRole())
        .withExpiresAt(expireAt)
        .sign(Algorithm.HMAC256(JWT_SECRET));

    String accessToken = prefix + " " + token;
    Map<String, Object> result = new HashMap<>();

    result.put("user", user);
    result.put("accessToken", accessToken);
    result.put("expireAt", expireAt);

    return result;
  }

  public boolean validate(String accessToken) {
    if (StringUtils.isEmpty(accessToken)) {
      return false;
    }
    String token = accessToken;
    if (accessToken.startsWith(prefix)) {
      token = accessToken.substring(prefix.length());
    }

    token = token.trim();

    try {
      JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public CurrentUserVO decodeToken(String accessToken) {
    if (StringUtils.isEmpty(accessToken)) {
      throw new ImoocException(ImoocMallExceptionEnum.INVALID_TOKEN);
    }
    String token = accessToken;
    if (accessToken.startsWith(prefix)) {
      token = accessToken.substring(prefix.length());
    }

    token = token.trim();


    try {
      JWTVerifier verifier = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
      DecodedJWT jwt = verifier.verify(token);
      Integer userId = jwt.getClaim("userId").asInt();
      String username = jwt.getClaim("username").asString();
      Integer role = jwt.getClaim("role").asInt();
      String email = jwt.getClaim("email").asString();
      CurrentUserVO userVO = new CurrentUserVO();

      userVO.setEmailAddress(email);
      userVO.setId(userId);
      userVO.setUsername(username);
      userVO.setRole(role);
      return userVO;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }
}
