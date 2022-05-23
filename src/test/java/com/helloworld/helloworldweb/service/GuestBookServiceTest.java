package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.GuestBook;
import com.helloworld.helloworldweb.domain.GuestBookComment;
import com.helloworld.helloworldweb.repository.guestbook.GuestBookCommentRepository;
import com.helloworld.helloworldweb.repository.guestbook.GuestBookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuestBookServiceTest {

    @Mock
    GuestBookCommentRepository guestBookCommentRepository;

    @InjectMocks
    GuestBookService guestBookService;

    @Test
    @DisplayName("방명록_삭제")
    void deleteGuestBookComment(){
        // given : 삭제하려고 하는 방명록 생성
        GuestBook guestBook = GuestBook.builder()
                .build();
        GuestBookComment guestBookComment = GuestBookComment.builder()
                .content("왔다감.")
                .build();
        guestBookComment.changeGuestBook(guestBook);
        Long id = guestBookComment.getId();
        // when : 삭제
        boolean b = guestBookService.deleteGuestBookComment(id);
        // then : 삭제됐는 지 확인
        Assertions.assertEquals(b,true);

    }
}
