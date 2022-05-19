package com.helloworld.helloworldweb.dto.PostComment;

import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.service.PostCommentService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCommentRequestDto {
    private Long id;
    private Long postId;
    private String content;

    public PostCommentRequestDto(Long postId, String content)
    {
        this.postId = postId;
        this.content = content;
    }
}
