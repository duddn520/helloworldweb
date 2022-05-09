package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
public class PostServiceTest {

    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired UserRepository userRepository;
    @Autowired UserService userService;
    @Autowired PostService postService;

    String testEmail = "helloworldtest@gmail.com";

    @BeforeEach
    private void 유저생성(){
//        String token = jwtTokenProvider.createToken(testEmail, Role.USER);
        User user = User.builder()
                .email(testEmail)
                .role(Role.USER)
                .posts(new ArrayList<>())
                .build();
        userService.addUser(user);
    }

    @Test
    void 게시물_모두검색(){
        // 유저 생성
        User testUser = userService.getUserByEmail(testEmail);

        // 100개의 Post 생성
        // 제목안에 "react" ,내용안에 "react"
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("제목 react")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser);
        }
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("")
                    .content("내용 react")
                    .build();
            Post addPost = postService.addPost(newPost, testUser);
        }

        // when - "react" 로 검색
        List<Post> findPosts = postService.getSearchedPost("react");

        // then - 100개의 게시물이 있는 지
        Assertions.assertEquals(100,findPosts.size());
    }
    @Test
    void 게시물_중복삽입방지(){
        // 유저 생성
        User testUser = userService.getUserByEmail(testEmail);

        // 100개의 Post 생성
        // "react native"
        for (int i = 0; i < 100; i++) {
            Post newPost = Post.builder()
                    .title("react native")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser);
        }

        // when - "react native" 로 검색
        List<Post> findPosts = postService.getSearchedPost("react native");

        // then - 100개의 게시물이 있는 지
        Assertions.assertEquals(100,findPosts.size());

    }

    @Test
    void 게시물_여러키워드로검색(){
        // 유저 생성
        User testUser = userService.getUserByEmail(testEmail);

        // 100개의 Post 생성
        // "react"만 포함,"native"만 포함
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("제목 react")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser);
        }
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("")
                    .content("내용 native")
                    .build();
            Post addPost = postService.addPost(newPost, testUser);
        }

        // when - "react native" 로 검색
        List<Post> findPosts = postService.getSearchedPost("react native");

        // then - 100개의 게시물이 있는 지
        Assertions.assertEquals(100,findPosts.size());
    }

    @Test
    void 게시물_태그로검색(){
        // 유저 생성
        User testUser = userService.getUserByEmail(testEmail);

        // 100개의 Post 생성
        // tag 내용 안 "react" 포함
        for (int i = 0; i < 100; i++) {
            Post newPost = Post.builder()
                    .title("")
                    .content("")
                    .tags("react,")
                    .build();
            Post addPost = postService.addPost(newPost, testUser);
        }

        // when - "[react]" 로 검색
        List<Post> findPosts = postService.getSearchedPost("%react% 질문");

        // then - 100개의 게시물이 있는 지
        Assertions.assertEquals(100,findPosts.size());
    }

    @Test
    void 게시물_정확한문구로검색(){
        // 유저 생성
        User testUser = userService.getUserByEmail(testEmail);

        // 100개의 Post 생성
        // "react"포함 ,"react native"포함
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("react")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser);
        }
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("react and native")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser);
        }

        // when - " "react naitve" " 로 검색
        List<Post> findPosts = postService.getSearchedPost("\"react and native\"");

        // then - 50개의 게시물이 있는 지 ( "react native"만 포함하는 게시물만 결과로 나와야 함 )
        Assertions.assertEquals(50,findPosts.size());
    }

}
