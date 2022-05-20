package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
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

    @Test @DisplayName("특정유저_작성한댓글_모두조회")
    void getAllUserComments(){
        // given : User , Post , Post- PostComment - PostSubComment
        User user = User.builder()
                .id(1L)
                .email("test@email.com")
                .build();
        Post post = Post.builder()
                .id(2L)
                .title("Post Title")
                .content("Post Content")
                .build();
        PostComment postComment = PostComment.builder()
                .id(1L)
                .post(post)
                .build();
        PostSubComment postSubComment = PostSubComment.builder()
                .id(1L)
                .content("PostSubComment")
                .postComment(postComment)
                .user(user)
                .build();
        // when : postSubCommentService.getAllUserCommments()
        when(postSubCommentRepository.findAllByUserId(any(Long.class))).thenReturn(Optional.of(List.of(postSubComment)));
        List<Pair<Post, PostSubComment>> allUserComments = postSubCommentService.getAllUserComments(user.getId());

        // then : 유저가 작성한 (Post,PostSubComment) 반환
        for( Pair<Post,PostSubComment> pair : allUserComments ){
            // Post Id 확인
            assertEquals( post  , pair.getFirst() );
            // SubComment 확인
            assertEquals( postSubComment , pair.getSecond() );
        }

    }
    @Test @DisplayName("다른유저_작성한댓글_조회제외")
    void getAllUserComments_exception(){
        // given : User , Post , Post- PostComment - PostSubComment
        User user1 = User.builder()
                .id(1L)
                .email("test1@email.com")
                .build();
        User user2 = User.builder()
                .id(2L)
                .email("test2@email.com")
                .build();
        Post post = Post.builder()
                .id(2L)
                .title("Post Title")
                .content("Post Content")
                .build();
        PostComment postComment = PostComment.builder()
                .id(1L)
                .post(post)
                .build();
        PostSubComment postSubComment = PostSubComment.builder()
                .id(1L)
                .content("PostSubComment")
                .postComment(postComment)
                .user(user2)
                .build();
        // when : postSubCommentService.getAllUserCommments()
        when(postSubCommentRepository.findAllByUserId(any(Long.class))).thenReturn(Optional.of(new ArrayList<>()));
        List<Pair<Post, PostSubComment>> allUserComments = postSubCommentService.getAllUserComments(user1.getId());

        // then : List.size() = 0
        assertEquals( 0 , allUserComments.size() );


    }

}
