package com.helloworld.helloworldweb.service.guestbook;

import com.helloworld.helloworldweb.domain.GuestBook;
import com.helloworld.helloworldweb.domain.GuestBookComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.repository.guestbook.GuestBookCommentRepository;
import com.helloworld.helloworldweb.repository.guestbook.GuestBookRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class GuestBookService {

    private final GuestBookRepository guestBookRepository;
    private final GuestBookCommentRepository guestBookCommentRepository;

//    @Transactional
    public List<GuestBookComment> getGuestBookComments(User user){

        // GuestBook - List<GuestBookComment> 리턴
        Optional<GuestBook> findGuestBook = guestBookRepository.findByUser(user);

        if( findGuestBook.isPresent() ) return findGuestBook.get().getGuestBookComments();
        return getNewGuestBook(user).getGuestBookComments();

    }

    @Transactional
    public GuestBookComment postGuestBookComment(User user,GuestBookComment guestBookComment){

            // GuestBook 찾기
            Optional<GuestBook> optionalGuestBook = guestBookRepository.findByUser(user) ;
            GuestBook findGuestBook;
            if( optionalGuestBook.isPresent() ) findGuestBook = optionalGuestBook.get();
            else findGuestBook = getNewGuestBook(user);

            // GuestBookComment -> FindGuestBook 연결
            GuestBookComment addGuestBookComment = guestBookComment.changeGuestBook(findGuestBook);
            guestBookCommentRepository.save(addGuestBookComment);

            return addGuestBookComment;
    }

    private GuestBook getNewGuestBook(User user){
        GuestBook newGuestBook = GuestBook.builder()
                                    .user(user)
                                    .build();
        guestBookRepository.save(newGuestBook);
        return newGuestBook;
    }


}
