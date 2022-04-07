package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.dto.PostSubComment.PostSubCommentRequestDto;
import com.helloworld.helloworldweb.dto.PostSubComment.PostSubCommentResponseDto;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.PostCommentService;
import com.helloworld.helloworldweb.service.PostSubCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostSubCommentController {

    private final PostCommentService postCommentService;
    private final PostSubCommentService postSubCommentService;

    @Transactional
    @PostMapping("/api/postsubcomment")
    public ResponseEntity<ApiResponse<PostSubCommentResponseDto>> registerPostSubComment(@RequestBody PostSubCommentRequestDto requestDto)
    {
        PostComment postComment = postCommentService.getPostCommentById(requestDto.getPostCommentId());

        PostSubComment postSubComment = requestDto.toEntity();

        PostSubCommentResponseDto responseDto = new PostSubCommentResponseDto(postSubCommentService.registerPostSubComment(postSubComment,postComment));

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS,
                responseDto), HttpStatus.OK);
    }

    @PutMapping("/api/postsubcomment")
    public ResponseEntity<ApiResponse<PostSubCommentResponseDto>> updatePostSubComment(@RequestBody PostSubCommentRequestDto requestDto)
    {
        PostSubComment postSubComment = postSubCommentService.getPostSubCommentById(requestDto.getId());

        PostSubCommentResponseDto responseDto = new PostSubCommentResponseDto(postSubComment.updatePostSubComment(requestDto.toEntity()));

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.PUT_SUCCESS,
                responseDto), HttpStatus.OK);
    }

    @GetMapping("/api/postsubcomment")
    public ResponseEntity<ApiResponse<PostSubCommentResponseDto>> getPostSubComment(@RequestParam(name = "id")Long id){
        PostSubCommentResponseDto responseDto = new PostSubCommentResponseDto(postSubCommentService.getPostSubCommentById(id));

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.GET_SUCCESS,
                responseDto), HttpStatus.OK);
    }

    @DeleteMapping("/api/postsubcomment")
    public ResponseEntity<ApiResponse> deletePostSubComment(@RequestParam(name = "id")Long id)
    {
        postSubCommentService.deletePostSubComment(postSubCommentService.getPostSubCommentById(id));

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }
}
