package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

}
