package com.helloworld.helloworldweb.repository.guestbook;

import com.helloworld.helloworldweb.domain.GuestBookComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestBookCommentRepository extends JpaRepository<GuestBookComment,Long> {

}
