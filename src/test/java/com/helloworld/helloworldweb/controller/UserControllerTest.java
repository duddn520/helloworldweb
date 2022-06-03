package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.repository.UserRepository;
import com.helloworld.helloworldweb.service.FileProcessService;
import com.helloworld.helloworldweb.service.UserService;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
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
    @Autowired
    MockMvc mockMvc;
    @Autowired
    FileProcessService fileProcessService;
    @Autowired
    UserService userService;

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

    @Test
    @DisplayName("이미지 업로드 성공")
    void updateUserProfilMusic_success() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file",
                "테스트음악.png",
                "audio/mp3",
                //이미지경로는 각 로컬에서 수정해야함.
                new FileInputStream("/Users/heojihun/Downloads/Over_the_Horizon.mp3"));

        //when
        MvcResult result = mockMvc.perform(
                        multipart("/api/user/music")
                                .file(file)
                                .param("id", userService.getUserByEmail(testEmail).getId().toString())
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Auth", generateAccessToken())
                )

        //then
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        JSONParser p = new JSONParser();
        JSONObject obj = (JSONObject)p.parse(content);
        String url = (String) obj.get("data");
        String fileName = fileProcessService.getFileName(URLDecoder.decode(url, "UTF-8"));
        fileProcessService.deleteMusic(fileName);
    }
}
