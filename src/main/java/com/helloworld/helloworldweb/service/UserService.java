package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Optional<User> searchUserById(Long Id){
        return userRepository.findById(Id);
    }

    @Transactional
    public User registerUser(User user)
    {
        return userRepository.save(user);
    }

    @Transactional
    public User findUserById(Long userId) {

        User findUser = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("로그인상태가 아닙니다."));

        return findUser;
    }

}
