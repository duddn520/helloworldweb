package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Post;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    FileProcessService fileProcessService;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("음악 최초 업로드 성공")
    void firstUploadMusic_success() throws UnsupportedEncodingException {
        User user = User.builder()
                .email("test@email.com")
                .profileUrl(" ")
                .posts(new ArrayList<>())
                .profileMusic(null)
                .profileMusicUrl(null)
                .build();
        String filename = "hello.mp3";
        String url = "http://test123hello.mp3";
        when(userRepository.save(any(User.class))).thenReturn(user);
        //when
        userService.updateProfileMusic(user, filename, url);

        //then
        assertThat(user.getProfileMusic()).isEqualTo(filename);
        assertThat(user.getProfileMusicUrl()).isEqualTo(url);

    }

    @Test
    @DisplayName("음악 수정 성공")
    void updateMusic_success() throws UnsupportedEncodingException {
        User user = User.builder()
                .email("test@email.com")
                .profileUrl(" ")
                .posts(new ArrayList<>())
                .profileMusicUrl("http://test123hihi.mp3")
                .profileMusic("hihi.mp3")
                .build();
        String newFilename = "hello.mp3";
        String newUrl = "http://test123hello.mp3";

        when(fileProcessService.getFileName(anyString())).thenReturn(newFilename);
        doNothing().when(fileProcessService).deleteMusic(anyString());
        when(userRepository.save(any(User.class))).thenReturn(user);
        //when
        userService.updateProfileMusic(user, newFilename, newUrl);

        //then
        assertThat(user.getProfileMusic()).isEqualTo(newFilename);
        assertThat(user.getProfileMusicUrl()).isEqualTo(newUrl);

    }

}
