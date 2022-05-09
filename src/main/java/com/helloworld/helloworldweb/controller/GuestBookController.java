package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.GuestBookComment;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.guestbook.GuestBookRequestDto;
import com.helloworld.helloworldweb.dto.guestbook.GuestBookResponseDto;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.UserService;
import com.helloworld.helloworldweb.service.GuestBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
    public ResponseEntity<ApiResponse<List<GuestBookRequestDto>>> getGuestBooks(HttpServletRequest request,@RequestParam(name = "email")String email){

        // 유저 찾기
//        String findEmail = jwtTokenProvider.getUserEmail(jwtTokenProvider.getTokenByHeader(request));
        User findUser = userService.getUserByEmail(email);

        // 유저를 통해 방명록들 불러옴.
        List<GuestBookComment> findGuestBookComments = guestBookService.getGuestBookComments(findUser);

        List<GuestBookResponseDto> responseDtos = new ArrayList<GuestBookResponseDto>();

        for ( GuestBookComment comment : findGuestBookComments ) {
            responseDtos.add(new GuestBookResponseDto(comment));
        }

        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,responseDtos), HttpStatus.OK);
    }

    @PostMapping("/api/guestbook")
    public ResponseEntity<ApiResponse> registerGuestBook(@RequestBody GuestBookRequestDto guestBookRequestDto, HttpServletRequest request){
        try {
            // 유저 찾기
            String findEmail = jwtTokenProvider.getUserEmail(jwtTokenProvider.getTokenByHeader(request));
            User findCreatedUser = userService.getUserByEmail(findEmail);

            // Dto -> Entity
            GuestBookComment guestBookComment = guestBookRequestDto.toEntity(findCreatedUser);

            // 방명록 저장
            // 타겟유저 찾기
            User findTargetUser = userService.getUserByEmail(guestBookRequestDto.getTargetUserEmail());

            GuestBookComment saveGuestBookComment = guestBookService.addGuestBookComment(findTargetUser,guestBookComment);
            GuestBookResponseDto responseDto = new GuestBookResponseDto(saveGuestBookComment);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.POST_SUCCESS,
                    HttpResponseMsg.POST_SUCCESS,responseDto), HttpStatus.OK);

        } catch( Exception e ){
            System.out.println("e.getMessage() = " + e.getMessage());
            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.INTERNAL_SERVER_ERROR,
                    HttpResponseMsg.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 방명록에 답글 달기
    @PutMapping("/api/guestbook")
    public ResponseEntity<ApiResponse<GuestBookResponseDto>> updateGuestBook(@RequestBody GuestBookRequestDto guestBookRequestDto){

        // Dto -> Entity
        Long id = guestBookRequestDto.getId();
        GuestBookComment guestBookComment = guestBookRequestDto.toEntity();

        GuestBookComment updateGuestBookComment = guestBookService.updateGuestBookComment(id, guestBookComment);

        GuestBookResponseDto responseDto = new GuestBookResponseDto(updateGuestBookComment);

        return new ResponseEntity(ApiResponse.response(
                HttpStatusCode.PUT_SUCCESS,
                HttpResponseMsg.PUT_SUCCESS,responseDto),HttpStatus.OK
        );

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
