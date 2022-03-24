package com.helloworld.helloworldweb.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class PostComment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "postComment", cascade = CascadeType.ALL)
    private List<PostSubComment> postSubComments = new ArrayList<>();

}
