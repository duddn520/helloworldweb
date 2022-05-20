package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.PostSubComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostSubCommentRepository extends JpaRepository<PostSubComment,Long> {
    Optional<PostSubComment> findById(Long id);
    Optional<List<PostSubComment>> findAllById(Long id);
    Optional<List<PostSubComment>> findAllByUserId(Long userId);
}
