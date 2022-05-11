package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostSubCommentService {

    private final PostSubCommentRepository postSubCommentRepository;

    public PostSubComment getPostSubCommentById(Long id)
    {
        return postSubCommentRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 대댓글이 존재하지 않습니다."));
    }

    @Transactional
    public PostSubComment registerPostSubComment(PostSubComment postSubComment, PostComment postComment, User user)
    {
        postSubComment.updatePostComment(postComment);
        postSubComment.updateUser(user);
        return postSubCommentRepository.save(postSubComment);
    }

    @Transactional
    public void deletePostSubComment(PostSubComment postSubComment)
    {
        postSubCommentRepository.delete(postSubComment);
    }
}
