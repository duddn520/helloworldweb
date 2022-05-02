package com.helloworld.helloworldweb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    // 제목
    private String title;
    // 내용
    private String content;
    // 태그
    private String tags;
    // 검색횟수
    private Long searchCount = 0L;
    // 조회수
    private Long views = 0L;


    @Builder
    public Post(Long id, User user, List<PostComment> postComments, List<PostImage> postImages, Category category, String content, String title ,String tags) {
        this.id = id;
        this.user = user;
        this.postComments = postComments;
        this.postImages = postImages;
        this.category = category;
        this.content = content;
        this.title = title;
        this.tags = tags;
    }

    public void updateUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }
    public void updateSearchCount(){
        this.searchCount += 1;
    }
    public void updateViews(){
        this.views += 1;
    }
}
