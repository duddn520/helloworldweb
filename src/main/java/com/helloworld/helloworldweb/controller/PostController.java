package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/post")
    public ResponseEntity<ApiResponse> registerPost(@RequestBody PostRequestDto requestDto) {

        Post post = postService.registerPost(requestDto);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/api/post/blog")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> listUserBlog(@RequestBody PostRequestDto requestDto) {

        List<Post> blogs = postService.listUserBlog(requestDto);
        List<PostResponseDto> responses = blogs.stream()
                                            .map(PostResponseDto::new)
                                            .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.GET_SUCCESS,
                responses), HttpStatus.OK);
    }

    @GetMapping("/api/post/qna")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> listAllQna() {

        List<Post> qnas = postService.listAllQna();
        List<PostResponseDto> responses = qnas.stream()
                                            .map(PostResponseDto::new)
                                            .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.GET_SUCCESS,
                responses), HttpStatus.OK);
    }

    @DeleteMapping("/api/post")
    public ResponseEntity<ApiResponse> deletePost(@RequestBody PostRequestDto requestDto) {

        Post findPost = postService.findPost(requestDto);
        postService.deletePost(findPost);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }

}
