package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.*;
import com.helloworld.helloworldweb.repository.PostCommentRepository;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostCommentServiceTest {

    @Mock
    PostCommentRepository postCommentRepository;

    @Mock
    PostSubCommentRepository postSubCommentRepository;

    @InjectMocks
    PostCommentService postCommentService;

    @Test
    @DisplayName("댓글 등록 성공")
    void registerPostComment_success() throws IOException{
        //given
        User user = User.builder()
                .posts(new ArrayList<>())
                .subComments(new ArrayList<>())
                .email("test@email.com")
                .role(Role.USER)
                .nickName("hihihi")
                .build();

        Post post = Post.builder()
                .postComments(new ArrayList<>())
                .title("hello")
                .content("i dont know!!!")
                .tags("java")
                .category(Category.QNA)
                .build();

        PostComment postComment = PostComment.builder()
                .selected(false)
                .build();

        PostSubComment subComment = PostSubComment.builder()
                .content("1234")
                .build();

        when(postCommentRepository.save(any(PostComment.class))).thenReturn(postComment);
        when(postSubCommentRepository.save(any(PostSubComment.class))).thenReturn(subComment);

        //when
        PostComment result = postCommentService.registerPostComment(postComment,post,subComment,user);

        //then (연관관계 잘 맺어졌는지 확인.)
        assertThat(result.getPostSubComments().get(0).getContent()).isEqualTo("1234");
        assertThat(result.getPost().getContent()).isEqualTo("i dont know!!!");
        assertThat(result.getPostSubComments().get(0).getUser()).isEqualTo(user);

    }

    @Test
    @DisplayName("댓글 채택 성공")
    void selectPostComment_success() throws IOException{
        //given
        User user = User.builder()
                .posts(new ArrayList<>())
                .subComments(new ArrayList<>())
                .email("test@email.com")
                .role(Role.USER)
                .nickName("hihihi")
                .build();

        Post post = Post.builder()
                .postComments(new ArrayList<>())
                .title("hello")
                .content("i dont know!!!")
                .tags("java")
                .category(Category.QNA)
                .build();

        post.updateUser(user);

        PostComment postComment = PostComment.builder()
                        .selected(false)
                        .build();

        postComment.updatePost(post);

        List<PostSubComment> subCommentList = new ArrayList<>();
        for(int i=0;i<3;i++)
        {
            PostSubComment subComment = PostSubComment.builder()
                    .content("1234"+i)
                    .build();

            subComment.updateUser(user);
            subComment.updatePostComment(postComment);
            subCommentList.add(subComment);
        }

        when(postCommentRepository.save(any(PostComment.class))).thenReturn(postComment);
        when(postCommentService.getPostCommentById(any(Long.class))).thenReturn(postComment);

        //when
        PostComment result = postCommentService.selectPostComment(postComment.getId());

        //then
        assertThat(result.isSelected()).isEqualTo(true);
        assertThat(post.getPostComments().get(0).isSelected()).isEqualTo((true));

    }

}
