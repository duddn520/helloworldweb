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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public Optional<User> searchUserById(Long Id){
        return userRepository.findById(Id);
    }

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

    @Transactional
    public String addKakaoUser(String accessToken) throws ParseException {

        String userInfoFromKakao = getUserInfoFromKakao(accessToken);

        //받은 String정보를 JSON 객체화
        JSONObject userInfo = stringToJson(userInfoFromKakao, "kakao_account");

        return addUser(userInfo);

    }

    @Transactional
    public String addNaverUser(String userInfoRespnoseFromNaver) throws ParseException {

        //받은 String정보를 JSON 객체화
        JSONObject userInfo = stringToJson(userInfoRespnoseFromNaver, "response");

        return addUser(userInfo);
    }

    @Transactional
    public String addGithubUser(JSONObject userInfoJsonObject)
    {
        String repo_url = (String) userInfoJsonObject.get("repos_url");
        String email = (userInfoJsonObject.get("login") + "@github.com");
        User user = User.builder()
                .email(email)
                .repo_url(repo_url)
                .role(Role.USER)
                .build();

        addUser(user);
        return jwtTokenProvider.createToken(email,Role.USER);
    }

    //네이버로 부터 사용자 정보 불러오는 부분
    public String getUserInfoFromNaver(String accessToken) {

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

    private String addUser(JSONObject userInfo){
        User newUser = User.builder()
                .email(userInfo.getAsString("email"))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(newUser);

        String token = jwtTokenProvider.createToken(savedUser.getEmail(), Role.USER);

        return token;
    }

    private JSONObject stringToJson(String str,String key) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(str);
        JSONObject userInfoFromKakaoJson = (JSONObject) obj;

        Object obj2 = jsonParser.parse(userInfoFromKakaoJson.getAsString(key));
        return (JSONObject) obj2;
    }

}
