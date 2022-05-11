package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostImage;
import com.helloworld.helloworldweb.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDtoWithIsOwner {

    private List<PostResponseDto> postResponseDtos = new ArrayList<>();
    private boolean IsOwner;

    public PostResponseDtoWithIsOwner(List<Post> posts, boolean isOwner) {
        this.postResponseDtos = postResponseDtos(posts);
        this.IsOwner = isOwner;
    }

    // 게시물의 썸네일 표시에 필요한 정보
    private List<PostResponseDto> postResponseDtos (List<Post> posts) {
        List<PostResponseDto> responseDtos = new ArrayList<>();

        for( Post post : posts) {
            responseDtos.add(new PostResponseDto(post));
        }

        return responseDtos;
    }

}
