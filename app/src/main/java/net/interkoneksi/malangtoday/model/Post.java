package net.interkoneksi.malangtoday.model;

import java.util.ArrayList;


public class Post {
    private String title;
    private String content;
    private String thumbnailUrl;
    private String featuredImageUrl = "";
    private String viewCount;
    private String date;
    private String author;
    private String url;
    private int id;
    private ArrayList<String> categories;

    public Post(){

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Post && this.getId() == (((Post) obj).getId());
    }
    @Override
    public int hashCode(){
        return Integer.valueOf(this.getId()).hashCode();
    }
    public int getId(){return id;}

    public void setId(int id){this.id= id;}

    public String getTitle(){ return  title;}
    public void setTitle(String title){this.title= title;}
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }

    public void setFeaturedImageUrl(String featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

}
