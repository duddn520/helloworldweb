package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.*;
import com.helloworld.helloworldweb.repository.PostCommentRepository;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public List<Pair<Post,PostSubComment>> getAllUserComments(Long userId){
        List<Pair<Post,PostSubComment>> resultList = new ArrayList<>();

        List<PostSubComment> findPostSubComments = postSubCommentRepository.findAllByUserId(userId).orElseGet(ArrayList::new);
        for ( PostSubComment subcomment : findPostSubComments){
            // 연관관계가 끊어진 경우 에러 유발방지
            // ex) PostSubComment 는 존재하나 PostCommentId == null
            if ( subcomment.getPostComment() == null || subcomment.getPostComment().getPost() == null ) continue;
            Post post = subcomment.getPostComment().getPost();
            // 블로그 댓글은 제외
            if( post.getCategory() == Category.BLOG ) continue;
            // (Post, SubComment ) 묶음
            resultList.add(Pair.of(post,subcomment));
        }
        return resultList;
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
        List<PostSubComment> allPostSubComments = postSubComment.getPostComment().getPostSubComments();

        // 댓글 삭제
        if( allPostSubComments.size() == 1 ){
            postCommentRepository.delete( postSubComment.getPostComment() );
        } else {
            postSubComment.delete(); // 연관관계 편의 메서드
            postSubCommentRepository.delete( postSubComment );
        }

    }
}
