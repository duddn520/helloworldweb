package com.helloworld.helloworldweb.domain;

import com.helloworld.helloworldweb.dto.PostComment.PostCommentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class PostComment extends BaseTimeEntity{

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL)
    private List<PostSubComment> postSubComments = new ArrayList<>();

    private boolean selected;

    @Builder
    public PostComment(Long id, Post post, boolean selected)
    {
        this.id = id;
        this.post = post;
        this.postSubComments = new ArrayList<>();
        this.selected = selected;
    }

    public void updatePost(Post post)
    {
        post.getPostComments().add(this);
        this.post = post;
    }


    public void selectPostComment()
    {
        this.selected = true;
    }

    public PostComment removePostSubComment(PostSubComment postSubComment){
        // 마지막 댓글인 경우
        if( this.postSubComments.size() == 1 ){
            this.post.getPostComments().remove(this);
            this.post = null;
        } else { // 일반적인 경우
            this.postSubComments.remove(postSubComment);
            postSubComment.removePostComment();
        }
        return this;
    }

}
