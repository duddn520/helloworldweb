package com.helloworld.helloworldweb.s3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AmazonS3Component {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder.image}")
    private String imageFolder;

    @Value("${cloud.aws.s3.folder.music}")
    private String musicFolder;
}
