package com.helloworld.helloworldweb.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class PostImage {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String originalFileName;
    @NotNull
    private String storedFileName;
    @NotNull
    private String storedUrl;

    @Builder
    public PostImage(Long id, Post post, String originalFileName, String storedFileName, String storedUrl) {
        this.id = id;
        this.post = post;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storedUrl = storedUrl;
    }

    public void updatePost(Post post) {
        this.post = post;
        post.getPostImages().add(this);
    }
}
