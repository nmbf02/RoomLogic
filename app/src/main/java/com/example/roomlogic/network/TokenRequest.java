package com.example.roomlogic.network;

public class TokenRequest {
    private String title;
    private String body;
    private String token;

    public TokenRequest(String title, String body, String token) {
        this.title = title;
        this.body = body;
        this.token = token;
    }

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getToken() { return token; }
}