package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.dto.PostComment.PostCommentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
public class PostResponseDto { // post결과로 전달할 데이터
    private Long id;
    private Long user_id;
    private Category category;
    private String title;
    private String content;
    private String tags;
    private Long views;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.user_id = post.getUser().getId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tags = post.getTags();
        this.views = post.getViews();
        this.createdTime = post.getCreatedTime();
        this.modifiedTime = post.getModifiedTime();
    }
}
