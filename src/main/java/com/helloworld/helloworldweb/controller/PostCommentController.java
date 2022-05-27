package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.*;
import com.helloworld.helloworldweb.dto.Post.PostResponseDtoWithPostComments;
import com.helloworld.helloworldweb.dto.PostComment.PostCommentRequestDto;
import com.helloworld.helloworldweb.dto.PostComment.PostCommentResponseDto;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.PostCommentService;
import com.helloworld.helloworldweb.service.PostService;
import com.helloworld.helloworldweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    //PostComment CRUD

    @Transactional
    @PostMapping("/api/postcomment")
    public ResponseEntity<ApiResponse<PostCommentResponseDto>> registerPostComment(@RequestBody PostCommentRequestDto postCommentRequestDto, HttpServletRequest request)
    {
        String token = jwtTokenProvider.getTokenByHeader(request);
        User user = userService.getUserByEmail(jwtTokenProvider.getUserEmail(token));

        Post post = postService.getPost(postCommentRequestDto.getPostId());
        PostComment postComment = PostComment.builder()
                .selected(false)
                .build();
        PostSubComment postSubComment = PostSubComment.builder()
                .content(postCommentRequestDto.getContent())
                .build();

        PostCommentResponseDto responseDto = new PostCommentResponseDto(postCommentService.registerPostComment(postComment,post,postSubComment,user));

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS,
                responseDto), HttpStatus.OK);
    }

    @GetMapping("/api/postcomment")
    public ResponseEntity<ApiResponse<PostCommentResponseDto>> getPostComment(@RequestParam(name = "id")Long id)
    {
        PostComment postComment = postCommentService.getPostCommentById(id);

        PostCommentResponseDto responseDto = new PostCommentResponseDto(postComment);

        return new ResponseEntity<>(ApiResponse.response(
            HttpStatusCode.OK,
            HttpResponseMsg.GET_SUCCESS,
                responseDto), HttpStatus.OK);

    }

    @DeleteMapping("/api/postcomment")
    public ResponseEntity<ApiResponse> deletePostComment(@RequestParam(name = "id") Long id)
    {
        PostComment postComment = postCommentService.getPostCommentById(id);
        postCommentService.deletePostComment(postComment);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);

    }

    @Transactional
    @PostMapping("/api/postcomment/select")
    public ResponseEntity<ApiResponse> selectPostComment(@RequestParam(name="id") Long id, HttpServletRequest request, HttpServletResponse response)
    {
        String token = jwtTokenProvider.getTokenByHeader(request);
        User user = userService.getUserByEmail(jwtTokenProvider.getUserEmail(token));

        PostComment postComment = postCommentService.getPostCommentById(id);
        postCommentService.selectPostComment(postComment);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.PUT_SUCCESS,
                new PostResponseDtoWithPostComments(postComment.getPost(),user)), HttpStatus.OK);
    }


}
