package com.example.roomlogic.model;

import java.util.List;

public class UnsplashResponse {
    private List<UnsplashImage> results;

    public List<UnsplashImage> getResults() {
        return results;
    }

    public static class UnsplashImage {
        private Urls urls;

        public Urls getUrls() {
            return urls;
        }
    }

    public static class Urls {
        private String regular;

        public String getRegular() {
            return regular;
        }
    }
}
