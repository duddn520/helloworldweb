package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.PostSubComment.PostSubCommentRequestDto;
import com.helloworld.helloworldweb.dto.PostSubComment.PostSubCommentResponseDto;
import com.helloworld.helloworldweb.firebase.FirebaseCloudMessageService;
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

import java.io.IOException;
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
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Transactional
    @PostMapping("/api/postsubcomment")
    public ResponseEntity<ApiResponse<PostSubCommentResponseDto>> registerPostSubComment(@RequestBody PostSubCommentRequestDto requestDto, HttpServletRequest request) throws IOException {
        String token = jwtTokenProvider.getTokenByHeader(request);
        User user = userService.getUserByEmail(jwtTokenProvider.getUserEmail(token));

        PostComment postComment = postCommentService.getPostCommentById(requestDto.getPostCommentId());

        PostSubComment postSubComment = requestDto.toEntity();

        PostSubCommentResponseDto responseDto = new PostSubCommentResponseDto(postSubCommentService.registerPostSubComment(postSubComment,postComment,user));

        firebaseCloudMessageService.sendSubCommentNotificationToManyUser(postComment.getEngagingUserList(),user);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS,
                responseDto), HttpStatus.OK);
    }

    @PutMapping("/api/postsubcomment")
    public ResponseEntity<ApiResponse<PostSubCommentResponseDto>> updatePostSubComment(@RequestBody PostSubCommentRequestDto requestDto)
    {
        PostSubCommentResponseDto responseDto = new PostSubCommentResponseDto(postSubCommentService.updatePostSubComment(requestDto.getId(), requestDto.getContent()));

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
        postSubCommentService.deletePostSubComment(id);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.DELETE_SUCCESS), HttpStatus.OK);
    }

}
