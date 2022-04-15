package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.PostService;
import com.helloworld.helloworldweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping("/api/post")
    @Transactional
    public ResponseEntity<ApiResponse> registerPost(@RequestBody PostRequestDto requestDto) {

        User findUser = userService.getUserById(requestDto.getUser_id());

        Post post = requestDto.toEntity();
        postService.addPost(post, findUser);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/api/post/myblogs")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getUserBlog(@RequestBody PostRequestDto requestDto) {

        List<Post> myBlogs = postService.getAllUserPosts(requestDto.getUser_id(), Category.BLOG);

        List<PostResponseDto> responseDtos = myBlogs.stream()
                                            .map(PostResponseDto::new)
                                            .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }

    @GetMapping("/api/post/qnas")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getAllQna() {

        List<Post> qnas = postService.getAllPost(Category.QNA);

        List<PostResponseDto> responseDtos = qnas.stream()
                                            .map(PostResponseDto::new)
                                            .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }

    @DeleteMapping("/api/post")
    public ResponseEntity<ApiResponse> deletePost(@RequestBody PostRequestDto requestDto) {

        Post findPost = postService.getPost(requestDto.getPost_id());

        postService.deletePost(findPost);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.DELETE_SUCCESS,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/api/post")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPost(@RequestBody PostRequestDto requestDto) {

        Post post = postService.getPost(requestDto.getPost_id());
        PostResponseDto responseDto = new PostResponseDto(post);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDto), HttpStatus.OK);
    }

}
