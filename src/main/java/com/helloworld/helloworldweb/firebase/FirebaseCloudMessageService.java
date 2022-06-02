package com.helloworld.helloworldweb.firebase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    @Autowired
    ResourceLoader resourceLoader;

    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/helloworldweb-32e96/messages:send";
    private final ObjectMapper objectMapper;



    private String getAccessToken() throws IOException{
        Resource resource = resourceLoader.getResource(FCM_PRIVATE_KEY_PATH);
        InputStream inputStream = resource.getInputStream();
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();
    }

    public void sendMessageTo(String targetToken, String title, String body, String index) throws IOException {
        String message = makeMessage(targetToken, title, body, index);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();


        Response response = client.newCall(request)
                .execute();

        System.out.println(response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body,String index) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> apnHeader = new HashMap<>();
        Map<String, Object> apnPayload = new HashMap<>();
        Map<String, Object> andNotification = new HashMap<>();
        Map<String, Object> aps = new HashMap<>();
        map.put("index", index);
        map.put("time", LocalDateTime.now());
        andNotification.put("click_action", "TOP_STORY_ACTIVITY");
        apnHeader.put("apns_priority", "5");
        aps.put("sound", "default");
        aps.put("content-available", 1);

        apnPayload.put("aps", aps);

        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .data(map)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build())
                        .android(FcmMessage.Android.builder()
                                .notification(andNotification)
                                .build())
                        .apns(FcmMessage.Apns.builder()
                                .headers(apnHeader)
                                .payload(apnPayload)
                                .build())
                        .build())
                .validate_only(false)
                .build();


        return objectMapper.writeValueAsString(fcmMessage);
    }

}
