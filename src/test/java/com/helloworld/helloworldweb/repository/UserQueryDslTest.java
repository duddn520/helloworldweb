package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserQueryDslTest {

    @Autowired
    UserService userService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository userRepository;

    String testEmail = "test@email.com";

    private String getToken() {
        return jwtTokenProvider.createToken(testEmail, Role.USER);
    }

    @BeforeEach
    public void 회원가입() {
         User testUser = User.builder()
                .email(testEmail)
                .repo_url(" ")
                .profileUrl(" ")
                .nickName(testEmail)
                .role(Role.USER)
                .posts(new ArrayList<>())
                .fcm("123")
                .build();
        userRepository.save(testUser);
    }

    @Test
    public void Post포함한User조회_성공(){
        User findUserPost = userRepository.findUserWithPostByEmail(testEmail).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        assertThat(findUserPost.getEmail()).isEqualTo(testEmail);
    }

    @Test
    public void Post포함한User조회_실패(){

        Throwable exception = assertThrows(NoSuchElementException.class, () -> {
            userRepository.findUserWithPostByEmail(" ").orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        });

        //then
        assertEquals("존재하지 않는 유저입니다.", exception.getMessage());
    }
}
