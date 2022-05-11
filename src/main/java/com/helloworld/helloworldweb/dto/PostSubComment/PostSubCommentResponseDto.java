package com.helloworld.helloworldweb.dto.PostSubComment;

import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.dto.User.UserResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class PostSubCommentResponseDto {

    private Long id;
    private String content;
    private String createdTime;
    private UserResponseDto userResponseDto;

    public PostSubCommentResponseDto (PostSubComment postSubComment)
    {
        this.id = postSubComment.getId();
        this.content = postSubComment.getContent();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postSubComment.getCreatedTime());
        this.userResponseDto = new UserResponseDto(postSubComment.getUser());
    }
}
