package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PostResponseDto { // post결과로 전달할 데이터
    private Long id;
    private Long user_id;
    private Category category;
    private String content;
    private String title;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.user_id = post.getUser().getId();
        this.category = post.getCategory();
        this.content = post.getContent();
        this.title = post.getTitle();
    }
}
