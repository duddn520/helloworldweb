package com.helloworld.helloworldweb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PostSubComment extends BaseTimeEntity {

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

    public void updatePostComment(PostComment postComment)
    {
        this.postComment = postComment;
        postComment.getPostSubComments().add(this);
    }

    public void updateUser(User user)
    {
        this.user = user;
        user.getSubComments().add(this);
    }

    public PostSubComment updatePostSubComment(PostSubComment postSubComment)
    {
        this.content = postSubComment.getContent();
        return this;
    }

    public PostSubComment updateContent(String content){
        this.content = content;
        return this;
    }

    public void delete(){
        // 연관관계 모두 삭제
        this.postComment.getPostSubComments().clear();
        this.postComment = null;
        this.user = null;
    }


}
