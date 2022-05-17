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
    private String profileUrl;
    private boolean IsOwner;

    public UserResponseDto(User user, boolean isOwner) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getNickName();
        this.profileUrl = user.getProfileUrl();
        this.IsOwner = isOwner;
    }
    public UserResponseDto(User user, User caller) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getNickName();
        this.profileUrl = user.getProfileUrl();
        this.IsOwner = checkIsOwner(user,caller);
    }

    public UserResponseDto(User user)
    {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUsername().split("/")[0];
        this.profileUrl = user.getProfileUrl();
    }

    private boolean checkIsOwner(User owner, User caller){
        return owner.getId()==caller.getId();
    }
}
