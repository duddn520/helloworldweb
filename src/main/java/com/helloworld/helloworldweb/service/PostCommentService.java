package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.repository.PostCommentRepository;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostSubCommentRepository postSubCommentRepository;

    @Transactional
    public PostComment registerPostComment(PostComment postComment, Post post, PostSubComment postSubComment)
    {
        postComment.updatePost(post);
        postSubComment.updatePostComment(postComment);
        postSubCommentRepository.save(postSubComment);
        return postCommentRepository.save(postComment);
    }

    public PostComment getPostCommentById(Long id)
    {
        PostComment postComment = postCommentRepository.findById(id).orElseThrow(()->new RuntimeException("해당 댓글이 존재하지 않습니다."));

        return postComment;
    }

    @Transactional
    public void deletePostComment(PostComment postComment)
    {
        postCommentRepository.delete(postComment);
    }
}
