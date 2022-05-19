package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostSubCommentServiceTest {

    @Mock
    PostSubCommentRepository postSubCommentRepository;

    @InjectMocks
    PostSubCommentService postSubCommentService;

    @Test
    @DisplayName("댓글수정")
    void updatePostSubComment(){

        // given : 특정 Post에 달린 댓글이 주어짐.
        PostSubComment postSubComment = PostSubComment.builder()
                .id(1L)
                .content("수정 전")
                .build();

        // when : 댓글을 수정
        when(postSubCommentRepository.findById(any(Long.class))).thenReturn(Optional.of(postSubComment));
        when(postSubCommentRepository.save(any(PostSubComment.class))).thenReturn(postSubComment);

        PostSubComment savedPostSubComment = postSubCommentService.updatePostSubComment(1L,"수정 후");

        // then : 내용이 "수정 후"인지 확인
        assertEquals("수정 후",savedPostSubComment.getContent());
    }

}
