package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.server.ExportException;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository userRepository;

    String testEmail = "test@email.com";

    private String generateAccessToken(){ return jwtTokenProvider.createToken(testEmail, Role.USER);}
    private String generateRefreshToken(){ return jwtTokenProvider.createRefreshToken(testEmail,Role.USER);}
    private String generateExpiredToken(){ return jwtTokenProvider.createExpiredToken(testEmail,Role.USER);}



    @BeforeEach
    void registerUser() {
        User testUser = User.builder()
                .email(testEmail)
                .repo_url(" ")
                .profileUrl(" ")
                .nickName(testEmail)
                .role(Role.USER)
                .posts(new ArrayList<>())
                .build();
        userRepository.save(testUser);
    }

    @Test
    void getNewTokenByExpiredAccessToken() throws Exception{

        HttpHeaders accessTokenExpiredHeader = new HttpHeaders();
        accessTokenExpiredHeader.add("Auth",generateExpiredToken());
        accessTokenExpiredHeader.add("Refresh",generateRefreshToken());
        mvc.perform(
                get("/api/user/getnewtoken")
                        .headers(accessTokenExpiredHeader))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Auth"));


    }

    @Test
    void getNewTokenByExpiredBothToken() throws Exception{
        HttpHeaders twoExpiredHeader = new HttpHeaders();
        twoExpiredHeader.add("Auth",generateExpiredToken());
        twoExpiredHeader.add("Refresh",generateExpiredToken());
        mvc.perform(
                get("/api/user/getnewtoken")
                        .headers(twoExpiredHeader))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Auth"));

    }

    @Test
    void getNewTokenByNoExpiredToken() throws Exception{
        HttpHeaders noExpiredHeader = new HttpHeaders();
        noExpiredHeader.add("Auth",generateAccessToken());
        noExpiredHeader.add("Refresh",generateRefreshToken());
        mvc.perform(
                get("/api/user/getnewtoken")
                        .headers(noExpiredHeader))
                .andExpect(status().isServiceUnavailable())
                .andExpect(header().doesNotExist("Auth"));
    }
}
