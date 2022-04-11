package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.UserService;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class UserController extends HttpServlet {

    private final UserService userService;

    // 카카오 로그인 및 회원가입 요청
    @PostMapping("/user/register/kakao")
    public ResponseEntity<ApiResponse> registerUserWithKakao(HttpServletRequest request,HttpServletResponse response) throws ParseException {

        response.addHeader("Access-Control-Allow-Origin","*");

        // 카카오로 부터 받아온 정보로 유저로 등록
        String jwt = userService.addKakaoUser(request.getHeader("token"));


        HttpHeaders headers = new HttpHeaders();
        headers.add("Auth", jwt);
        headers.add("Access-Control-Allow-Origin", "*");

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS), headers, HttpStatus.OK);
    }

    @PostMapping("/user/register/naver")
    public ResponseEntity<ApiResponse<String>> registerUserWithNaver(@RequestParam(name = "accessToken") String accessToken) throws ParseException {

        //accessToken으로 네이버로 부터 정보를 받아옴 String 형식
        String userInfoRespnoseFromNaver = userService.getUserInfoFromNaver(accessToken);

        //네이버로 부터 받아온 정보로 유저로 등록
        String jwt = userService.addNaverUser(userInfoRespnoseFromNaver);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Auth", jwt);
        headers.add("Access-Control-Allow-Origin", "*");

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS,
                userInfoRespnoseFromNaver), headers, HttpStatus.OK);
    }

}
