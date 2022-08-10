package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

     @Override
     Optional<Post> findById(Long postId);

     Optional<List<Post>> findByCategory(Category category);
     Optional<List<Post>> findByUserIdAndCategory(Long id, Category category);
     Optional<List<Post>> findAllByTitleContainingOrContentContaining(String s1,String s2);

     Optional<List<Post>> findAllByTagsContaining(String s);

     Optional<List<Post>> findByCategory(Category category, Pageable pageable);
     Optional<List<Post>> findByUserIdAndCategory(Long id, Category category, Pageable pageable);

     Optional<List<Post>> findTop5ByCreatedTimeGreaterThanEqualAndCategoryOrderByViewsDesc(LocalDateTime dateTime,Category category);
     //전체페이지 수 구할 때 사용
     Page<Post> findAllByCategory(Category category, Pageable pageable);
     Page<Post> findAllByUserIdAndCategory(Long id, Category category, Pageable pageable);
}
