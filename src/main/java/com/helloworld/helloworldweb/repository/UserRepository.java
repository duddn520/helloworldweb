package com.helloworld.helloworldweb.repository;

import com.helloworld.helloworldweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, UserRepositoryCustom {
    @Override
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}
