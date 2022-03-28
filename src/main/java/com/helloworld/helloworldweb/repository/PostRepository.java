package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
     Optional<List<Post>> findByUserId(Long user_id);
}
