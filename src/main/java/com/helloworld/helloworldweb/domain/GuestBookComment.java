package com.helloworld.helloworldweb.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class GuestBookComment extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    // 작성한 유저
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "guest_book_id")
    private GuestBook guestBook;

    // 방명록
    private String content;
    // 답글
    private String reply;

    @Builder
    public GuestBookComment(Long id, User user, GuestBook guestBook, String content, String reply) {
        this.id = id;
        this.user = user;
        this.guestBook = guestBook;
        this.content = content;
        this.reply = reply;
    }

    public GuestBookComment changeGuestBook(GuestBook guestBook){
        this.guestBook = guestBook;
        guestBook.getGuestBookComments().add(this);
        return this;
    }

    public GuestBookComment updateGuestBookComment(GuestBookComment guestBookComment){
        this.reply = guestBookComment.getReply();
        return this;
    }


}
