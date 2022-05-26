package com.helloworld.helloworldweb.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GuestBookCommentTest {

    private GuestBookComment guestBookComment;
    private GuestBook guestBook;
    @BeforeEach
    void setUp(){
        this.guestBook = GuestBook.builder()
                .build();
        this.guestBookComment = GuestBookComment.builder()
                .content("test_content")
                .guestBook(guestBook)
                .reply("test_reply")
                .build();
    }

    @Test
    void changeGuestBook() {
        GuestBook newGuestBook = GuestBook.builder().build();
        guestBookComment.changeGuestBook(newGuestBook);

        assertThat(guestBookComment.getGuestBook()).isNotEqualTo(guestBook);
        assertThat(guestBookComment.getGuestBook()).isEqualTo(newGuestBook);
    }

    @Test
    void updateGuestBookComment() {
        GuestBookComment newGuestBookComment = GuestBookComment.builder().reply("change_reply").build();
        guestBookComment.updateGuestBookComment(newGuestBookComment);

        assertThat(guestBookComment.getReply()).isEqualTo("change_reply");
    }

    @Test
    void builder() {
       assertThat(guestBookComment.getId()).isNull();
       assertThat(guestBookComment.getUser()).isNull();
       assertThat(guestBookComment.getContent()).isEqualTo("test_content");
       assertThat(guestBookComment.getReply()).isEqualTo("test_reply");
       assertThat(guestBookComment.getGuestBook()).isEqualTo(guestBook);
    }

}