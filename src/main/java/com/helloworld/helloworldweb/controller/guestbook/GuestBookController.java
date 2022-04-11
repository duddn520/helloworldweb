package com.helloworld.helloworldweb.controller.guestbook;

import com.helloworld.helloworldweb.domain.GuestBook;
import com.helloworld.helloworldweb.domain.GuestBookComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.guestbook.GuestBookDto;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.UserService;
import com.helloworld.helloworldweb.service.guestbook.GuestBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestBookService guestBookService;
    private final UserService userService;


    // 임시
    @GetMapping("/api/guestbook/test")
    public void test(){
        userService.addUser(new User());
        userService.addUser(new User());
    }
    //



    @GetMapping("/api/guestbook")
    @Transactional
    public ResponseEntity<ApiResponse<List<GuestBookDto>>> getGuestBooks(HttpServletRequest request){
            // 임시: 유저 ID로 유저를 찾음.
            User findUser = userService.searchUserById(Long.parseLong(request.getParameter("id")))
                    .orElseThrow(() -> new IllegalStateException("GuestBook - GET - 회원조회 실패"));
            //

            // 유저를 통해 방명록들 불러옴.
            List<GuestBookComment> findGuestBookComments = guestBookService.getGuestBookComments(findUser);

            List<GuestBookDto> responseDtos = new ArrayList<GuestBookDto>();

            for ( GuestBookComment comment : findGuestBookComments ) {
                responseDtos.add(new GuestBookDto(comment));
            }

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.GET_SUCCESS,responseDtos), HttpStatus.OK);
    }

    @PostMapping("/api/guestbook")
    public ResponseEntity<ApiResponse> postGuestBook(GuestBookDto guestBookDto){
        try {
            GuestBookComment guestBookComment = guestBookDto.toEntity();
            // 임시: 유저 ID로 유저를 찾음.
            User findUser = userService.searchUserById(1L)
                    .orElseThrow(() -> new IllegalStateException("GuestBook - GET - 회원조회 실패"));
            //

            // 방명록 저장
            GuestBookComment saveGuestBookComment = guestBookService.postGuestBookComment(findUser, guestBookComment);
            GuestBookDto responseDto = new GuestBookDto(saveGuestBookComment);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.OK,
                    HttpResponseMsg.POST_SUCCESS,responseDto), HttpStatus.OK);

        } catch( Exception e ){

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.INTERNAL_SERVER_ERROR,
                    HttpResponseMsg.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
