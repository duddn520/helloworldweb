package com.helloworld.helloworldweb.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.repository.UserRepository;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final FileProcessService fileProcessService;

    @Transactional
    public User addUser(User user)
    {
        return userRepository.save(user);
    }

    @Transactional
    public User getUserById(Long userId) {

        User findUser = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("로그인상태가 아닙니다."));

        return findUser;
    }

    // Jwt 안의 이메일 정보를 통해 유저를 찾기 위한 함수
    @Transactional
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
    }

    @Transactional
    public User getUserWithPostByEmail(String email){
        return userRepository.findUserWithPostByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
    }

    // JWT를 사용하여 User를 찾아주는 함수
    @Transactional
    public User getUserByJwt(String jwtToken) {
        if( jwtToken == null )
            return User.builder().role(Role.GUEST).build();
        String userEmail = jwtTokenProvider.getUserEmail(jwtToken);
        User findUser = getUserByEmail(userEmail);

        return findUser;
    }

    @Transactional
    public Map<String,Object> addKakaoUser(String accessToken) throws ParseException {

        String userInfoFromKakao = getUserInfoFromKakao(accessToken);

        //받은 String정보를 JSON 객체화
        JsonObject total = stringToJson(userInfoFromKakao);
        String accountInfoString = total.get("kakao_account").toString();
        JsonObject userInfo = stringToJson(accountInfoString);
        String profileString = userInfo.get("profile").toString();
        JsonObject profileInfo = stringToJson(profileString);


        // 필요한 정보들
        String email = userInfo.get("email").getAsString();
        String profileUrl = profileInfo.get("profile_image_url").getAsString();


        return addUser(email,profileUrl);

    }

    @Transactional
    public Map<String,Object> addNaverUser(String accessToken) throws ParseException {

        String userInfoRespnoseFromNaver = getUserInfoFromNaver(accessToken);

        //받은 String정보를 JSON 객체화
        JsonObject total = stringToJson(userInfoRespnoseFromNaver);
        String accountInfoString = total.get("response").toString();
        JsonObject userInfo = stringToJson(accountInfoString);


        // 필요한 정보들
        String email = userInfo.get("email").getAsString() + "/";
        String profileUrl = userInfo.get("profile_image").getAsString();

        return addUser(email, profileUrl);
    }

    @Transactional
    public Map<String,Object> addGithubUser(JSONObject userInfoJsonObject)
    {
        String repo_url = (String) userInfoJsonObject.get("repos_url");
        String email = (userInfoJsonObject.get("login") + "@github.com");
        String profile_url = (String) userInfoJsonObject.get("avatar_url");

        Optional<User> userOptional = userRepository.findByEmail(email);

        Map<String,Object> map = new HashMap<>();

        if(userOptional.isPresent())
        {
            map.put("accessToken", jwtTokenProvider.createToken(email,Role.USER));
            map.put("refreshToken", jwtTokenProvider.createRefreshToken(email,Role.USER));
            map.put("isAlreadyRegister", true);

            return map;
        }else
        {
            User user = User.builder()
                    .email(email)
                    .repo_url(repo_url)
                    .profileUrl(profile_url)
                    .nickName(email)
                    .role(Role.USER)
                    .build();

            userRepository.save(user);

            map.put("accessToken", jwtTokenProvider.createToken(email,Role.USER));
            map.put("refreshToken",jwtTokenProvider.createRefreshToken(email,Role.USER));
            map.put("isAlreadyRegister", false);

            return map;
        }
    }

    //네이버로 부터 사용자 정보 불러오는 부분
    private String getUserInfoFromNaver(String accessToken) {

        String token = accessToken; // 네이버 로그인 접근 토큰;
        String header = "Bearer " + token; // Bearer 다음에 공백 추가


        String apiURL = "https://openapi.naver.com/v1/nid/me";


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL,requestHeaders);

        return responseBody;

    }

    //카카오로 부터 사용자 정보 불러오는 부분
    private String getUserInfoFromKakao(String accessToken) {

        String token = accessToken; // 네이버 로그인 접근 토큰;
        String header = "Bearer " + token; // Bearer 다음에 공백 추가


        String apiURL = "https://kapi.kakao.com/v2/user/me";


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL,requestHeaders);

        return responseBody;
    }

    //InputStream을 읽는 함수
    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    //http url 연결을 여는 함수
    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    //http get 요청을 하는 함수
    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            con.setConnectTimeout(2000);
            con.setReadTimeout(2000);

            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }
    private static String get(String apiUrl){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            con.setConnectTimeout(2000);
            con.setReadTimeout(2000);

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    //각 소셜 로그인으로부터 받은 정보로 회원가입 시키는 함수
    //JWT를 반환
    private Map<String,Object> addUser(String email,String profileUrl){

        // 유저가 이미 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findByEmail(email);

        Map<String,Object> map = new HashMap<>();

        // 존재 유무 isPresent()로 확인
        if( findUser.isPresent() ){

            map.put("accessToken", jwtTokenProvider.createToken(findUser.get().getEmail(),findUser.get().getRole()));
            map.put("refreshToken",jwtTokenProvider.createRefreshToken(findUser.get().getEmail(),findUser.get().getRole()));
            map.put("isAlreadyRegister", true);

            return map;
        } else{
            User newUser = User.builder()
                    .email(email)
                    .profileUrl(profileUrl)
                    .repo_url(" ")
                    .role(Role.USER)
                    .nickName(email.split("/")[0])
                    .build();
            User savedUser = userRepository.save(newUser);

            map.put("accessToken", jwtTokenProvider.createToken(savedUser.getEmail(), Role.USER));
            map.put("refreshToken",jwtTokenProvider.createRefreshToken(savedUser.getEmail(),Role.USER));
            map.put("isAlreadyRegister", false);

            return map;
        }

    }

    private JsonObject stringToJson(String str) {
        JsonObject json = JsonParser.parseString(str).getAsJsonObject();
        return json;
    }

    public String getAccessTokenFromNaver(String state, String code) throws ParseException {
        String response = get(" https://nid.naver.com/oauth2.0/token?client_id=3RFZ_7joHf_HlJXavuMB&client_secret=luFryHID4J&grant_type=authorization_code&state="+state+"&code="+code);
        String access_token = stringToJson(response).get("access_token").getAsString();

        return access_token;
    }

    public String getGithubAccessTokenByCode(String code)
    {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id","105e0b50eefc27b4dc81");
        params.add("client_secret","ce4d0a93a257529e78a8804f322ca629b1d7cba6");
        params.add("code",code);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<JSONObject> accessTokenResponse = rt.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                entity,
                JSONObject.class
        );

        JSONObject obj = accessTokenResponse.getBody();
        return obj.getAsString("access_token");

    }

    public JSONObject getGithubUserInfoByAccessToken(String token)
    {
        System.out.println("token = " + token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Token "+ token);

        HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<>(params, headers);
        RestTemplate rt = new RestTemplate();

        ResponseEntity<JSONObject> userInfoResponse = rt.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                entity,
                JSONObject.class
        );

        return userInfoResponse.getBody();

    }

    public void updateUserRepoUrl(User user, String repo_url)
    {
        user.updateRepoUrl(repo_url);
        userRepository.save(user);
    }

    public void updateNickName(User user, String nickName) {

        user.updateNickName(nickName);
        userRepository.save(user);
    }

    public void updateProfileMusic(User user, String fileName, String url) throws UnsupportedEncodingException {

        if(user.getProfileMusic() != null){
            String deleteTarget = user.getProfileMusic();
            String decodeTarget = URLDecoder.decode(deleteTarget, "UTF-8");
            fileProcessService.deleteMusic(fileProcessService.getFileName(decodeTarget));
        }
        user.updateProfileMusic(fileName, url);
        userRepository.save(user);
    }

    @Transactional
    public User updateFcm(User user ,String fcmToken)
    {
        user.updateFcm(fcmToken);
        return userRepository.save(user);
    }


}
