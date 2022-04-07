package com.helloworld.helloworldweb.dto.PostSubComment;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostSubComment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSubCommentRequestDto {
    private Long id;
    private Long postCommentId;
    private String content;

    public PostSubComment toEntity(){
        return PostSubComment.builder()
                .content(content)
                .build();
    }

}