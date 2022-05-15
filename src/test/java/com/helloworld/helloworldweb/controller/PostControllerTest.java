package com.helloworld.helloworldweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.UserRepository;
import com.helloworld.helloworldweb.service.PostService;
import com.helloworld.helloworldweb.service.UserService;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional //테스트 반복을 위해 한 트랜잭션 후에 롤백함.
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostController postController;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    String testEmail = "ys05143@naver.com/";
//    String testEmail = "test@email.com";

    private String getToken() {
        return jwtTokenProvider.createToken(testEmail, Role.USER);
    }

//    @BeforeEach
//    void 회원가입() {
//        User testUser = User.builder()
//                    .email(testEmail)
//                    .repo_url(" ")
//                    .profileUrl(" ")
//                    .nickName(testEmail)
//                    .role(Role.USER)
//                    .posts(new ArrayList<>())
//                    .build();
//        userRepository.save(testUser);
//    }

    @Test
    void 게시물작성() throws Exception {
        //given
        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("title", "BLOG");
        requestParam.set("category", "BLOG");
        requestParam.set("tags", "TEST");
        requestParam.set("content", "hello my name is Jihun");

        //when
        MvcResult result = mockMvc.perform(
                        post("/api/post")
                                .params(requestParam)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .header("Auth", getToken())
                )

        //then
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        JSONParser p = new JSONParser();
        JSONObject obj = (JSONObject)p.parse(content);
        JSONObject data = (JSONObject) p.parse(obj.get("data").toString());
        Post savedPost = postService.getPost(Long.valueOf(String.valueOf(data.get("id"))));

        assertThat(savedPost.getTitle()).isEqualTo("BLOG");
        assertThat(savedPost.getContent()).isEqualTo("hello my name is Jihun");
    }

    @Test
    void 게시물삭제() throws Exception {
        //given
        Post newPost = Post.builder()
                .category(Category.BLOG)
                .title("BLOG")
                .content("hello my name is Jihun")
//                .postImages(new ArrayList<>())
                .tags("TEST")
                .build();
        String testUserJwt = getToken();
        User user = userService.getUserByJwt(testUserJwt);

        Post savedpost = postService.addPost(newPost, user);

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("post_id", savedpost.getId().toString());

        //when
        mockMvc.perform(
                delete("/api/post")
                        .params(requestParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                )

        //then
                .andExpect(status().isOk());
    }

    @Test
    void QNA전체조회() throws Exception  {
        //given
        String testUserJwt = getToken();
        User user = userService.getUserByJwt(testUserJwt);

        Post newQna = Post.builder()
                .category(Category.QNA)
                .title("QNA")
                .content("I have a question")
//                .postImages(new ArrayList<>())
                .tags("TEST")
                .build();
        Post savedQna = postService.addPost(newQna, user);

        Post newBlog = Post.builder()
                .category(Category.BLOG)
                .title("BLOG")
                .content("hello my name is Jihun")
//                .postImages(new ArrayList<>())
                .tags("TEST")
                .build();
        Post savedBlog = postService.addPost(newBlog, user);

        //when
        mockMvc.perform(
                get("/api/post/qnas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
                .andExpect(status().isOk());
        List<Post> qnas = postRepository.findByCategory(Category.QNA).get();
        //이미 저장된 블로그까지 불러오므로 local DB 에서 create 했을때만 사용
//        assertThat(qnas.toArray().length).isEqualTo(1);
        assertThat(savedQna).isEqualTo(qnas.get(qnas.toArray().length - 1 ));
    }

    @Test
    void 사용자의블로그조회() throws Exception {
        //given
        String testUserJwt = getToken();
        User user = userService.getUserByJwt(testUserJwt);

        Post newQna = Post.builder()
                .category(Category.QNA)
                .title("QNA")
                .content("I have a question")
//                .postImages(new ArrayList<>())
                .tags("TEST")
                .build();
        Post savedQna = postService.addPost(newQna, user);

        Post newBlog = Post.builder()
                .category(Category.BLOG)
                .title("BLOG")
                .content("hello my name is Jihun")
//                .postImages(new ArrayList<>())
                .tags("TEST")
                .build();
        Post savedBlog = postService.addPost(newBlog, user);

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("email", testEmail);

        //when
        mockMvc.perform(
                get("/api/post/blogs")
                        .params(requestParam)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .header("Auth",testUserJwt)
        )

        //then
                .andExpect(status().isOk());

        List<Post> blogs = postRepository.findByUserIdAndCategory(user.getId(), Category.BLOG).get();
        //이미 저장된 블로그까지 불러오므로 local DB 에서 create 했을때만 사용
//        assertThat(blogs.toArray().length).isEqualTo(1);
        assertThat(savedBlog).isEqualTo(blogs.get(blogs.toArray().length - 1));
    }

    @Test
    void 아미지를_포함한_게시물등록() throws Exception {
        //given
        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("title", "title");
        requestParam.set("category", "BLOG");
        requestParam.set("tags", "TEST");
        requestParam.set("content", "hello my name is Jihun\n&&&&\n");

        MockMultipartFile file = new MockMultipartFile("files",
                "TestImage.png",
                "image/png",
                //이미지경로는 각 로컬에서 수정해야함.
                new FileInputStream("/Users/heojihun/Project/helloworldweb/src/test/java/com/helloworld/helloworldweb/controller/TestImage.png"));

        //when
        MvcResult result = mockMvc.perform(
                        multipart("/api/post")
                                .file(file)
                                .params(requestParam)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .characterEncoding("UTF-8")
                                .header("Auth", getToken())
                )

        //then
                .andExpect(status().isOk())
                .andReturn();

        //S3에 저장된 테스트 이미지파일 삭제
        String content = result.getResponse().getContentAsString();
        JSONParser p = new JSONParser();
        JSONObject obj = (JSONObject)p.parse(content);
        JSONObject data = (JSONObject) p.parse(obj.get("data").toString());
        Post deleteTargetPost = postService.getPost(Long.valueOf(String.valueOf(data.get("id"))));
        postService.deletePost(deleteTargetPost);
    }

    @Test
    void 게시물_하나_조회() throws Exception {
        //given
        Post newPost = Post.builder()
                .category(Category.BLOG)
                .title("BLOG")
                .content("hello my name is Jihun")
//                .postImages(new ArrayList<>())
                .postComments(new ArrayList<>())
                .tags("TEST")
                .build();
        String testUserJwt = getToken();
        User user = userService.getUserByJwt(testUserJwt);

        Post savedpost = postService.addPost(newPost, user);

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("post_id", savedpost.getId().toString());

        //when
        MvcResult result = mockMvc.perform(
                        get("/api/post")
                                .params(requestParam)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .header("Auth", getToken())
                )

        //then
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        JSONParser p = new JSONParser();
        JSONObject obj = (JSONObject)p.parse(content);
        JSONObject data = (JSONObject) p.parse(obj.get("data").toString());
        Post savedPost = postService.getPost(Long.valueOf(String.valueOf(data.get("id"))));

        assertThat(savedPost.getTitle()).isEqualTo("BLOG");
        assertThat(savedPost.getContent()).isEqualTo("hello my name is Jihun");

    }


}