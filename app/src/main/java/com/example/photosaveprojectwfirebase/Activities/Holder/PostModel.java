package com.example.photosaveprojectwfirebase.Activities.Holder;

public class PostModel {

    String documentId, location, time, description, imgUrl, uid;

    PostModel(){

    }

    public PostModel(String documentId,String konum, String zaman, String aciklama, String resimUrl, String uid) {
        this.documentId = documentId;
        this.location = konum;
        this.time = zaman;
        this.description = aciklama;
        this.imgUrl = resimUrl;
        this.uid = uid;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
