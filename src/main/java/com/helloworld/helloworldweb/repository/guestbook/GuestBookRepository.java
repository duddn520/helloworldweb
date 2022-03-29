package com.helloworld.helloworldweb.repository.guestbook;

import com.helloworld.helloworldweb.domain.GuestBook;
import com.helloworld.helloworldweb.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestBookRepository extends JpaRepository<GuestBook,Long> {

//    @EntityGraph(attributePaths = { "guestBookComments" })
    public Optional<GuestBook> findByUser(User user);

}
