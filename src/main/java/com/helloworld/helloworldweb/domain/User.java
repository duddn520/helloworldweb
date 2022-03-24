package com.helloworld.helloworldweb.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostSubComment> subComments = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserHome userHome;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private GuestBook guestBook;

    @OneToMany(mappedBy = "user")
    private List<GuestBookComment> guestBookComments = new ArrayList<>();

}
