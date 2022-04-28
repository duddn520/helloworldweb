package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.dto.Post.PostRequestDto;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.dto.User.UserResponseDto;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.UserService;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController extends HttpServlet {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 카카오 로그인 및 회원가입 요청
    @PostMapping("/user/register/kakao")
    public ResponseEntity<ApiResponse> registerUserWithKakao(HttpServletRequest request,HttpServletResponse response) throws ParseException {

        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Expose-Headers", "Auth");

        // 카카오로 부터 받아온 정보로 유저로 등록
        String jwt = userService.addKakaoUser(request.getHeader("token"));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Auth", jwt);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS), headers, HttpStatus.OK);
    }

    //네이버로그인 및 회원가입 요청
    @PostMapping("/user/register/naver")
    public ResponseEntity<ApiResponse> registerUserWithNaver(@RequestParam(name = "code") String code, @RequestParam("state") String state, HttpServletResponse response) throws ParseException {

        //네이버로 부터 엑세스 토큰을 받아옴.
        String accessToken = userService.getAccessTokenFromNaver(state, code);

        //받아온 엑세스 토큰을 사용하여 네이버에서 유저 정보를 받아온 후 유저로 등록
        String jwt = userService.addNaverUser(accessToken);

        response.addHeader("Auth", jwt);
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Expose-Headers", "Auth");

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/user/register/github")
    public ResponseEntity<ApiResponse> registerUserWithGithub(@RequestParam(name = "code") String code,HttpServletResponse servletresponse) throws ParseException {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id","105e0b50eefc27b4dc81");
        params.add("client_secret","ce4d0a93a257529e78a8804f322ca629b1d7cba6");
        params.add("code",code);

        HttpHeaders headers = new HttpHeaders();

        servletresponse.addHeader("Access-Control-Allow-Origin","*");
        servletresponse.addHeader("Access-Control-Expose-Headers", "Auth");

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
        //TODO User엔티티에 repos_url 애트리뷰트 만들고 저장, LoginMethod 추가.

        String jwt = userService.addGithubUser(userInfoResponse.getBody());

        headers.add("Auth",jwt);
        System.out.println("jwt = " + jwt);

        System.out.println("userInfoResponse = " + userInfoResponse);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS),headers, HttpStatus.OK);
    }

    //유저 db에 저장된 repo_url 통해 깃허브 레포지토리 조회, 레포지토리 json 리스트 반환.
    @GetMapping("/user/repos_url")
    public ResponseEntity<ApiResponse> getGithubRepositories(HttpServletRequest request, HttpServletResponse response)
    {
         String email = jwtTokenProvider.getUserEmail(jwtTokenProvider.getTokenByHeader(request));
         User user = userService.getUserByEmail(email);

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Auth");

         if(user.getRepo_url().equals(null))
         {
             return new ResponseEntity<>(ApiResponse.response(
                     HttpStatusCode.NO_CONTENT,
                     HttpResponseMsg.NO_CONTENT), HttpStatus.NO_CONTENT);
         }
         else {

             RestTemplate rt = new RestTemplate();
             HttpHeaders headers = new HttpHeaders();
             MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

             headers.add("Authorization","token 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");
             HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params, headers);

             ResponseEntity<Object[]> res = rt.exchange(user.getRepo_url(),HttpMethod.GET,entity,Object[].class);
             Object[] objects = res.getBody();

             return new ResponseEntity<>(ApiResponse.response(
                     HttpStatusCode.GET_SUCCESS,
                     HttpResponseMsg.GET_SUCCESS,
                     objects), HttpStatus.OK);
         }
    }

    @GetMapping("/api/user")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@RequestHeader(value = "Auth") String jwtToken) {

        String userEmail = jwtTokenProvider.getUserEmail(jwtToken);
        User findUser = userService.getUserByEmail(userEmail);
        UserResponseDto responseDto = new UserResponseDto(findUser);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDto), HttpStatus.OK);
    }
}
