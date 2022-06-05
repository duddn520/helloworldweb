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

    @ManyToMany
    private List<User> engagingUserList = new ArrayList<>();

    private boolean selected;

    @Builder
    public PostComment(Long id, Post post, boolean selected)
    {
        this.id = id;
        this.post = post;
        this.postSubComments = new ArrayList<>();
        this.selected = selected;
        this.engagingUserList = new ArrayList<>();
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

    public void updateEngagingUserList(User user)
    {
        //engageUserList 에 이미 등록된 유저면 등록하지 않는다.
        if(!this.engagingUserList.contains(user))
        {
            this.engagingUserList.add(user);
        }
    }

}
