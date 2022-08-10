package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.*;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.service.PostService;
import com.helloworld.helloworldweb.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostQueryDslTest {
    @Autowired
    PostService postService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostImageRepository postImageRepository;
    @Autowired UserRepository userRepository;

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
    public void 이미지가_있는_게시물_조회() throws IOException {
        //given
        Post post = Post.builder()
                .category(Category.BLOG)
                .title("BLOG")
                .content("hello my name is Jihun")
                .postImages(new ArrayList<>())
                .tags("TEST")
                .build();

        String[] testImages = new String[]{"http://123412341234TestImage.png"};

        User testUser = userRepository.findUserWithPostByEmail(testEmail).get();
        Post addPost = postService.addPost(post, testUser, testImages);

        //then
        Post findPost = postRepository.findPostWithImagesById(addPost.getId()).orElseThrow((()->new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")));

        //when
        assertThat(findPost.getContent()).isEqualTo("hello my name is Jihun");
        assertThat(findPost.getPostImages().get(0).getStoredUrl()).isEqualTo("http://123412341234TestImage.png");
    }
}
