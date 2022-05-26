package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostImageResponseDto {

    private String storedUrl;
    private String storedFileName;

    public PostImageResponseDto(PostImage postImage){
        this.storedUrl = postImage.getStoredUrl();
        this.storedFileName = postImage.getStoredFileName();
    }
}
