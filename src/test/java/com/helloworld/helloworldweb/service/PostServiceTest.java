package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@SpringBootTest
public class PostServiceTest {

    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired UserRepository userRepository;
    @Autowired UserService userService;
    @Autowired PostService postService;
    @Autowired
    PostRepository postRepository;

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
    void 게시물_모두검색() throws IOException {
        // 유저 생성
        User testUser = userService.getUserByEmail(testEmail);

        // 100개의 Post 생성
        // 제목안에 "react" ,내용안에 "react"
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("제목 react")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser, null);
        }
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("")
                    .content("내용 react")
                    .build();
            Post addPost = postService.addPost(newPost, testUser, null);
        }

        // when - "react" 로 검색
        List<Post> findPosts = postService.getSearchedPost("react");

        // then - 100개의 게시물이 있는 지
        Assertions.assertEquals(100,findPosts.size());
    }
    @Test
    void 게시물_중복삽입방지() throws IOException {
        // 유저 생성
        User testUser = userService.getUserByEmail(testEmail);

        // 100개의 Post 생성
        // "react native"
        for (int i = 0; i < 100; i++) {
            Post newPost = Post.builder()
                    .title("react native")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser, null);
        }

        // when - "react native" 로 검색
        List<Post> findPosts = postService.getSearchedPost("react native");

        // then - 100개의 게시물이 있는 지
        Assertions.assertEquals(100,findPosts.size());

    }

    @Test
    void 게시물_여러키워드로검색() throws IOException {
        // 유저 생성
        User testUser = userService.getUserByEmail(testEmail);

        // 100개의 Post 생성
        // "react"만 포함,"native"만 포함
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("제목 react")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser, null);
        }
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("")
                    .content("내용 native")
                    .build();
            Post addPost = postService.addPost(newPost, testUser, null);
        }

        // when - "react native" 로 검색
        List<Post> findPosts = postService.getSearchedPost("react native");

        // then - 100개의 게시물이 있는 지
        Assertions.assertEquals(100,findPosts.size());
    }

    @Test
    void 게시물_태그로검색() throws IOException {
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
            Post addPost = postService.addPost(newPost, testUser, null);
        }

        // when - "[react]" 로 검색
        List<Post> findPosts = postService.getSearchedPost("%react% 질문");

        // then - 100개의 게시물이 있는 지
        Assertions.assertEquals(100,findPosts.size());
    }

    @Test
    void 게시물_정확한문구로검색() throws IOException {
        // 유저 생성
        User testUser = userService.getUserByEmail(testEmail);

        // 100개의 Post 생성
        // "react"포함 ,"react native"포함
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("react")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser, null);
        }
        for (int i = 0; i < 50; i++) {
            Post newPost = Post.builder()
                    .title("react and native")
                    .content("")
                    .build();
            Post addPost = postService.addPost(newPost, testUser, null);
        }

        // when - " "react naitve" " 로 검색
        List<Post> findPosts = postService.getSearchedPost("\"react and native\"");

        // then - 50개의 게시물이 있는 지 ( "react native"만 포함하는 게시물만 결과로 나와야 함 )
        Assertions.assertEquals(50,findPosts.size());
    }

    @Test
    void 금일_조회_상위5개_게시물(){
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime today = LocalDateTime.of(time.getYear(),time.getMonth(),time.getDayOfMonth(),0,0);
        Optional<List<Post>> findQnas = postRepository.findTop5ByCreatedTimeGreaterThanEqualAndCategoryOrderByViewsDesc(today, Category.QNA);
        for ( Post post : findQnas.get()){
            Assertions.assertEquals(Category.QNA,post.getCategory());
            org.assertj.core.api.Assertions.assertThat(post.getCreatedTime()).isAfterOrEqualTo(today);
        }
    }

}
