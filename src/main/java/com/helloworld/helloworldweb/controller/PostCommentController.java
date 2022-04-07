package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.dto.PostComment.PostCommentRequestDto;
import com.helloworld.helloworldweb.dto.PostComment.PostCommentResponseDto;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.PostCommentService;
import com.helloworld.helloworldweb.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;
    private final PostService postService;

    //PostComment CRUD

    @Transactional
    @PostMapping("/api/postcomment")
    public ResponseEntity<ApiResponse<PostCommentResponseDto>> registerPostComment(@RequestBody PostCommentRequestDto postCommentRequestDto)
    {
        Post post = postService.findPost(postCommentRequestDto.getPostId());
        PostComment postComment = PostComment.builder()
                .build();
        PostSubComment postSubComment = PostSubComment.builder()
                .content(postCommentRequestDto.getContent())
                .build();

        PostCommentResponseDto responseDto = new PostCommentResponseDto(postCommentService.registerPostComment(postComment,post,postSubComment));

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

    //update는 필요 없을듯.

    @DeleteMapping("/api/postcomment")
    public ResponseEntity<ApiResponse> deletePostComment(@RequestParam(name = "id") Long id)
    {
        PostComment postComment = postCommentService.getPostCommentById(id);
        postCommentService.deletePostComment(postComment);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);

    }


}
