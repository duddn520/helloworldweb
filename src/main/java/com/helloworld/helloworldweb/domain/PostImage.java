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

    @NotNull
    private String originalFileName;
    @NotNull
    private String storedFileName;
    @NotNull
    private String storedUrl;

    private Long fileSize;

    @Builder
    public PostImage(Long id, Post post, String originalFileName, String storedFileName, String storedUrl, Long fileSize) {
        this.id = id;
        this.post = post;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.storedUrl = storedUrl;
        this.fileSize = fileSize;
    }

    public void updatePost(Post post) {
        this.post = post;
        post.getPostImages().add(this);
    }
}
