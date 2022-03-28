package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Post registerPost(PostRequestDto requestDto, Long user_id) {

        User findUser = userRepository.findById(user_id).get();
        Post post = requestDto.toEntity();
        post.updateUser(findUser);

        return postRepository.save(post);
    }

    @Transactional
    public List<Post> listPost(Long user_id) {

        List<Post> posts = postRepository.findByUserId(user_id).get();
        return posts;
    }

    @Transactional
    public void deletePost(Long user_id, Long post_id) {

        Post findPost = postRepository.findById(post_id).get();
//        if (user_id == findPost.getUser().getId()) {
//            postRepository.deleteById(post_id);
//        }
        postRepository.delete(findPost);

    }
}
