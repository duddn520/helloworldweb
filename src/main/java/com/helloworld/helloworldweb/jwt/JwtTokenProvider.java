package com.helloworld.helloworldweb.jwt;

import com.helloworld.helloworldweb.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    private String secretKey = "HELLO_WORLD_WEB";

    private static final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60 * 10; //10시간
    private static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //JWT토큰생성
    public String createToken(String email, Role role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getUserEmail(String token)
    {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean verifyToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            long valid_time = claims.getBody().getExpiration().getTime() - claims.getBody().getIssuedAt().getTime();
            // refresh_token인지 확인
            return valid_time > ACCESS_TOKEN_VALID_TIME ? false : !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Object getRole(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role");
    }

    public String getTokenByHeader(HttpServletRequest request) {
        return request.getHeader("Auth");
    }



}
