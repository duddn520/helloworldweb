package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment,Long> {
}
