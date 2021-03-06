package com.helloworld.helloworldweb.controller;

import com.helloworld.helloworldweb.domain.*;
import com.helloworld.helloworldweb.dto.Post.PostResponseDto;
import com.helloworld.helloworldweb.dto.PostSubComment.PostSubCommentResponseDto;
import com.helloworld.helloworldweb.dto.User.UserResponseDto;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.model.ApiResponse;
import com.helloworld.helloworldweb.model.HttpResponseMsg;
import com.helloworld.helloworldweb.model.HttpStatusCode;
import com.helloworld.helloworldweb.service.FileProcessService;
import com.helloworld.helloworldweb.service.PostService;
import com.helloworld.helloworldweb.service.PostSubCommentService;
import com.helloworld.helloworldweb.service.UserService;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserController extends HttpServlet {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostService postService;
    private final PostSubCommentService postSubCommentService;
    private final FileProcessService fileProcessService;

    // ????????? ????????? ??? ???????????? ??????
    @PostMapping("/api/user/register/kakao")
    public ResponseEntity<ApiResponse> registerUserWithKakao(HttpServletRequest request,HttpServletResponse response) throws ParseException {

        response.addHeader("Access-Control-Expose-Headers", "Auth");
        response.addHeader("Access-Control-Expose-Headers", "Refresh");

        // ???????????? ?????? ????????? ????????? ????????? ??????
        Map<String, Object> stringObjectMap = userService.addKakaoUser(request.getHeader("token"));

        String jwt = (String) stringObjectMap.get("accessToken");
        String refresh = (String) stringObjectMap.get("refreshToken");
        boolean isAlreadyRegister = (boolean) stringObjectMap.get("isAlreadyRegister");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Auth", jwt);
        headers.add("Refresh",refresh);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                stringObjectMap), headers, HttpStatus.OK);
    }

    //?????????????????? ??? ???????????? ??????
    @PostMapping("/api/user/register/naver")
    public ResponseEntity<ApiResponse> registerUserWithNaver(@RequestParam(name = "code") String code, @RequestParam("state") String state, HttpServletResponse response) throws ParseException {

        //???????????? ?????? ????????? ????????? ?????????.
        String accessToken = userService.getAccessTokenFromNaver(state, code);

        //????????? ????????? ????????? ???????????? ??????????????? ?????? ????????? ????????? ??? ????????? ??????
        Map<String, Object> stringObjectMap = userService.addNaverUser(accessToken);

        String jwt = (String) stringObjectMap.get("accessToken");
        String refresh = (String) stringObjectMap.get("refreshToken");
        boolean isAlreadyRegister = (boolean) stringObjectMap.get("isAlreadyRegister");

        response.addHeader("Auth", jwt);
        response.addHeader("Refresh",refresh);
        response.addHeader("Access-Control-Expose-Headers", "Auth");
        response.addHeader("Access-Control-Expose-Headers", "Refresh");


        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                stringObjectMap), HttpStatus.OK);
    }

    @PostMapping("/api/user/register/github")
    public ResponseEntity<ApiResponse> registerUserWithGithub(@RequestParam(name = "code") String code,HttpServletResponse servletresponse) throws ParseException {

        servletresponse.addHeader("Access-Control-Expose-Headers", "Auth");
        servletresponse.addHeader("Access-Control-Expose-Headers", "Refresh");


        String token = userService.getGithubAccessTokenByCode(code);

        JSONObject userInfo = userService.getGithubUserInfoByAccessToken(token);

        Map<String, Object> stringObjectMap = userService.addGithubUser(userInfo);

        String jwt = (String) stringObjectMap.get("accessToken");
        String refresh = (String) stringObjectMap.get("refreshToken");
        boolean isAlreadyRegister = (boolean) stringObjectMap.get("isAlreadyRegister");

        servletresponse.addHeader("Auth",jwt);
        servletresponse.addHeader("Refresh",refresh);

        return new ResponseEntity<>(ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                stringObjectMap), HttpStatus.OK);
    }

    //?????? db??? ????????? repo_url ?????? ????????? ??????????????? ??????, ??????????????? json ????????? ??????.
    @GetMapping("/api/user/repos_url")
    public ResponseEntity<ApiResponse> getGithubRepositories(@RequestParam(name = "email") String email, HttpServletRequest request, HttpServletResponse response)
    {
        User user = userService.getUserByEmail(email);

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
                                                                @RequestParam(value = "email", required = false) String email,
                                                                HttpServletResponse response) {
        if(email == null){
            // ????????? ??????????????? ?????? ??????(customAppbar ?????? ??????)
            String userEmail = jwtTokenProvider.getUserEmail(jwtToken);
            User findUser = userService.getUserByEmail(userEmail);
            UserResponseDto responseDto = new UserResponseDto(findUser, true);

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.GET_SUCCESS,
                    HttpResponseMsg.GET_SUCCESS,
                    responseDto), HttpStatus.OK);
        }
        else{
            //???????????? ?????? ?????? ??????
            User caller = userService.getUserByJwt(jwtToken);
            User findUser = userService.getUserByEmail(email);
            UserResponseDto responseDto = new UserResponseDto(findUser, caller.getId() == findUser.getId());

            return new ResponseEntity<>(ApiResponse.response(
                    HttpStatusCode.GET_SUCCESS,
                    HttpResponseMsg.GET_SUCCESS,
                    responseDto), HttpStatus.OK);
        }
    }

    //??????????????? ????????? ???????????? ????????????, ??????????????? ????????? ??? ??? ???????????? ????????? ??????.
    @PostMapping("/api/user/githubconnect")
    public ResponseEntity<ApiResponse> connectUserToGithub(@RequestParam(name = "code") String code, HttpServletRequest request, HttpServletResponse response)
    {
        String email = jwtTokenProvider.getUserEmail(jwtTokenProvider.getTokenByHeader(request));
        User user = userService.getUserByEmail(email);

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

    // ?????? ????????? ????????? ???????????? ??????
    @GetMapping("/api/user/qnas")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getUserQnas(@RequestParam(name= "id") Long id){
        List<Post> findQnas = postService.getAllUserPosts(id, Category.QNA);

        List<PostResponseDto> responseDtos = findQnas.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }

    // ?????? ????????? ????????? ???????????? ??????
    @Transactional
    @GetMapping("/api/user/comments")
    public ResponseEntity<ApiResponse<List<PostSubCommentResponseDto>>> getUserComments(@RequestParam(name= "id") Long id){
        List<Pair<Post, PostSubComment>> findAllUserComments = postSubCommentService.getAllUserComments(id);

        // List -> ResponseDto
        List<PostSubCommentResponseDto> responseDtos = findAllUserComments.stream()
                .map(PostSubCommentResponseDto::allUserCommentsDtos)
                .collect(Collectors.toList());

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.GET_SUCCESS,
                HttpResponseMsg.GET_SUCCESS,
                responseDtos), HttpStatus.OK);
    }

    //??? ????????? jwt ????????? ?????? ????????? ?????? ??? ??? ????????? ??????
    @GetMapping("/api/user/getnewtoken")
    public ResponseEntity<ApiResponse> getNewToken(@RequestHeader(name= "Auth") String jwtToken,
                                                   @RequestHeader(name = "Refresh") String refreshToken,
                                                   HttpServletResponse response){

        //??? ????????? ??????????????? ?????? 500??? ????????? ??? ?????? ?????? ??? ??????
        // 500??? ????????? ???
        // accessToken??? ????????? ????????? ??? -> ?????? ?????? ?????? ??????
        if(jwtTokenProvider.verifyToken(jwtToken)){
            return new ResponseEntity (ApiResponse.response(
                    HttpStatusCode.SERVICE_UNAVAILABLE,
                    HttpResponseMsg.SERVICE_UNAVAILABLE), HttpStatus.SERVICE_UNAVAILABLE);
        }
        else{
            // accessToken??? ????????? ????????????,
            // refresh??? ????????? ?????? ?????? -> accessToken??? ?????????
            if(jwtTokenProvider.verifyAllToken(refreshToken)){
                //?????? ????????? ?????? ??????
                String email = jwtTokenProvider.getUserEmail(refreshToken);
                Object role = jwtTokenProvider.getRole(refreshToken);
                String newJwt = jwtTokenProvider.createToken(email, Role.USER);

                response.addHeader("Auth", newJwt);
                response.addHeader("Access-Control-Expose-Headers", "Auth");

                return new ResponseEntity (ApiResponse.response(
                        HttpStatusCode.REISSUE_SUCCESS,
                        HttpResponseMsg.REISSUE_SUCCESS), HttpStatus.CREATED);
            }
            else{
                //refreshToken??? ?????? -> ?????? ?????????
                return new ResponseEntity (ApiResponse.response(
                        HttpStatusCode.NOT_ACCEPTABLE,
                        HttpResponseMsg.NEED_JOIN), HttpStatus.NOT_ACCEPTABLE);
            }
        }

    }

    @PostMapping ("/api/user/music")
    @Transactional
    public ResponseEntity<ApiResponse<String>> updateProfileMusic(@RequestParam(value = "file") MultipartFile file,
                                                                  @RequestParam(value = "id") Long userId) throws UnsupportedEncodingException {

        String uploadMusicUrl = fileProcessService.uploadMusic(file);
        User findUser = userService.getUserById(userId);
        userService.updateProfileMusic(findUser, file.getOriginalFilename(), uploadMusicUrl);

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                uploadMusicUrl), HttpStatus.OK);
    }

    @PostMapping("/api/user/fcm")
    public ResponseEntity<ApiResponse> updateFcm(HttpServletRequest request)
    {
        String email = jwtTokenProvider.getUserEmail(request.getHeader("Auth"));
        String fcmToken = request.getHeader("FCM");
        User user = userService.getUserByEmail(email);

        UserResponseDto responseDto = new UserResponseDto(userService.updateFcm(user,fcmToken));

        return new ResponseEntity (ApiResponse.response(
                HttpStatusCode.POST_SUCCESS,
                HttpResponseMsg.POST_SUCCESS,
                responseDto), HttpStatus.OK);
    }
}
