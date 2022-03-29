package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Category;
import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Transactional
    public Post findPost(PostRequestDto requestDto) {

        Post findPost = postRepository.findById(requestDto.getPost_id()).orElseThrow(NoSuchElementException::new);

        return findPost;
    }

    @Transactional
    public Post registerPost(PostRequestDto requestDto) {

        User findUser = userRepository.findById(requestDto.getUser_id()).orElseThrow(() -> new NoSuchElementException("로그인상태가 아닙니다."));
        Post post = requestDto.toEntity();
        post.updateUser(findUser);

        return postRepository.save(post);
    }

    @Transactional
    //사용자 본인의 블로그 게시물 조회
    public List<Post> listUserBlog(PostRequestDto requestDto) {

        List<Post> blogs = postRepository.findByUserIdAndCategory(requestDto.getUser_id(), Category.BLOG).orElseGet(() -> new ArrayList<>());

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
