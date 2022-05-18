package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostImage;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.PostImageRepository;
import com.helloworld.helloworldweb.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceUnitTest {

    @Mock
    PostRepository postRepository;
    @Mock
    PostImageRepository postImageRepository;
    @Mock
    FileProcessService fileProcessService;

    @InjectMocks
    PostService postService;

    @Test
    @DisplayName("addPost 성공한 케이스")
    void addPost_success() throws IOException {
        //given
        Post post = Post.builder()
                        .title("hello")
                        .content("my name is Jihun")
                        .tags("test")
                        .category(Category.BLOG)
                        .build();
        User user = User.builder()
                        .email("test@email.com")
                        .profileUrl(" ")
                        .posts(new ArrayList<>())
                        .build();
        when(postRepository.save(any(Post.class))).thenReturn(post);

        //when
        Post savedPost = postService.addPost(post, user, null);

        //then
        assertThat(savedPost.getUser()).isEqualTo(user);
    }
    @Test
    @DisplayName("addPost 실패한 케이스")
    void addPost_fail() throws IOException {
        //given
        Post post = Post.builder()
                .title("hello")
                .content("my name is Jihun")
                .tags("test")
                .category(Category.BLOG)
                .build();
        User user = User.builder()
                .email("test@email.com")
                .profileUrl(" ")
                .posts(new ArrayList<>())
                .build();
        when(postRepository.save(any(Post.class))).thenReturn(null);

        //when
        Post savedPost = postService.addPost(post, user, null);

        //then
        assertThat(savedPost).isEqualTo(null);
    }

    @Test
    @DisplayName("getPost 성공한 케이스")
    void getPost_success(){
        //given
        Post post = Post.builder()
                .title("hello")
                .content("my name is Jihun")
                .category(Category.BLOG)
                .tags("test")
                .build();
        Long undefinedId = 100L;
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));

        //when
        Post getPost = postService.getPost(undefinedId);

        //then
        assertThat(getPost).isEqualTo(post);

    }
    @Test
    @DisplayName("getPost 실패한 케이스")
    void getPost_fail(){
        //given
        Long undefinedId = 100L;
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //when, then
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.getPost(undefinedId);
        });
        assertEquals("해당 게시물이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("addPost_WithImage 성공한 케이스")
    void addPost_WithImage_success() throws IOException {
        //given
        Post post = Post.builder()
                .title("hello")
                .content("my name is Jihun")
                .category(Category.BLOG)
                .tags("test")
                .build();
        User user = User.builder()
                .email("test@email.com")
                .profileUrl(" ")
                .posts(new ArrayList<>())
                .build();
        MockMultipartFile file = new MockMultipartFile("files",
                "TestImage.png",
                "image/png",
                //이미지경로는 각 로컬에서 수정해야함.
                new FileInputStream("/Users/heojihun/Project/helloworldweb/src/test/java/com/helloworld/helloworldweb/controller/TestImage.png"));

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] photoEncode = encoder.encode(file.getBytes());
        String fileBase64 = new String(photoEncode, "UTF8");
        String base64ForFront = "data:"+file.getContentType()+";base64,"+fileBase64;

        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file);

        when(fileProcessService.uploadImage(any(MultipartFile.class))).thenReturn("www.naver.com123TestImage.png");
        when(fileProcessService.getFileName(any(String.class))).thenReturn("123TestImage.png");
        when(postImageRepository.save(any(PostImage.class))).thenReturn(null);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        //when
        Post savedPost = postService.addPost(post, user, fileList);

        //then
        assertThat("www.naver.com123TestImage.png").isEqualTo(savedPost.getPostImages().get(0).getStoredUrl());
        assertThat(base64ForFront).isEqualTo(savedPost.getPostImages().get(0).getBase64());
        System.out.println("savedPost.getPostImages().get(0).getBase64() = " + savedPost.getPostImages().get(0).getBase64());
    }
    @Test
    @DisplayName("addPost_WithImage 실패한 케이스")
    void addPost_WithImage_fail() throws IOException {
        //given
        Post post = Post.builder()
                .title("hello")
                .content("my name is Jihun")
                .category(Category.BLOG)
                .tags("test")
                .build();
        User user = User.builder()
                .email("test@email.com")
                .profileUrl(" ")
                .posts(new ArrayList<>())
                .build();
        MockMultipartFile file = new MockMultipartFile("files",
                "TestImage.png",
                "image/png",
                //이미지경로는 각 로컬에서 수정해야함.
                new FileInputStream("/Users/heojihun/Project/helloworldweb/src/test/java/com/helloworld/helloworldweb/controller/TestImage.png"));

        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file);

        when(fileProcessService.uploadImage(any(MultipartFile.class))).thenThrow(
                new IllegalArgumentException(String.format("파일 변환 중 에러가 발생했습니다 (%s)", file.getOriginalFilename())));;

        //when, then
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.addPost(post, user, fileList);
        });
        assertEquals("파일 변환 중 에러가 발생했습니다 (TestImage.png)", exception.getMessage());
    }

    @Test
    @DisplayName("getAllUserPosts 유저post가 없을 때")
    void getAllUserPosts_No(){
        //given
        Long undefinedId = 100L;
        List<Post> emptyList = new ArrayList<>();
        when(postRepository.findByUserIdAndCategory(any(Long.class), any(Category.class))).thenReturn(Optional.empty());

        //when
        List<Post> allUserPosts = postService.getAllUserPosts(undefinedId, Category.BLOG);

        //then
        assertThat(allUserPosts).isEqualTo(emptyList);
    }
    @Test
    @DisplayName("getAllUserPosts 유저post가 있을 때")
    void getAllUserPosts_Yes(){
        //given
        Long undefinedId = 100L;
        List<Post> postList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            Post post = Post.builder()
                    .title("hello")
                    .content("my name is Jihun")
                    .category(Category.BLOG)
                    .tags("test")
                    .build();
            postList.add(post);
        }

        when(postRepository.findByUserIdAndCategory(any(Long.class), any(Category.class))).thenReturn(Optional.of(postList));
        //when
        List<Post> allUserPosts = postService.getAllUserPosts(undefinedId, Category.BLOG);
        //then
        assertThat(allUserPosts.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("updatePostViews 성공")
    void updatePostViews_success() {
        //given
        Post post = Post.builder()
                .title("hello")
                .content("my name is Jihun")
                .category(Category.BLOG)
                .tags("test")
                .build();

        when(postRepository.save(any(Post.class))).thenReturn(post);

        //when
        postService.updatePostViews(post);

        //then
        assertThat(post.getViews()).isEqualTo(1L);
    }

    //해당 모듈을 테스트하는 목적을 명확히 하자 => 테스트할 내용을 하나로 확실하게 정하자
    @Test
    @DisplayName("Post 완전히 새로 수정 했을 때")
    void updatePost_success() throws IOException {
        //given
        Post post = Post.builder()
                .title("hello")
                .content("my name is Jihun")
                .category(Category.BLOG)
                .tags("test")
                .build();
        PostImage oldPostImage = PostImage.builder()
                .originalFileName("oldTestImage.png")
                .storedFileName("123oldTestImage.png")
                .storedUrl("www.naver.com123oldTestImage.png")
                .fileSize(10L)
                .build();
        oldPostImage.updatePost(post);

        String newTitle = "new!!";
        String newContent = "this is new content!";
        String newTags = "NEW";
        MockMultipartFile newfile = new MockMultipartFile("files",
                "newTestImage.png",
                "image/png",
                //이미지경로는 각 로컬에서 수정해야함.
                new FileInputStream("/Users/heojihun/Project/helloworldweb/src/test/java/com/helloworld/helloworldweb/controller/TestImage.png"));

        List<MultipartFile> newFileList = new ArrayList<>();
        newFileList.add(newfile);
        PostImage newPostImage = PostImage.builder()
                .originalFileName("newTestImage.png")
                .storedFileName("123newTestImage.png")
                .storedUrl("www.naver.com123newTestImage.png")
                .fileSize(10L)
                .build();

        doNothing().when(fileProcessService).deleteImage(anyString());
        when(fileProcessService.uploadImage(any(MultipartFile.class))).thenReturn("www.naver.com123newTestImage.png");
        when(fileProcessService.getFileName(any(String.class))).thenReturn("123newTestImage.png");
        when(postImageRepository.save(any(PostImage.class))).thenReturn(newPostImage);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        //when
        Post savedPost = postService.updatePost(post, newTitle, newContent, newTags, newFileList);

        //then
        assertThat(savedPost.getTitle()).isEqualTo(newTitle);
        assertThat(savedPost.getContent()).isEqualTo(newContent);
        assertThat(savedPost.getTags()).isEqualTo(newTags);
        assertThat(savedPost.getPostImages().get(0).getStoredUrl()).isEqualTo("www.naver.com123newTestImage.png");

    }
}
