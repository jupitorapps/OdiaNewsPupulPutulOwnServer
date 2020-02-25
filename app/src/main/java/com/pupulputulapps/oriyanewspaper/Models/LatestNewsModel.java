package com.pupulputulapps.oriyanewspaper.Models;

public class LatestNewsModel {
    private int id;
    private String title;
    private String description;
    private String pub_date;
    private String news_link;
    private String image_link;
    private String timestamp;
    private String news_source;

    public LatestNewsModel(int id, String title, String description, String pub_date, String news_link, String image_link, String timestamp, String news_source) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pub_date = pub_date;
        this.news_link = news_link;
        this.image_link = image_link;
        this.timestamp = timestamp;
        this.news_source = news_source;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPub_date() {
        return pub_date;
    }

    public String getNews_link() {
        return news_link;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNews_source() {
        return news_source;
    }
}
