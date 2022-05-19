package com.helloworld.helloworldweb.dto.PostComment;

import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.domain.User;
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
    private boolean selected;
    private String createdTime;

    public PostCommentResponseDto(PostComment postComment)
    {
        this.id = postComment.getId();
        this.postId = postComment.getPost().getId();
        this.postSubCommentResponseDtos = postSubCommentsToResponseDtos(postComment.getPostSubComments());
        this.selected = postComment.isSelected();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postComment.getCreatedTime());
    }

    public PostCommentResponseDto(PostComment postComment, User caller)
    {
        this.id = postComment.getId();
        this.postId = postComment.getPost().getId();
        this.postSubCommentResponseDtos = postSubCommentsToResponseDtos(postComment.getPostSubComments(), caller);
        this.selected = postComment.isSelected();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postComment.getCreatedTime());
    }

    private List<PostSubCommentResponseDto> postSubCommentsToResponseDtos(List<PostSubComment> postSubComments, User caller)
    {
        List<PostSubCommentResponseDto> responseDtos = new ArrayList<>();
        for(PostSubComment p : postSubComments)
        {
            PostSubCommentResponseDto responseDto = new PostSubCommentResponseDto(p,caller);
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    private List<PostSubCommentResponseDto> postSubCommentsToResponseDtos(List<PostSubComment> postSubComments)
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
