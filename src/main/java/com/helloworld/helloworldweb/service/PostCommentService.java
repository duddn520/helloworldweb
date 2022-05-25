package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostComment;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.PostCommentRepository;
import com.helloworld.helloworldweb.repository.PostRepository;
import com.helloworld.helloworldweb.repository.PostSubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostSubCommentRepository postSubCommentRepository;
    private final PostRepository postRepository;

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

    @Transactional
    public PostComment selectPostComment(PostComment postComment)
    {
        postComment.selectPostComment();
        Post post = postComment.getPost();
        postRepository.save(post.updateSolved());
        return postCommentRepository.save(postComment);
    }

    @Transactional
    public List<PostComment> getPostCommentsInOrder(Post post)
    {
        List<PostComment> postComments = new ArrayList<>();
        PostComment selectedPostComment = postCommentRepository.findByPostAndSelectedTrue(post).orElseThrow(()-> new IllegalStateException("해결되지 않은 질문입니다."));
        postComments.add(selectedPostComment);
        List<PostComment> otherPostComments = postCommentRepository.findAllByPostAndSelectedFalse(post).orElseGet(()-> new ArrayList<>());
        postComments.addAll(otherPostComments);
        return postComments;
    }
}
