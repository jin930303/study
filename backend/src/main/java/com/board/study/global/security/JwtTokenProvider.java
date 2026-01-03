package com.board.study.global.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    //실제 서비스에선 환경변수로 관리
    private final String secretKey = "YourSecretKeyForJWTTokenGenerationShouldBeLongEnough";
    private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
    private final Long expirationTime = 1000L * 60 * 60; //1시간

    //만료 시간 설정
    private final long accessTokenExpiration = 1000L * 60 *30; //30분
    private final long refreshTokenExpiration = 1000L * 60 * 60 * 24 * 7; //7일

    //Access Token 생성
    public String createAccessToken(String loginId){
        return createToken(loginId,accessTokenExpiration);
    }

    //Refresh Token 생성
    public String createRefreshToken(String loginId){
        return createToken(loginId,refreshTokenExpiration);
    }

    //토큰 생성
    public String createToken(String loginId, long validityInMilliseconds){
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //토큰에서 아이디 추출
    public String getLoginId(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    //토큰 유효성 검증
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
