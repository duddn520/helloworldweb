package com.helloworld.helloworldweb.controller;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import java.util.Map;
import java.util.Optional;

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
        Map<String, Object> stringObjectMap = userService.addKakaoUser(request.getHeader("token"));

        String jwt = (String) stringObjectMap.get("token");
        boolean isAlreadyRegister = (boolean) stringObjectMap.get("isAlreadyRegister");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Auth", jwt);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                stringObjectMap), headers, HttpStatus.OK);
    }

    //네이버로그인 및 회원가입 요청
    @PostMapping("/user/register/naver")
    public ResponseEntity<ApiResponse> registerUserWithNaver(@RequestParam(name = "code") String code, @RequestParam("state") String state, HttpServletResponse response) throws ParseException {

        //네이버로 부터 엑세스 토큰을 받아옴.
        String accessToken = userService.getAccessTokenFromNaver(state, code);

        //받아온 엑세스 토큰을 사용하여 네이버에서 유저 정보를 받아온 후 유저로 등록
        Map<String, Object> stringObjectMap = userService.addNaverUser(accessToken);

        String jwt = (String) stringObjectMap.get("token");
        boolean isAlreadyRegister = (boolean) stringObjectMap.get("isAlreadyRegister");

        response.addHeader("Auth", jwt);
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Expose-Headers", "Auth");

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                stringObjectMap), HttpStatus.OK);
    }

    @PostMapping("/user/register/github")
    public ResponseEntity<ApiResponse> registerUserWithGithub(@RequestParam(name = "code") String code,HttpServletResponse servletresponse) throws ParseException {

        servletresponse.addHeader("Access-Control-Allow-Origin","*");
        servletresponse.addHeader("Access-Control-Expose-Headers", "Auth");

        String token = userService.getGithubAccessTokenByCode(code);

        JSONObject userInfo = userService.getGithubUserInfoByAccessToken(token);

        Map<String, Object> stringObjectMap = userService.addGithubUser(userInfo);

        String jwt = (String) stringObjectMap.get("token");
        boolean isAlreadyRegister = (boolean) stringObjectMap.get("isAlreadyRegister");

        servletresponse.addHeader("Auth",jwt);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                stringObjectMap), HttpStatus.OK);
    }

    //유저 db에 저장된 repo_url 통해 깃허브 레포지토리 조회, 레포지토리 json 리스트 반환.
    @GetMapping("/user/repos_url")
    public ResponseEntity<ApiResponse> getGithubRepositories(HttpServletRequest request, HttpServletResponse response)
    {
         String email = jwtTokenProvider.getUserEmail(jwtTokenProvider.getTokenByHeader(request));
         User user = userService.getUserByEmail(email);

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Auth");


         if(user.getRepo_url().equals(" "))
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
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@RequestHeader(value = "Auth") String jwtToken,
                                                                @RequestParam(value = "email", required = false) String email) {
        if(email == null){
            // 자기가 자기자신의 정보 조회(customAppbar 에서 사용)
            String userEmail = jwtTokenProvider.getUserEmail(jwtToken);
            User findUser = userService.getUserByEmail(userEmail);
            UserResponseDto responseDto = new UserResponseDto(findUser, true);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.GET_SUCCESS,
                    HttpResponseMsg.GET_SUCCESS,
                    responseDto), HttpStatus.OK);
        }
        else{
            //이메일로 유저 정보 조회
            User caller = userService.getUserByJwt(jwtToken);
            User findUser = userService.getUserByEmail(email);
            UserResponseDto responseDto = new UserResponseDto(findUser, caller.getId() == findUser.getId());

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.GET_SUCCESS,
                    HttpResponseMsg.GET_SUCCESS,
                    responseDto), HttpStatus.OK);
        }
    }

    //프론트에서 생성할 연동하기 버튼에서, 인가코드를 받아온 후 이 컨트롤러 메서드 실행.
    @PostMapping("/user/githubconnect")
    public ResponseEntity<ApiResponse> connectUserToGithub(@RequestParam(name = "code") String code, HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("method init");
        System.out.println("code = " + code);
        String email = jwtTokenProvider.getUserEmail(jwtTokenProvider.getTokenByHeader(request));
        User user = userService.getUserByEmail(email);

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Auth");

        String token = userService.getGithubAccessTokenByCode(code);
        JSONObject userInfoRes = userService.getGithubUserInfoByAccessToken(token);

        String repo_api_url = (String) userInfoRes.get("repos_url");

        userService.updateUserRepoUrl(user,repo_api_url);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.PUT_SUCCESS,
                HttpResponseMsg.PUT_SUCCESS), HttpStatus.OK);

    }

    @PutMapping("/api/user/nickname")
    public ResponseEntity<ApiResponse> updateNickName(@RequestHeader(value = "Auth") String jwtToken,
                                                      @RequestParam(name="nickName")String nickName){

        User targetUser = userService.getUserByJwt(jwtToken);
        userService.updateNickName(targetUser, nickName);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.PUT_SUCCESS,
                HttpResponseMsg.PUT_SUCCESS), HttpStatus.OK);
    }
}
