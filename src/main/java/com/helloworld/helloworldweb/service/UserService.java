package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User registerUser(User user)
    {
        return userRepository.save(user);
    }


}
