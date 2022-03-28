package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("api/post")
    @ResponseBody
    public ResponseEntity<ApiResponse> registerPost(@RequestHeader("user_id") Long user_id, @RequestBody PostRequestDto requestDto) {

        Post post = postService.registerPost(requestDto, user_id);
        PostResponseDto responseDto = new PostResponseDto(post);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);
    }

    @GetMapping("api/post")
    @ResponseBody
    public ResponseEntity<ApiResponse> listPost(@RequestHeader("user_id") Long user_id) {

        List<Post> posts = postService.listPost(user_id);
        List<PostResponseDto> responses = posts.stream()
                                            .map(PostResponseDto::new)
                                            .collect(Collectors.toList());

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.GET_SUCCESS,
                responses), HttpStatus.OK);
    }

    @DeleteMapping("api/post")
    @ResponseBody
    public ResponseEntity<ApiResponse> deletePost(@RequestHeader("user_id") Long user_id, @RequestParam("post_id") Long id) {

        postService.deletePost(user_id, id);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }
}
