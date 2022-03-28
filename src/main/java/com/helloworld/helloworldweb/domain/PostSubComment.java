package com.helloworld.helloworldweb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PostSubComment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_comment_id")
    private PostComment postComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @Builder
    public PostSubComment(Long id, PostComment postComment, User user, String content)
    {
        this.id = id;
        this.postComment = postComment;
        this.user = user;
        this.content = content;
    }

}
