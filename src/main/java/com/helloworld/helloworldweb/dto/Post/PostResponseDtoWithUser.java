package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.User.UserResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDtoWithUser {
    private Long id;
    private UserResponseDto userResponseDto;
    private Category category;
    private String title;
    private String content;
    private String tags;
    private Long views;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private int numOfSubComments;

    public PostResponseDtoWithUser(Post post) {
        this.id = post.getId();
        this.userResponseDto = new UserResponseDto(post.getUser());
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tags = post.getTags();
        this.views = post.getViews();
        this.createdTime = post.getCreatedTime();
        this.modifiedTime = post.getModifiedTime();
    }

    public PostResponseDtoWithUser (Post post, int numOfSubComments) {
        this.id = post.getId();
        this.userResponseDto = new UserResponseDto(post.getUser());
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tags = post.getTags();
        this.views = post.getViews();
        this.createdTime = post.getCreatedTime();
        this.modifiedTime = post.getModifiedTime();
        this.numOfSubComments = numOfSubComments;
    }
}
