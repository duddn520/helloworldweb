package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDtoWithPageNum {
    private List<PostResponseDto> postResponseDtos = new ArrayList<>();
    private int pageNum;

    public PostResponseDtoWithPageNum(List<PostResponseDto> responseDtos, int pageNum) {
        this.postResponseDtos = responseDtos;
        this.pageNum = pageNum;
    }
}
