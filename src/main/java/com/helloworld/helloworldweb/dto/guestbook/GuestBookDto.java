package com.helloworld.helloworldweb.dto.guestbook;

import com.helloworld.helloworldweb.domain.GuestBook;
import com.helloworld.helloworldweb.domain.GuestBookComment;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GuestBookDto {
    private Long id;
    private String title;
    private String content;

    public GuestBookComment toEntity(){
        return GuestBookComment.builder()
                .title(title)
                .content(content)
                .build();
    }

    public GuestBookDto(GuestBookComment guestBookComment) {
        this.id = guestBookComment.getId();
        this.title = guestBookComment.getTitle();
        this.content = guestBookComment.getContent();
    }
}
