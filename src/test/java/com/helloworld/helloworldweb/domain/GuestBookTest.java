package com.helloworld.helloworldweb.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
class GuestBookTest {

    @Test
    void changeUser() {
        User user = User.builder()
                .email("test@email.com")
                .build();
        GuestBook guestBook = GuestBook.builder()
                .user(user)
                .build();
        User newUser = User.builder()
                .email("test@email.com")
                .build();

        // when
        guestBook.changeUser(newUser);

        // then
        assertThat(guestBook.getUser()).isNotEqualTo(user);
        assertThat(guestBook.getUser()).isEqualTo(newUser);
    }

    @Test
    void builder() {
        User user = User.builder()
                .email("test@email.com")
                .build();
        GuestBook guestBook = GuestBook.builder()
                .user(user)
                .build();

        assertThat(guestBook.getUser()).isEqualTo(user);
        assertThat(guestBook.getGuestBookComments()).isNotNull();
        assertThat(guestBook.getGuestBookComments().size()).isEqualTo(0);
    }
}