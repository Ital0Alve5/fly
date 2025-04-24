package com.dac.fly.authservice.dto.response;

public class ApiResponse<T> {
    private boolean error;
    private T data;
    private String message;
    private Integer statusCode;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> res = new ApiResponse<>();
        res.error = false;
        res.data = data;
        return res;
    }

    public static <T> ApiResponse<T> error(String message, int statusCode) {
        ApiResponse<T> res = new ApiResponse<>();
        res.error = true;
        res.message = message;
        res.statusCode = statusCode;
        return res;
    }

    public boolean isError() {
        return error;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
