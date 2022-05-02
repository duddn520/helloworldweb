package com.helloworld.helloworldweb.dto.guestbook;

import com.helloworld.helloworldweb.domain.GuestBookComment;
import com.helloworld.helloworldweb.domain.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class GuestBookResponseDto {
    private Long id;
    private String content;
    private String userName;
    private String createdTime;

    public GuestBookResponseDto(GuestBookComment guestBookComment) {
        this.id = guestBookComment.getId();
        this.content = guestBookComment.getContent();
        this.userName = guestBookComment.getUser().getUsername();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(guestBookComment.getCreatedTime());;
    }
}
