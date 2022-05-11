package com.helloworld.helloworldweb.dto.PostComment;

import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.dto.PostSubComment.PostSubCommentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostCommentResponseDto {

    private Long id;
    private Long postId;
    private List<PostSubCommentResponseDto> postSubCommentResponseDtos = new ArrayList<>();
    private String createdTime;

    public PostCommentResponseDto(PostComment postComment)
    {
        this.id = postComment.getId();
        this.postId = postComment.getPost().getId();
        this.postSubCommentResponseDtos = postSubCommentsToResponseDtos(postComment.getPostSubComments());
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postComment.getCreatedTime());
    }

    public List<PostSubCommentResponseDto> postSubCommentsToResponseDtos(List<PostSubComment> postSubComments)
    {
        List<PostSubCommentResponseDto> responseDtos = new ArrayList<>();
        for(PostSubComment p : postSubComments)
        {
            PostSubCommentResponseDto responseDto = new PostSubCommentResponseDto(p);
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }




}
