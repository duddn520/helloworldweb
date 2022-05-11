package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.PostCommentRepository;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostSubCommentRepository postSubCommentRepository;

    @Transactional
    public PostComment registerPostComment(PostComment postComment, Post post, PostSubComment postSubComment, User user)
    {
        postComment.updatePost(post);
        postSubComment.updatePostComment(postComment);
        postSubComment.updateUser(user);
        postSubCommentRepository.save(postSubComment);
        return postCommentRepository.save(postComment);
    }

    public PostComment getPostCommentById(Long id)
    {
        PostComment postComment = postCommentRepository.findById(id).orElseGet(()-> new PostComment());

        return postComment;
    }

    @Transactional
    public void deletePostComment(PostComment postComment)
    {
        postCommentRepository.delete(postComment);
    }
}
