package com.helloworld.helloworldweb.dto.User;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String userName;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUsername();
    }
}
