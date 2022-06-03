package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.*;
import com.helloworld.helloworldweb.repository.PostCommentRepository;
import com.helloworld.helloworldweb.repository.PostRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostCommentServiceTest {

    @Mock
    PostCommentRepository postCommentRepository;

    @Mock
    PostSubCommentRepository postSubCommentRepository;

    @Mock
    PostRepository postRepository;

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
                .solved(false)
                .build();

        post.updateUser(user);

        PostComment postComment = PostComment.builder()
                        .selected(false)
                        .build();

        postComment.updatePost(post);

        for(int i=0;i<3;i++)
        {
            PostSubComment subComment = PostSubComment.builder()
                    .content("1234"+i)
                    .build();

            subComment.updateUser(user);
            subComment.updatePostComment(postComment);
        }

        when(postCommentRepository.save(any(PostComment.class))).thenReturn(postComment);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        //when
        PostComment result = postCommentService.selectPostComment(postComment);

        //then
        assertThat(result.isSelected()).isEqualTo(true);
        assertThat(post.getPostComments().get(0).isSelected()).isEqualTo((true));
        assertThat(result.getPost().isSolved()).isEqualTo(true);

    }

    @Test
    @DisplayName("채택 댓글이 리스트 최상단에 위치(SolvedPost)")
    void getPostCommentsInOrder_SolvedPost() throws Exception{
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
                .solved(true)
                .build();

        post.updateUser(user);
        PostComment selectedPostComment = new PostComment();
        List<PostComment> otherPostComments = new ArrayList<>();
        for( int j=0;j<3;j++) {
            if(j==1) {

                PostComment postComment = PostComment.builder()
                        .selected(true)
                        .build();

                postComment.updatePost(post);


                for (int i = 0; i < 3; i++) {
                    PostSubComment subComment = PostSubComment.builder()
                            .content("1234" + i)
                            .build();

                    subComment.updateUser(user);
                    subComment.updatePostComment(postComment);
                }

                selectedPostComment = postComment;

            }
            else
            {
                PostComment postComment = PostComment.builder()
                    .selected(false)
                    .build();

                postComment.updatePost(post);

                for (int i = 0; i < 3; i++) {
                    PostSubComment subComment = PostSubComment.builder()
                            .content("1234" + i)
                            .build();

                    subComment.updateUser(user);
                    subComment.updatePostComment(postComment);
                }

                otherPostComments.add(postComment);
            }

        }
        when(postCommentRepository.findByPostAndSelectedTrue(any(Post.class))).thenReturn(Optional.of(selectedPostComment));
        when(postCommentRepository.findAllByPostAndSelectedFalse(any(Post.class))).thenReturn(Optional.of(otherPostComments));


        //when
        List<PostComment> postComments = postCommentService.getPostCommentsInOrder(post);

        //then
        assertThat(postComments.get(0).isSelected()).isEqualTo(true);
        assertThat(postComments.size()).isEqualTo(3);
    }

}
