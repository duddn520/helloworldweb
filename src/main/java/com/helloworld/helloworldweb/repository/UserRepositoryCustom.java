package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findUserWithPostByEmail(String email);
}
