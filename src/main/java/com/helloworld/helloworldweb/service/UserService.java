package com.helloworld.helloworldweb.service;

import com.helloworld.helloworldweb.domain.Role;
import com.helloworld.helloworldweb.domain.User;
import com.helloworld.helloworldweb.jwt.JwtTokenProvider;
import com.helloworld.helloworldweb.repository.UserRepository;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

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
    public String addKakaoUser(String accessToken) throws ParseException {

        String userInfoFromKakao = getUserInfoFromKakao(accessToken);

        //받은 String정보를 JSON 객체화
        JSONObject userInfo = stringToJson(userInfoFromKakao, "kakao_account");

        return addUser(userInfo);

    }

    @Transactional
    public String addNaverUser(String accessToken) throws ParseException {

        String userInfoRespnoseFromNaver = getUserInfoFromNaver(accessToken);

        //받은 String정보를 JSON 객체화
        JSONObject userInfo = stringToJson(userInfoRespnoseFromNaver, "response");

        return addUser(userInfo);
    }

    @Transactional
    public String addGithubUser(JSONObject userInfoJsonObject)
    {
        String repo_url = (String) userInfoJsonObject.get("repos_url");
        String email = (userInfoJsonObject.get("login") + "@github.com");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent())
        {
            return jwtTokenProvider.createToken(email,Role.USER);
        }else
        {
            User user = User.builder()
                    .email(email)
                    .repo_url(repo_url)
                    .role(Role.USER)
                    .build();

            addUser(user);

            return jwtTokenProvider.createToken(email,Role.USER);
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

        System.out.println("responseBody = " + responseBody);
        return responseBody;

    }

    //네이버로 부터 사용자 정보 불러오는 부분
    private String getUserInfoFromKakao(String accessToken) {

        String token = accessToken; // 네이버 로그인 접근 토큰;
        String header = "Bearer " + token; // Bearer 다음에 공백 추가


        String apiURL = "https://kapi.kakao.com/v2/user/me";


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String responseBody = get(apiURL,requestHeaders);

        System.out.println("responseBody = " + responseBody);
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

    //각 소셜 로그인으로부터 받은 정보로 회원가입 시키는 함수
    //JWT를 반환
    private String addUser(JSONObject userInfo){

        // 유저가 이미 DB에 존재하는지 확인
        Optional<User> findUser = userRepository.findByEmail(userInfo.getAsString(("email")));

        // 존재 유무 isPresent()로 확인
        if( findUser.isPresent() ){
            return jwtTokenProvider.createToken(findUser.get().getEmail(),findUser.get().getRole());
        } else{
            User newUser = User.builder()
                    .email(userInfo.getAsString("email"))
                    .role(Role.USER)
                    .build();
            User savedUser = userRepository.save(newUser);
            return jwtTokenProvider.createToken(savedUser.getEmail(), Role.USER);
        }

    }

    private JSONObject stringToJson(String str, String key) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(str);
        JSONObject newJson = (JSONObject) obj;

        Object obj2 = jsonParser.parse(newJson.getAsString(key));
        return (JSONObject) obj2;
    }

    public String getAccessTokenFromNaver(String state, String code) {

        HttpURLConnection con = connect(" https://nid.naver.com/oauth2.0/token?client_id=3RFZ_7joHf_HlJXavuMB&client_secret=luFryHID4J&grant_type=authorization_code&state="+state+"&code="+code);
        try {
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                String response = readBody(con.getInputStream());

                JSONParser parser = new JSONParser();
                Object obj = parser.parse(response);
                JSONObject responseJSON = (JSONObject) obj;

                String access_token = responseJSON.getAsString("access_token");

                return access_token;

            } else { // 에러 발생
                throw new RuntimeException("API 엑세스 토큰 요청 실패");
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

}
