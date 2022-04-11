package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.Role;
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
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class UserController extends HttpServlet {

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
        userService.addUser(user);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);

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

    @PostMapping("/user/register/github")
    public ResponseEntity<ApiResponse> registerUserWithGithub(@RequestParam(name = "code") String code,HttpServletResponse servletresponse) throws ParseException {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id","105e0b50eefc27b4dc81");
        params.add("client_secret","ce4d0a93a257529e78a8804f322ca629b1d7cba6");
        params.add("code",code);

        HttpHeaders headers = new HttpHeaders();

        servletresponse.addHeader("Access-Control-Allow-Origin","*");

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<JSONObject> accessTokenResponse = rt.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                entity,
                JSONObject.class
        );

        MultiValueMap<String, String> newParams = new LinkedMultiValueMap<>();
        JSONObject obj = accessTokenResponse.getBody();
        String accessToken = obj.getAsString("access_token");
        RestTemplate newRt = new RestTemplate();

        headers.add("Authorization","token " + accessToken);

        HttpEntity<MultiValueMap<String,String>> newEntity = new HttpEntity<>(newParams, headers);

        ResponseEntity<JSONObject> userInfoResponse = newRt.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                newEntity,
                JSONObject.class
        );
        //TODO User엔티티에 repos_url 애트리뷰트 만들고 저장, LoginMethod e
        userInfoResponse.getBody().get("repos_url");
        String email = (String) (userInfoResponse.getBody().get("login") + "@github.com");

        User user = User.builder()
                .email(email)
                .role(Role.USER)
                        .build();
        userService.addUser(user);

        System.out.println("userInfoResponse = " + userInfoResponse);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.OK,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);
    }
}
