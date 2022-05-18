package com.helloworld.helloworldweb.dto.Post;

import com.helloworld.helloworldweb.domain.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostImageResponseDto {

    private String storedUrl;
    private String base64;
    private String originalFileName;

    public PostImageResponseDto(PostImage postImage){
        this.storedUrl = postImage.getStoredUrl();
        this.base64 = postImage.getBase64();
        this.originalFileName = postImage.getOriginalFileName();
    }
}
