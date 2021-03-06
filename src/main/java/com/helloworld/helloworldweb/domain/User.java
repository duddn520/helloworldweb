package com.helloworld.helloworldweb.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User implements UserDetails {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostSubComment> subComments = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserHome userHome;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private GuestBook guestBook;

    @OneToMany(mappedBy = "user")
    private List<GuestBookComment> guestBookComments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;
    private String profileUrl;
    private String repo_url;
    private String nickName;
    private String profileMusic;
    private String profileMusicUrl;
    private String fcm;

    @Transient
    private Collection<SimpleGrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Builder
    public User(Long id, List<Post> posts, List<PostSubComment> subComments, UserHome userHome, GuestBook guestBook, List<GuestBookComment> guestBookComments, Role role, String email
    ,String repo_url ,String profileUrl, String nickName, String profileMusic, String profileMusicUrl, String fcm) {
        this.id = id;
        this.posts = posts;
        this.subComments = subComments;
        this.userHome = userHome;
        this.guestBook = guestBook;
        this.guestBookComments = guestBookComments;
        this.role = role;
        this.email = email;
        this.profileUrl = profileUrl;
        this.repo_url = repo_url;
        this.nickName = nickName;
        this.profileMusic = profileMusic;
        this.profileMusicUrl = profileMusicUrl;
        this.fcm = fcm;
    }

    public void updateRepoUrl(String repo_url)
    {
        this.repo_url = repo_url;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateProfileMusic(String profileMusic, String profileMusicUrl) {
        this.profileMusic = profileMusic;
        this.profileMusicUrl = profileMusicUrl;
    }

    public void updateFcm(String fcmToken)
    {
        this.fcm = fcmToken;
    }
}
