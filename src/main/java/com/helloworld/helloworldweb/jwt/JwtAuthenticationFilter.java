package com.helloworld.helloworldweb.jwt;

import com.helloworld.helloworldweb.domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        ((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Headers", "x-requested-with, content-type, Authorization, Auth, token, Refresh, FCM");
        ((HttpServletResponse) response).addHeader("Access-Control-Allow-Methods","GET, HEAD, POST, PUT, DELETE");

        HttpServletResponse resp = (HttpServletResponse) response;

        if (httpRequest.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        String token = ((HttpServletRequest)request).getHeader("Auth");
        response.setCharacterEncoding("UTF-8");

        //토큰 유효성 검증 및 auth객체 생성 후 SecurityContextHolder에 등록.
        if (token != null && jwtTokenProvider.verifyToken(token)) {
            Role role =  Role.valueOf((String)jwtTokenProvider.getRole(token));
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(request, response);
    }
}
