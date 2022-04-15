package com.helloworld.helloworldweb.controller.guestbook;

import com.helloworld.helloworldweb.domain.GuestBook;
import com.helloworld.helloworldweb.domain.GuestBookComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.guestbook.GuestBookDto;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.UserService;
import com.helloworld.helloworldweb.service.guestbook.GuestBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestBookService guestBookService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/api/guestbook")
    @Transactional
    public ResponseEntity<ApiResponse<List<GuestBookDto>>> getGuestBooks(@RequestParam(name="id") Long id,HttpServletRequest request){

        // 유저 찾기
        String findEmail = jwtTokenProvider.getUserEmail(jwtTokenProvider.getTokenByHeader(request));
        User findUser = userService.getUserByEmail(findEmail);

        // 유저를 통해 방명록들 불러옴.
        List<GuestBookComment> findGuestBookComments = guestBookService.getGuestBookComments(findUser);

        List<GuestBookDto> responseDtos = new ArrayList<GuestBookDto>();

        for ( GuestBookComment comment : findGuestBookComments ) {
            responseDtos.add(new GuestBookDto(comment));
        }

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,responseDtos), HttpStatus.OK);
    }

    @PostMapping("/api/guestbook")
    public ResponseEntity<ApiResponse> registerGuestBook(GuestBookDto guestBookDto,HttpServletRequest request){
        try {
            // 유저 찾기
            String findEmail = jwtTokenProvider.getUserEmail(jwtTokenProvider.getTokenByHeader(request));
            User findUser = userService.getUserByEmail(findEmail);

            // Dto -> Entity
            GuestBookComment guestBookComment = guestBookDto.toEntity();
            // 방명록 저장
            GuestBookComment saveGuestBookComment = guestBookService.addGuestBookComment(findUser, guestBookComment);
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

    @DeleteMapping("/api/guestbook")
    public ResponseEntity<ApiResponse> deleteGuestBook(HttpServletRequest request){
        String requestId = request.getParameter("id");
        boolean result = guestBookService.deleteGuestBookComment(Long.parseLong(requestId));
        if( result ) {
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.DELETE_SUCCESS,
                    HttpResponseMsg.DELETE_SUCCESS),HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.INTERNAL_SERVER_ERROR,
                    HttpResponseMsg.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

}
