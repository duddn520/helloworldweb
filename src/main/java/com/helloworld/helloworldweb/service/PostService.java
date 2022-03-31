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
    public Post findPost(Long postId) {

        Post findPost = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);

        return findPost;
    }

    @Transactional
    public Post registerPost(PostRequestDto requestDto, User user) {

        Post post = requestDto.toEntity();
        post.updateUser(user);

        return postRepository.save(post);
    }

    @Transactional
    //사용자 본인의 블로그 게시물 전체조회
    public List<Post> listUserBlog(Long userId) {

        List<Post> blogs = postRepository.findByUserIdAndCategory(userId, Category.BLOG).orElseGet(() -> new ArrayList<>());

        return blogs;
    }

    @Transactional
    //모든 QNA 게시물
    public List<Post> listAllQna() {

        List<Post> qnas = postRepository.findByCategory(Category.QNA).orElseGet(() -> new ArrayList<>());

        return qnas;
    }

    @Transactional
    public void deletePost(Post post) {


        postRepository.delete(post);
    }



}
