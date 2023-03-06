package com.example.photosaveprojectwfirebase.Activities.Holder;

public class PostDetailsModel {

    String location, time, description, img_url;

    PostDetailsModel(){

    }

    public PostDetailsModel(String konum, String zaman, String aciklama, String resim_url) {
        this.location = konum;
        this.time = zaman;
        this.description = aciklama;
        this.img_url = resim_url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
