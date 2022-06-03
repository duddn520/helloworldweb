package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.dto.User.UserResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDtoWithPageNum {
    private List<PostResponseDtoWithUser> postResponseDtoWithUser = new ArrayList<>();
    private int pageNum;

    public PostResponseDtoWithPageNum(List<PostResponseDtoWithUser> responseDtos, int pageNum) {
        this.postResponseDtoWithUser = responseDtos;
        this.pageNum = pageNum;
    }
}
