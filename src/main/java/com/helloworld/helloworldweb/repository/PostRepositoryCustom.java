package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.Post;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findPostWithImagesById(Long postId);
}
