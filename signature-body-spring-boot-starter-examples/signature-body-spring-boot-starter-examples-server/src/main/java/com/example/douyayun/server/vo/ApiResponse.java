package com.example.douyayun.server.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回
 *
 * @param <T>
 */
@Data
public class ApiResponse<T> implements Serializable {

    private int code;

    private String message;

    private T data;

    private Long timestamp;

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public Long getTimestamp() {
        timestamp = System.currentTimeMillis();
        return timestamp;
    }
}