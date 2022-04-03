package com.example.pieceart.security.filter;

import com.example.pieceart.member.MemberRepository;
import com.example.pieceart.security.util.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

//api 접근 시 권한 체크
@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {
    private JWTUtil jwtUtil;
    private AntPathMatcher antPathMatcher;
    private String[] pattern;
    private final MemberRepository memberRepository;

    public ApiCheckFilter(String[] pattern, JWTUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String email = null;
        ArrayList<String> roles = null;
        Collection<? extends GrantedAuthority> auths = null;
        boolean tokenValidation = false;
        UsernamePasswordAuthenticationToken userAuthToken = null;
        log.info("--------ApiCheckFilter-----------");
        log.info(request.getRequestURI());
        log.info(antPathMatcher.match(pattern[2], request.getRequestURI()));

        if(antPathMatcher.match(pattern[0], request.getRequestURI()) || antPathMatcher.match(pattern[1], request.getRequestURI()) || antPathMatcher.match(pattern[2], request.getRequestURI())){
            //토큰 추출
            String token = jwtUtil.extractTokenFromHeader(request);
            log.info("token: "+token);
            //토큰이 있다면 만료여부, 토큰정보 체크
            if(token != null){
                tokenValidation = jwtUtil.validateToken(token);
                log.info("val"+tokenValidation);
                Jws<Claims> claims = jwtUtil.getClaimsFromToken(token);
                if(claims !=null){
                    email = claims.getBody().getSubject();
                    roles = (ArrayList<String>) claims.getBody().get("role");
                    auths = ((ArrayList<?>) claims.getBody().get("role")).stream().map(role->new SimpleGrantedAuthority((String)role)).collect(Collectors.toSet());
                    log.info("auths: "+auths);
                    log.info("roles: "+roles);
                    log.info(claims.getBody().get("role"));
                }
            }
            //만료되었으면 토큰 재발급
            //만료되지 않았다면 권한 체크 (요청 받을 때마다 db체크를 해야할까?)
            if(tokenValidation){
//            Optional<Member> member = memberRepository.findByEmail(email, false);
//            Set<String> roleMember = member.get().getRoleSet().stream().map(role-> "ROLE_"+role.name()).collect(Collectors.toSet());
//            if(member.isPresent() && roles.containsAll(roleMember) && roleMember.containsAll(roles)){
                userAuthToken = new UsernamePasswordAuthenticationToken(email, "", auths);
                log.info("----authenticate---------");
                log.info("userAuthToken: "+userAuthToken);

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(userAuthToken);
                SecurityContextHolder.setContext(context);

                filterChain.doFilter(request, response);
                return;
            } else {
                //토큰이 없거나, 만료되면 코드 401(권한오류) 전송
                if(antPathMatcher.match(pattern[0], request.getRequestURI())){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    JSONObject json = new JSONObject();
                    json.put("code", "401");
                    json.put("message", "NO TOKEN DISCOVERED / EXPIRED TOKEN RECEIVED");
                    PrintWriter out = response.getWriter();
                    out.print(json);
                    return;
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}