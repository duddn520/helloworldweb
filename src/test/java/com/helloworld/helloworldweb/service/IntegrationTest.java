//package com.helloworld.helloworldweb.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.helloworld.helloworldweb.controller.PostController;
//import com.helloworld.helloworldweb.domain.Category;
//import com.helloworld.helloworldweb.domain.Post;
//import com.helloworld.helloworldweb.domain.User;
//import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
//import com.helloworld.helloworldweb.repository.PostRepository;
//import com.helloworld.helloworldweb.repository.UserRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@Transactional //테스트 반복을 위해 한 트랜잭션 후에 롤백함.
//@AutoConfigureMockMvc
//public class IntegrationTest {
//
//    @Autowired UserService userService;
//    @Autowired PostService postService;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    PostRepository postRepository;
//    @Autowired
//    PostController postController;
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Test
//    void 회원가입() {
//        //given
//        User user = new User();
//
//        //when
//        User registerUser = userService.registerUser(user);
//
//        //then
//        User savedUser = userRepository.findById(registerUser.getId()).get();
//        assertThat(user.getId()).isEqualTo(savedUser.getId());
//    }
//
//    @Test
//    void 게시물작성() throws Exception {
//        //given
//        User user = new User();
//        User savedUser = userService.registerUser(user);
//        String content = objectMapper.writeValueAsString(
//                PostRequestDto.builder()
//                        .user_id(savedUser.getId())
//                        .category(Category.BLOG)
//                        .content("hello my name is jihun")
//                        .build());
//
//        //when
//        mockMvc.perform(
//                post("/api/post")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//                )
//
//        //then
//                .andExpect(status().isOk());
//        List<Post> posts = postRepository.findByUserIdAndCategory(savedUser.getId(), Category.BLOG).get();
//        assertThat(posts.size()).isEqualTo(1);
//    }
//
//    @Test
//    void 게시물삭제() throws Exception {
//        //given
//        User user = new User();
//        User savedUser = userService.registerUser(user);
//        PostRequestDto savedpostRequestDto = PostRequestDto.builder()
//                .user_id(savedUser.getId())
//                .category(Category.BLOG)
//                .content("hello my name is jihun")
//                .build();
//        Post savedpost = postService.registerPost(savedpostRequestDto, savedUser);
//
//        String content = objectMapper.writeValueAsString(
//                PostRequestDto.builder()
//                        .post_id(savedpost.getId())
//                        .build());
//
//        //when
//        mockMvc.perform(
//                delete("/api/post")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//                )
//
//        //then
//                .andExpect(status().isOk());
//
//        Optional<Post> deletePost = postRepository.findById(savedpost.getId());
//        Assertions.assertFalse(deletePost.isPresent());
//    }
//
//    @Test
//    void QNA전체조회() throws Exception  {
//        //given
//        User user = new User();
//        User savedUser = userService.registerUser(user);
//        PostRequestDto savedpostRequestDto = PostRequestDto.builder()
//                .user_id(savedUser.getId())
//                .category(Category.QNA)
//                .content("I have a question")
//                .build();
//        Post savedQna = postService.registerPost(savedpostRequestDto, savedUser);
//
//        String content = objectMapper.writeValueAsString(
//                PostRequestDto.builder()
//                        .post_id(savedQna.getId())
//                        .build());
//
//        //when
//        mockMvc.perform(
//                get("/api/post/qna")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//        )
//
//        //then
//                .andExpect(status().isOk());
//        List<Post> qnas = postRepository.findByCategory(Category.QNA).get();
//        assertThat(qnas.get(0)).isEqualTo(savedQna);
//    }
//
//    @Test
//    void 사용자의블로그조회() throws Exception {
//        //given
//        User user = new User();
//        User savedUser = userService.registerUser(user);
//        PostRequestDto savedBlogRequestDto = PostRequestDto.builder()
//                .user_id(savedUser.getId())
//                .category(Category.BLOG)
//                .content("hello my name is jihun")
//                .build();
//        Post savedblog = postService.registerPost(savedBlogRequestDto, savedUser);
//        PostRequestDto savedQnaRequestDto = PostRequestDto.builder()
//                .user_id(savedUser.getId())
//                .category(Category.QNA)
//                .content("I have a question")
//                .build();
//        Post savedqna = postService.registerPost(savedQnaRequestDto, savedUser);
//
//        String content = objectMapper.writeValueAsString(
//                PostRequestDto.builder()
//                        .user_id(savedUser.getId())
//                        .build());
//
//        //when
//        mockMvc.perform(
//                get("/api/post/blog")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//        )
//
//        //then
//                .andExpect(status().isOk());
//
//        List<Post> blogs = postRepository.findByUserIdAndCategory(savedUser.getId(), Category.BLOG).get();
//        assertThat(savedblog).isEqualTo(blogs.get(0));
//    }
//
//}
