package com.tudo.chatify.response;

public class ApiResponse {
    private String message;
    private Boolean status;

    public ApiResponse(String message, Boolean status) {
        this.message = message;
        this.status = status;
    }
}
