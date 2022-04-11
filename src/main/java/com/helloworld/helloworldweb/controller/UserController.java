package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 카카오 로그인 및 회원가입 요청
    @PostMapping("/login/kakao")
    public ResponseEntity<ApiResponse> kakaoLogin(HttpServletRequest request,HttpServletResponse response){

        response.addHeader("Access-Control-Allow-Origin","*");

        System.out.println("request.getHeader(\"token\") = " + request.getHeader("token"));

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);
    }


    @GetMapping("/user/register")
    public ResponseEntity<ApiResponse> registerUser()
    {
        User user = new User();
        userService.registerUser(user);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);

    }
}
