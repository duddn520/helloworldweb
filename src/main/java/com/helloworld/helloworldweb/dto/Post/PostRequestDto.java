package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto { //post요청과 함께 받을 데이터
    private Long post_id;
    private Long user_id;
    private Category category;
    private String content;
    private String title;
    private String tags;

    public Post toEntity() {
        return Post.builder()
                .id(post_id)
                .category(category)
                .title(title)
                .content(content)
                .tags(tags)
                .build();
    }

    @Builder
    public PostRequestDto(Long post_id, Long user_id, Category category, String content, String title, String tags) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.category = category;
        this.content = content;
        this.title = title;
        this.tags = tags;
    }
}
