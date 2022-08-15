package com.helloworld.helloworldweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.UserRepository;
import com.helloworld.helloworldweb.service.FileProcessService;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional //테스트 반복을 위해 한 트랜잭션 후에 롤백함.
@AutoConfigureMockMvc
//Post Integration Test
public class PostControllerTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    FileProcessService fileProcessService;
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

//    String testEmail = "ys05143@naver.com/";
    String testEmail = "test@email.com";

    private String getToken() {
        return jwtTokenProvider.createToken(testEmail, Role.USER);
    }

    @BeforeEach
    void 회원가입() {
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
                .tags("TEST")
                .build();

        User user = userService.getUserWithPostByEmail(testEmail);

        Post savedpost = postService.addPost(newPost, user, null);

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("post_id", savedpost.getId().toString());

        //when
        mockMvc.perform(
                delete("/api/post")
                        .params(requestParam)
                        .header("Auth", getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                )

        //then
                .andExpect(status().isOk());
    }
    @Test
    void 게시물삭제_이미지포함() throws Exception {
        //given
        Post newPost = Post.builder()
                .category(Category.BLOG)
                .title("BLOG")
                .content("hello my name is Jihun")
                .postImages(new ArrayList<>())
                .tags("TEST")
                .build();

        User user = userService.getUserWithPostByEmail(testEmail);

        MockMultipartFile file = new MockMultipartFile("file",
                "TestImage.png",
                "image/png",
                new FileInputStream("/Users/heojihun/Project/helloworldweb/src/test/java/com/helloworld/helloworldweb/controller/TestImage.png"));

        String uploadImageUrl = fileProcessService.uploadImage(file);
        String[] storedUrls = new String[]{uploadImageUrl};
        Post savedpost = postService.addPost(newPost, user, storedUrls);

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("post_id", savedpost.getId().toString());

        //when
        mockMvc.perform(
                        delete("/api/post")
                                .params(requestParam)
                                .header("Auth", getToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                )

                //then
                .andExpect(status().isOk());
    }


    @Test
    void 게시물_하나_조회() throws Exception {
        //given
        Post newPost = Post.builder()
                .category(Category.BLOG)
                .title("BLOG")
                .content("hello my name is Jihun")
                .postComments(new ArrayList<>())
                .tags("TEST")
                .build();

        User user = userService.getUserWithPostByEmail(testEmail);

        Post savedpost = postService.addPost(newPost, user, null);

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

    @Test
    void 게시물수정() throws Exception {
        //given
        Post newPost = Post.builder()
                .category(Category.BLOG)
                .title("BLOG")
                .content("hello my name is Jihun")
                .tags("TEST")
                .build();

        User findUser = userService.getUserWithPostByEmail(testEmail);
        Post savedpost = postService.addPost(newPost, findUser, null);

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("post_id", savedpost.getId().toString());
        requestParam.set("title", "newBLog title");
        requestParam.set("tags", "newTest");
        requestParam.set("content", "this is new Content");

        //when
        MvcResult result = mockMvc.perform(
                        put("/api/post")
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

        assertThat(savedPost.getTitle()).isEqualTo("newBLog title");
        assertThat(savedPost.getContent()).isEqualTo("this is new Content");
        assertThat(savedPost.getTags()).isEqualTo("newTest");
    }

    @Test
    void 이미지_한장_등록() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file",
                "TestImage.png",
                "image/png",
                //이미지경로는 각 로컬에서 수정해야함.
                new FileInputStream("/Users/heojihun/Project/helloworldweb/src/test/java/com/helloworld/helloworldweb/controller/TestImage.png"));

        //when
        MvcResult result = mockMvc.perform(
                        multipart("/api/image")
                                .file(file)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .header("Auth", getToken())
                )

                //then
                .andExpect(status().isOk())
                .andReturn();

        //S3에 저장된 테스트 이미지파일 삭제
        String content = result.getResponse().getContentAsString();
        JSONParser p = new JSONParser();
        JSONObject obj = (JSONObject)p.parse(content);
        String url = (String) obj.get("data");
        String fileName = fileProcessService.getFileName(URLDecoder.decode(url, "UTF-8"));
        fileProcessService.deleteImage(fileName);
    }

    @Test
    void 블로그_작성시_지운_이미지들_삭제() throws Exception {
        //given
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            MockMultipartFile file = new MockMultipartFile("file",
                    "테스트이미지" + i + ".png",
                    "image/png",
                    //이미지경로는 각 로컬에서 수정해야함.
                    new FileInputStream("/Users/heojihun/Project/helloworldweb/src/test/java/com/helloworld/helloworldweb/controller/TestImage.png"));
            urls.add(fileProcessService.uploadImage(file));
        }
        String joinUrls = urls.toString().replaceAll("\\[|\\]", "").replaceAll(", ",", ");
        //when
        MvcResult result = mockMvc.perform(
                        delete("/api/image")
                                .param("urls", URLEncoder.encode(joinUrls))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Auth", getToken())
                )

        //then
        .andExpect(status().isOk())
        .andReturn();
    }

    @Test
    void QNA_페이지조회() throws Exception {
        //given
        for(int i =0 ; i< 30; i++){
            Post newPost = Post.builder()
                    .category(Category.QNA)
                    .title("Qna"+i)
                    .content("hello my name is Jihun"+i)
                    .postComments(new ArrayList<>())
                    .tags("TEST")
                    .build();

            User user = userService.getUserWithPostByEmail(testEmail);
            postService.addPost(newPost, user, null);
        }
        for(int i =0 ; i< 30; i++){
            Post newPost = Post.builder()
                    .category(Category.BLOG)
                    .title("BLOG"+i)
                    .content("hello my name is Jihun"+i)
                    .postComments(new ArrayList<>())
                    .tags("TEST")
                    .build();

            User user = userService.getUserWithPostByEmail(testEmail);
            postService.addPost(newPost, user, null);
        }

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("page", "1");

        //when
        MvcResult result = mockMvc.perform(
                        get("/api/post/qnasPage")
                                .params(requestParam)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .header("Auth", getToken())
                )

                //then
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("content = " + content);

    }

    @Test
    void 블로그_페이지조회() throws Exception {
        //given
        for(int i =0 ; i< 30; i++){
            Post newPost = Post.builder()
                    .category(Category.QNA)
                    .title("Qna"+i)
                    .content("hello my name is Jihun"+i)
                    .postComments(new ArrayList<>())
                    .tags("TEST")
                    .build();

            User user = userService.getUserWithPostByEmail(testEmail);
            postService.addPost(newPost, user, null);
        }
        for(int i =0 ; i< 30; i++){
            Post newPost = Post.builder()
                    .category(Category.BLOG)
                    .title("BLOG"+i)
                    .content("hello my name is Jihun"+i)
                    .postComments(new ArrayList<>())
                    .tags("TEST")
                    .build();

            User user = userService.getUserWithPostByEmail(testEmail);
            postService.addPost(newPost, user, null);
        }

        MultiValueMap<String, String> requestParam = new LinkedMultiValueMap<>();
        requestParam.set("email", testEmail);
        requestParam.set("page", "2");

        //when
        MvcResult result = mockMvc.perform(
                        get("/api/post/blogsPage")
                                .params(requestParam)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .header("Auth", getToken())
                )

                //then
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        System.out.println("content = " + content);

    }


}
