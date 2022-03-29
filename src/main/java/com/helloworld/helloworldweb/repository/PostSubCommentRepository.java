package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.PostSubComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostSubCommentRepository extends JpaRepository<PostSubComment,Long> {
}
