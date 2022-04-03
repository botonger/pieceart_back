package com.example.pieceart.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

//JWT 토큰 관련 클래스 (토큰 생성, 유효여부, 헤더 추출)
@Log4j2
@Component
public class JWTUtil{
    //secret key 설정
    private SecretKey key;
    @Value("${jwt.secret}")
    public void setSecretKey(String myKey) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(myKey));
    }

    private long expire = 60 * 24 * 1; //토큰 유효기간 설정 (test 1일)

    //토큰 생성하기
    public String generateToken(String sub, Set<String> roles){
        Claims claims = Jwts.claims().setSubject(sub);
        claims.put("role", roles);
        log.info(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()));
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setClaims(claims)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .signWith(key)
                .compact();
    }

    //토큰 유효여부 체크 (true or false)
    public boolean validateToken(String jwtToken) {
        Jws<Claims> claims = getClaimsFromToken(jwtToken);
        log.info("-------validateToken---------");
        log.info(claims);
        if(claims!= null) log.info(claims.getBody().getExpiration());
        if(claims != null) return claims.getBody().getExpiration().after(new Date());
        return false;
    }

    //토큰 추출
    public String extractTokenFromHeader(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: "+authHeader);
            return authHeader.split("Bearer ")[1];
        }
        return null;
    }

    //claims 추출
    public Jws<Claims> getClaimsFromToken(String jwtToken) throws ExpiredJwtException, MalformedJwtException{
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken);
            return claims;
        } catch(ExpiredJwtException e){
            log.error("Token expired");
            return null;
        } catch(MalformedJwtException e){
            log.error("Malformed token");
            return null;
        }
    }
}
