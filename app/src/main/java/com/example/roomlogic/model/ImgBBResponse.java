package com.example.roomlogic.model;

public class ImgBBResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        private String url;

        public String getUrl() {
            return url;
        }
    }
}