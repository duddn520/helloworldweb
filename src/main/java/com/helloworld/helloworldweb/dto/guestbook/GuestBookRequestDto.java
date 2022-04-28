package com.helloworld.helloworldweb.dto.guestbook;

import com.helloworld.helloworldweb.domain.GuestBook;
import com.helloworld.helloworldweb.domain.GuestBookComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.service.UserService;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@Getter
public class GuestBookRequestDto {
    private Long id;
    private String content;
    private String targetUserEmail;
    private String reply;

    public GuestBookComment toEntity(User user){
        return GuestBookComment.builder()
                .content(content)
                .user(user)
                .build();
    }

    public GuestBookComment toEntity(){
        return GuestBookComment.builder()
                .content(content)
                .reply(reply)
                .build();
    }

    public GuestBookRequestDto(GuestBookComment guestBookComment) {
        this.id = guestBookComment.getId();
        this.content = guestBookComment.getContent();
    }
}
