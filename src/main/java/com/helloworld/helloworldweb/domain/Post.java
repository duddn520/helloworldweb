package com.helloworld.helloworldweb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Post {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postComments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    private String content;

    @Builder
    public Post(Long id, User user, List<PostComment> postComments, Category category, String content) {
        this.id = id;
        this.user = user;
        this.postComments = postComments;
        this.category = category;
        this.content = content;
    }

    public void updateUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }

}
