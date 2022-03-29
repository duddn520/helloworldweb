package com.helloworld.helloworldweb.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class GuestBookComment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "guest_book_id")
    private GuestBook guestBook;

    private String title;
    private String content;

    @Builder
    public GuestBookComment(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public GuestBookComment changeGuestBook(GuestBook guestBook){
        this.guestBook = guestBook;
        guestBook.getGuestBookComments().add(this);
        return this;
    }


}
