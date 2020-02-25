package com.pupulputulapps.oriyanewspaper.Models;

public class NewsPaperWebModel {
    private int id;
    private int order;
    private String type;
    private String name;
    private String website_link;
    private String epaper_link;
    private String image_name;
    private int clicks;

    public NewsPaperWebModel(int id, int order, String type, String name, String website_link, String epaper_link, String image_name, int clicks) {
        this.id = id;
        this.order = order;
        this.type = type;
        this.name = name;
        this.website_link = website_link;
        this.epaper_link = epaper_link;
        this.image_name = image_name;
        this.clicks = clicks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite_link() {
        return website_link;
    }

    public void setWebsite_link(String website_link) {
        this.website_link = website_link;
    }

    public String getEpaper_link() {
        return epaper_link;
    }

    public void setEpaper_link(String epaper_link) {
        this.epaper_link = epaper_link;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
}
