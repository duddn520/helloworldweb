package com.helloworld.helloworldweb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String httpResponseMessage;
    private T data;


    public ApiResponse(final int statusCode, final String httpResponseMessage) {
        this.statusCode = statusCode;
        this.httpResponseMessage = httpResponseMessage;
        this.data = null;
    }

    public static<T> ApiResponse<T> response(final int stautsCode, final String httpResponseMessage) {
        return response(stautsCode, httpResponseMessage, null);
    }

    public static<T> ApiResponse<T> response(final int stautsCode, final String httpResponseMessage, final T t) {
        return ApiResponse.<T>builder()
                .data(t)
                .statusCode(stautsCode)
                .httpResponseMessage(httpResponseMessage)
                .build();
    }
}
