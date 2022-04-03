package com.example.pieceart.security.config;

import com.example.pieceart.security.filter.ApiCheckFilter;
import com.example.pieceart.security.filter.ApiLoginFilter;
import com.example.pieceart.security.filter.CORSFilter;
import com.example.pieceart.security.handler.ApiLoginFailHandler;
import com.example.pieceart.member.MemberRepository;
import com.example.pieceart.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;


@Configuration
@EnableWebSecurity(debug = false) //production 시스템에는 debug 사용하지 마시오
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final MemberRepository memberRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers("/api/admin/**").hasRole("ADMIN")
                .mvcMatchers("/api/users/**").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(corsFilter(), LogoutFilter.class)
                .addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers(headers->headers.httpStrictTransportSecurity(hsts-> hsts
                        .includeSubDomains(true)
                        .preload(true)
                        .maxAgeInSeconds(31536000)
                ))
//                .logout().logoutUrl("/api/logout").addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(CACHE, COOKIES)))
//                .and()
                .requiresChannel(channel->channel.anyRequest().requiresSecure())
                ;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        DelegatingPasswordEncoder encoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        encoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return encoder;
    }
    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }
    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception{
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil(), memberRepository, passwordEncoder());
        apiLoginFilter.setAuthenticationManager(authenticationManager());
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }
    private static final String[] apiUrls = new String[]{"/api/users/**", "/api/works/*", "/api/admin/notices/**"};
    @Bean
    public ApiCheckFilter apiCheckFilter() throws Exception{
        return new ApiCheckFilter(apiUrls, jwtUtil(), memberRepository);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public CORSFilter corsFilter(){
        return new CORSFilter();
    }

}
