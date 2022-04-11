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

    // 방명록 조회
    public List<GuestBookComment> getGuestBookComments(User user){

        // GuestBook - List<GuestBookComment> 리턴
        Optional<GuestBook> findGuestBook = guestBookRepository.findByUser(user);

        if( findGuestBook.isPresent() ) return findGuestBook.get().getGuestBookComments();
        return getNewGuestBook(user).getGuestBookComments();

    }

    // 방명록 추가
    @Transactional
    public GuestBookComment addGuestBookComment(User user,GuestBookComment guestBookComment){

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

    // 방명록 수정
//    public GuestBookComment updateGuestBookComment() {
//
//    }

    // 방명록 삭제
    @Transactional
    public boolean deleteGuestBookComment(Long id){
        try {
            guestBookCommentRepository.deleteById(id);
            return true;
        } catch( Exception e ) {
            return false;
        }
    }


    private GuestBook getNewGuestBook(User user){
        GuestBook newGuestBook = GuestBook.builder()
                                    .user(user)
                                    .build();
        guestBookRepository.save(newGuestBook);
        return newGuestBook;
    }


}
