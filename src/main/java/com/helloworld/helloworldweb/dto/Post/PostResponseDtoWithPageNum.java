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
    private boolean IsOwner;

    public PostResponseDtoWithPageNum(List<PostResponseDtoWithUser> responseDtos, int pageNum) {
        this.postResponseDtoWithUser = responseDtos;
        this.pageNum = pageNum;
    }

    //조회한 게시물들이 본인의 게시물인지 알 필요가 있을 때 사용 ex) blog
    public PostResponseDtoWithPageNum(List<PostResponseDtoWithUser> responseDtos, int pageNum, boolean IsOwner) {
        this.postResponseDtoWithUser = responseDtos;
        this.pageNum = pageNum;
        this.IsOwner = IsOwner;
    }
}
