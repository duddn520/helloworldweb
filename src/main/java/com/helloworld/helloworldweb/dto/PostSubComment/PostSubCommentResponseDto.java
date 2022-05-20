package com.helloworld.helloworldweb.dto.PostSubComment;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.PostSubComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.dto.User.UserResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class PostSubCommentResponseDto {

    private Long id;
    private String content;
    private String createdTime;
    private String modifiedTime;
    private UserResponseDto userResponseDto;
    // 댓글과 연관된 Post
    private PostResponseDto postResponseDto;
    private boolean IsOwner;

    public PostSubCommentResponseDto (PostSubComment postSubComment)
    {
        this.id = postSubComment.getId();
        this.content = postSubComment.getContent();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postSubComment.getCreatedTime());
        this.modifiedTime = postSubComment.getModifiedTime() != null ? DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postSubComment.getModifiedTime()) : null;
        this.userResponseDto = new UserResponseDto(postSubComment.getUser());
    }

    public PostSubCommentResponseDto (PostSubComment postSubComment, User caller)
    {
        this.id = postSubComment.getId();
        this.content = postSubComment.getContent();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postSubComment.getCreatedTime());
        this.modifiedTime = postSubComment.getModifiedTime() != null ? DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postSubComment.getModifiedTime()) : null;
        this.userResponseDto = new UserResponseDto(postSubComment.getUser());
        this.IsOwner = checkIsOwner(postSubComment.getUser(),caller);
    }

    public PostSubCommentResponseDto (PostSubComment postSubComment, boolean isOwner)
    {
        this.id = postSubComment.getId();
        this.content = postSubComment.getContent();
        this.createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postSubComment.getCreatedTime());
        this.modifiedTime = postSubComment.getModifiedTime() != null ? DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(postSubComment.getModifiedTime()) : null;
        this.userResponseDto = new UserResponseDto(postSubComment.getUser());
        this.IsOwner = isOwner;
    }

    public static PostSubCommentResponseDto allUserCommentsDtos(Pair<Post,PostSubComment> pair){
        PostSubCommentResponseDto postSubCommentResponseDto = new PostSubCommentResponseDto(pair.getSecond());
        postSubCommentResponseDto.postResponseDto = new PostResponseDto(pair.getFirst());
        return postSubCommentResponseDto;
    }

    private boolean checkIsOwner(User owner, User caller){
        return owner.getId()==caller.getId();
    }
}
