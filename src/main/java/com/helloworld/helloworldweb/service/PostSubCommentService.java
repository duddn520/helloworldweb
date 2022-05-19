package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.PostCommentRepository;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostSubCommentService {

    private final PostSubCommentRepository postSubCommentRepository;
    private final PostCommentRepository postCommentRepository;

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

    // 유저가 작성한 모든 답변들 조회
    public List<PostSubComment> getAllUserComments(Long userId){
        return postSubCommentRepository.findAllById(userId).orElse(new ArrayList<PostSubComment>());
    }

    // 답변 수정
    public PostSubComment updatePostSubComment(Long id,String content){
        PostSubComment findPostSubComment = postSubCommentRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return postSubCommentRepository.save( findPostSubComment.updateContent(content) ) ;
    }

    @Transactional
    public void deletePostSubComment(Long id)
    {
        PostSubComment postSubComment = getPostSubCommentById(id);
        // 남아있는 모든 댓글들
        PostComment postComment = postSubComment.getPostComment();

        postCommentRepository.save( postComment.removePostSubComment(postSubComment) ) ;

        // 댓글 삭제
//        if( allPostSubComments.size() == 1 ){
//            postCommentRepository.delete( postSubComment.getPostComment() );
//        } else {
//            postSubCommentRepository.delete( postSubComment );
//        }

    }
}
