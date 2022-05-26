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
import static org.mockito.ArgumentMatchers.*;
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
        assertThat(savedPost).isEqualTo(post);
        assertThat(savedPost.getUser()).isEqualTo(user);
    }
    @Test
    @DisplayName("addPost url 포함 성공한 케이스")
    void addPost_withImg_success() throws IOException {
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
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            urls.add("http://test-server/123TestImage.png");
        }

        //when
        when(fileProcessService.getFileName(any(String.class))).thenReturn("123TestImage.png");
        when(postImageRepository.save(any(PostImage.class))).thenReturn(null);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        Post savedPost = postService.addPost(post, user, urls.toArray(new String[0]));

        //then
        assertThat(5).isEqualTo(savedPost.getPostImages().size());
        assertThat("http://test-server/123TestImage.png").isEqualTo(savedPost.getPostImages().get(0).getStoredUrl());
        assertThat(user).isEqualTo(savedPost.getUser());
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
        Throwable exception = assertThrows(NullPointerException.class, () -> {
            postService.addPost(post, user, null);
        });

        //then
        assertEquals("Post 저장에 실패했습니다.", exception.getMessage());
    }
    @Test
    @DisplayName("addPost url 포함 실패한 케이스")
    void addPost_withImg_fail() throws IOException {
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
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            urls.add("http://test-server/123TestImage.png");
        }

        //when
        when(fileProcessService.getFileName(any(String.class))).thenReturn("123TestImage.png");
        when(postImageRepository.save(any(PostImage.class))).thenReturn(null);
        when(postRepository.save(any(Post.class))).thenReturn(null);

        Throwable exception = assertThrows(NullPointerException.class, () -> {
            postService.addPost(post, user, urls.toArray(new String[0]));
        });

        //then
        assertEquals("Post 저장에 실패했습니다.", exception.getMessage());
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
                .storedFileName("123oldTestImage.png")
                .storedUrl("www.naver.com123oldTestImage.png")
                .build();
        oldPostImage.updatePost(post);

        String newTitle = "new!!";
        String newContent = "this is new content!";
        String newTags = "NEW";

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            urls.add("www.naver.com123newTestImage.png");
        }

        PostImage newPostImage = PostImage.builder()
                .originalFileName("IMAGE"+post.getId())
                .storedFileName("123newTestImage.png")
                .storedUrl("www.naver.com123newTestImage.png")
                .build();

        //when
        when(fileProcessService.getFileName(any(String.class))).thenReturn("123newTestImage.png");
        when(postImageRepository.save(any(PostImage.class))).thenReturn(newPostImage);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post savedPost = postService.updatePost(post, newTitle, newContent, newTags, urls.toArray(new String[0]));

        //then
        assertThat(savedPost.getTitle()).isEqualTo(newTitle);
        assertThat(savedPost.getContent()).isEqualTo(newContent);
        assertThat(savedPost.getTags()).isEqualTo(newTags);
        assertThat(savedPost.getPostImages().size()).isEqualTo(2);
        assertThat(savedPost.getPostImages().get(0).getStoredUrl()).isEqualTo("www.naver.com123newTestImage.png");

    }
    @Test
    @DisplayName("Post 완전히 새로 수정 했을 때 post 저장 실패")
    void updatePost_fail() throws IOException {
        //given
        Post post = Post.builder()
                .title("hello")
                .content("my name is Jihun")
                .category(Category.BLOG)
                .tags("test")
                .build();
        PostImage oldPostImage = PostImage.builder()
                .storedFileName("123oldTestImage.png")
                .storedUrl("www.naver.com123oldTestImage.png")
                .build();
        oldPostImage.updatePost(post);

        String newTitle = "new!!";
        String newContent = "this is new content!";
        String newTags = "NEW";

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            urls.add("www.naver.com123newTestImage.png");
        }

        PostImage newPostImage = PostImage.builder()
                .originalFileName("IMAGE"+post.getId())
                .storedFileName("123newTestImage.png")
                .storedUrl("www.naver.com123newTestImage.png")
                .build();

        //when
        when(fileProcessService.getFileName(any(String.class))).thenReturn("123newTestImage.png");
        when(postImageRepository.save(any(PostImage.class))).thenReturn(newPostImage);
        when(postRepository.save(any(Post.class))).thenReturn(null);

        Throwable exception = assertThrows(NullPointerException.class, () -> {
            postService.updatePost(post, newTitle, newContent, newTags, urls.toArray(new String[0]));
        });

        //then
        assertEquals("Post 수정에 실패했습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("게시물 제목, 내용으로 검색")
    void searchPost_withTitleAndContent() {
        //given
        List<Post> postList = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            Post post = Post.builder()
                    .title("제목 react")
                    .content("my name is Jihun")
                    .category(Category.BLOG)
                    .tags("test")
                    .build();
            postList.add(post);
        }
        for(int i = 0; i < 50; i++){
            Post post = Post.builder()
                    .title("title")
                    .content("내용 native")
                    .category(Category.BLOG)
                    .tags("test")
                    .build();
            postList.add(post);
        }

        //when
        when(postRepository.findAllByTitleContainingOrContentContaining(anyString(), anyString())).thenReturn(Optional.of(postList));
        List<Post> findPosts = postService.getSearchedPost("react native");

        //then
        assertThat(findPosts.size()).isEqualTo(100);
    }
    @Test
    @DisplayName("태그 모두 검색")
    void searchPost_withTags() {
        //given
        List<Post> postList_tag = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            Post post = Post.builder()
                    .title("title")
                    .content("content")
                    .category(Category.BLOG)
                    .tags("react, java")
                    .build();
            postList_tag.add(post);
        }

        //when
        when(postRepository.findAllByTagsContaining(anyString())).thenReturn(Optional.of(postList_tag));
        List<Post> findPosts = postService.getSearchedPost("%react%");

        //then
        assertThat(findPosts.size()).isEqualTo(50);
    }


}
