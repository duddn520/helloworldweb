package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment,Long> {
    Optional<PostComment> findByPostAndSelectedTrue(Post post);
    Optional<List<PostComment>> findAllByPostAndSelectedFalse(Post post);
}
