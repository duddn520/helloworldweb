package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.dto.PostComment.PostCommentRequestDto;
import com.helloworld.helloworldweb.dto.PostComment.PostCommentResponseDto;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    //PostComment CRUD

//    @PostMapping("/api/postcomment")
//    public ResponseEntity<ApiResponse<PostCommentResponseDto>> registerPostComment(@RequestBody PostCommentRequestDto postCommentRequestDto)
//    {
//        //TODO 지훈이 POST부분 완성 후 포스트 아이디 받고 포스트 찾아서 포스트코멘트 객체 주입, 서브코멘트도 생성해서 서브코멘트 1 에 박기.
//
//        return new ResponseEntity<>(ApiResponse.response(
//                HttpStatusCode.OK,
//                HttpResponseMsg.GET_SUCCESS,
//                responseDto), HttpStatus.OK);
//    }

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
