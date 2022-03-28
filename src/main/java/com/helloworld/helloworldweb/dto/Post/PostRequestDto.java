package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class PostRequestDto { //post요청과 함께 받을 데이터
    private Long id;
    private Category category;
    private String content;

    public Post toEntity() {
        return Post.builder()
                .id(id)
                .category(category)
                .content(content)
                .build();
    }

    public PostRequestDto(Category category, String content) {
        this.category = category;
        this.content = content;
    }
}
