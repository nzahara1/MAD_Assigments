package com.example.homework05;

public class News {

    private String title;

    private String publishedAt;

    private String urlToImage;

    private String author;

    private String url;

    public News(String title, String publishedAt, String urlToImage, String author, String url) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.urlToImage = urlToImage;
        this.author = author;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }
}
