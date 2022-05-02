package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.dto.PostComment.PostCommentResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDtoWithPostComments {
    private Long id;
    private Long user_id;
    private Category category;
    private String title;
    private String content;
    private String tags;
    private LocalDateTime createdTime;
    private List<PostCommentResponseDto> postCommentResponseDtos = new ArrayList<>();

    public PostResponseDtoWithPostComments(Post post) {
        this.id = post.getId();
        this.user_id = post.getUser().getId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tags = post.getTags();
        this.createdTime = post.getCreatedTime();
        this.postCommentResponseDtos = postCommentsToResponseDtos(post.getPostComments());
    }

    //post의 PostComment List를 PostCommentResponseDto의 List로 바꿔주는함수.
    private List<PostCommentResponseDto> postCommentsToResponseDtos(List<PostComment> postComments)
    {
        List<PostCommentResponseDto> responseDtos = new ArrayList<>();
        for( PostComment p : postComments)
        {
            responseDtos.add(new PostCommentResponseDto(p));
        }
        return responseDtos;
    }
}
