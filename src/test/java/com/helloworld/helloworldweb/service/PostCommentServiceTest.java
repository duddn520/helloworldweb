package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.*;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional //테스트 반복을 위해 한 트랜잭션 후에 롤백함.
@PersistenceContext
public class PostCommentServiceTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired PostCommentService postCommentService;

    String testEmail = "ys05143@naver.com/";
//    String testEmail = "test@email.com";

    private String getToken() {
        return jwtTokenProvider.createToken(testEmail, Role.USER);
    }

    @BeforeEach
    void 회원가입_게시물작성() throws IOException {

//        User testUser = User.builder()
//                .email(testEmail)
//                .repo_url(" ")
//                .profileUrl(" ")
//                .nickName(testEmail)
//                .role(Role.USER)
//                .build();
//        userService.addUser(testUser);

        Post newPost = Post.builder()
                .category(Category.BLOG)
                .title("BLOG")
                .content("hello my name is Jihun")
                .tags("TEST")
                .build();

        String testUserJwt = getToken();
        User user = userService.getUserByJwt(testUserJwt);

        postService.addPost(newPost, user, null);

    }

//    @Test
//    void 게시물_댓글_작성() {
//        //given
//        User findUser = userService.getUserByEmail(testEmail);
//        List<Post> blogs = postService.getAllUserPosts(findUser.getId(), Category.BLOG);
//        Post targetPost = blogs.get(blogs.size() - 1 );
//        System.out.println("targetPost = " + targetPost.getContent());
//
//        PostComment newPostComment = PostComment.builder()
//                .build();
//        PostSubComment newPostSubComment = PostSubComment.builder()
//                .content("hi this is comment")
//                .build();
//
//        //when
//        PostComment savedPostComment = postCommentService.registerPostComment(newPostComment, targetPost, newPostSubComment, findUser);
//
//        //then
//        System.out.println("savedPostComment = " + savedPostComment);
//    }
}
