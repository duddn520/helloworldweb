package com.helloworld.helloworldweb.domain;

import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class GuestBook {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "guestBook")
    private List<GuestBookComment> guestBookComments = new ArrayList<GuestBookComment>();

    @Builder
    public GuestBook(User user) {
        this.user = user;
    }

    public void changeUser(User user){
        this.user = user;
    }

}
