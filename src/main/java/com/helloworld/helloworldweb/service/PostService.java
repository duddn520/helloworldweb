package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    //Post 한 개 조회
    //Post 조회, 수정, 삭제 시에 사용.
    public Post getPost(Long postId) {

        Post findPost = postRepository.findById(postId).orElseThrow((()->new IllegalArgumentException("해당 게시물이 존재하지 않습니다.")));

        return findPost;
    }

    @Transactional
    public Post addPost(Post post, User user) {

        post.updateUser(user);
        Post savedPost = postRepository.save(post);

        return savedPost;
    }

    @Transactional
    //사용자 본인의 게시물만 전체조회
    //userId를 매개변수로 받음.
    public List<Post> getAllUserPosts(Long userId, Category category) {

        List<Post> posts = postRepository.findByUserIdAndCategory(userId, category).orElseGet(() -> new ArrayList<>());

        return posts;
    }

    @Transactional
    //모든 Post 조회
    //카테고리를 매개변수로 받음.
    public List<Post> getAllPost(Category category){

        List<Post> posts = postRepository.findByCategory(category).orElseGet(() -> new ArrayList<>());

        return posts;
    }

    @Transactional
    public void deletePost(Post post) {

        postRepository.delete(post);
    }



}
