package com.helloworld.helloworldweb.firebase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Builder
@AllArgsConstructor
@Getter
public class FcmMessage {
    private boolean validate_only;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message{
        private String token;
        private Notification notification;
        private Map<String,Object> data;
        private Android android;
        private Apns apns;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification{
        private String title;
        private String body;
        private String image;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Android{
        private Map<String, Object> notification;
        private String priority;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Apns{
        private Map<String,Object> headers;
        private Map<String,Object> payload;
    }
}

