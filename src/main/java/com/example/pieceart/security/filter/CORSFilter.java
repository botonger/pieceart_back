package com.example.pieceart.security.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//cors 필터
@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       response.setHeader("Access-Control-Allow-Origin", "https://d3fvuov9hophwk.cloudfront.net");
       response.setHeader("Access-Control-Allow-Credentials", "true");
       response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
       response.setHeader("Access-Control-Max-Age", "3600");
       response.setHeader("Access-Control-Allow-Headers",
               "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");

       if("OPTIONS".equalsIgnoreCase(request.getMethod())){
           response.setStatus(HttpServletResponse.SC_OK);
       } else{
           filterChain.doFilter(request, response);
       }
    }
}


