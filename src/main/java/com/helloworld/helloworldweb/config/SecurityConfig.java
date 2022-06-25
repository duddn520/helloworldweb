package com.helloworld.helloworldweb.config;

import com.helloworld.helloworldweb.jwt.JwtAuthenticationFilter;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import lombok.Builder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider)
    {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("user123")).roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .httpBasic().disable()
                .csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/user/register/**").permitAll()
                .antMatchers("/api/search").permitAll()
                .antMatchers("/api/post/qnasPage").permitAll()
                .antMatchers("/api/user/getnewtoken").permitAll()
                .antMatchers("/api/post/top-questions").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}


