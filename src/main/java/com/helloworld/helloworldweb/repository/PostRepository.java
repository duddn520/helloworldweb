package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

     @Override
     Optional<Post> findById(Long postId);

     Optional<List<Post>> findByCategory(Category category);
     Optional<List<Post>> findByUserIdAndCategory(Long id, Category category);
     Optional<List<Post>> findAllByTitleContainingOrContentContaining(String s1,String s2);
     Optional<List<Post>> findAllByTagsContaining(String s);

     Optional<List<Post>> findByCategory(Category category, Pageable pageable);
     Optional<List<Post>> findByUserIdAndCategory(Long id, Category category, Pageable pageable);

     Optional<List<Post>> findTop5ByCreatedTimeGreaterThanEqualAndCategoryOrderByViewsDesc(LocalDateTime dateTime,Category category);
}
