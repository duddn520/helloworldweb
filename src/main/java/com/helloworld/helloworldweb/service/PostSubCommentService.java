package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSubCommentService {

    private final PostSubCommentRepository postSubCommentRepository;

    public PostSubComment getPostSubCommentById(Long id)
    {
        return postSubCommentRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 대댓글이 존재하지 않습니다."));
    }

    @Transactional
    public PostSubComment registerPostSubComment(PostSubComment postSubComment, PostComment postComment)
    {
        postSubComment.updatePostComment(postComment);
        return postSubCommentRepository.save(postSubComment);
    }

    // 유저가 작성한 모든 답변들 조회
    public List<PostSubComment> getAllUserComments(Long userId){
        return postSubCommentRepository.findAllById(userId).orElse(new ArrayList<PostSubComment>());
    }

    @Transactional
    public void deletePostSubComment(PostSubComment postSubComment)
    {
        postSubCommentRepository.delete(postSubComment);
    }
}
