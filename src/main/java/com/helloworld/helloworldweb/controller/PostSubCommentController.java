package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.PostSubComment.PostSubCommentRequestDto;
import com.helloworld.helloworldweb.dto.PostSubComment.PostSubCommentResponseDto;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.PostCommentService;
import com.helloworld.helloworldweb.service.PostSubCommentService;
import com.helloworld.helloworldweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class PostSubCommentController {

    private final PostCommentService postCommentService;
    private final PostSubCommentService postSubCommentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Transactional
    @PostMapping("/api/postsubcomment")
    public ResponseEntity<ApiResponse<PostSubCommentResponseDto>> registerPostSubComment(@RequestBody PostSubCommentRequestDto requestDto, HttpServletRequest request)
    {
        String token = jwtTokenProvider.getTokenByHeader(request);
        User user = userService.getUserByEmail(jwtTokenProvider.getUserEmail(token));

        PostComment postComment = postCommentService.getPostCommentById(requestDto.getPostCommentId());

        PostSubComment postSubComment = requestDto.toEntity();

        PostSubCommentResponseDto responseDto = new PostSubCommentResponseDto(postSubCommentService.registerPostSubComment(postSubComment,postComment,user));

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

    // 특정 유저가 작성한 답변들만 조회
    @GetMapping("/user/comments")
    public ResponseEntity<ApiResponse<List<PostSubCommentResponseDto>>> getUserComments(@RequestParam(name= "id") Long id){
        List<PostSubComment> findAllUserComments = postSubCommentService.getAllUserComments(id);

        // List -> ResponseDto
        List<PostSubCommentResponseDto> responseDtos = findAllUserComments.stream()
                .map(PostSubCommentResponseDto::new)
                .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }
}
