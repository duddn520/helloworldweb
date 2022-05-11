package com.helloworld.helloworldweb.dto.PostSubComment;

import com.helloworld.helloworldweb.domain.PostSubComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class PostSubCommentResponseDto {

    private Long id;
    private String content;
    private String createdTime;

    public PostSubCommentResponseDto (PostSubComment postSubComment)
    {
        this.id = postSubComment.getId();
        this.content = postSubComment.getContent();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postSubComment.getCreatedTime());
    }
}
