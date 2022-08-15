package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.User.UserResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDtoWithUser extends PostResponseDto {
    private UserResponseDto userResponseDto;
    private int numOfSubComments;

    public PostResponseDtoWithUser(Post post) {
        super(post);
        this.userResponseDto = new UserResponseDto(post.getUser());
    }

    public PostResponseDtoWithUser (Post post, int numOfSubComments) {
        super(post);
        this.userResponseDto = new UserResponseDto(post.getUser());
        this.numOfSubComments = numOfSubComments;
    }
}
