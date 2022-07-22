package com.geral_area.collegemanagementadmin.Model;

public class ImageModel {

    String Image_url;
    String uniqueId;
    String authid;


    public ImageModel(String image_url) {
        Image_url = image_url;
    }

    public ImageModel(String image_url, String authid) {
        Image_url = image_url;
        this.authid = authid;
    }

    public ImageModel() {
    }

    public String getImage_url() {
        return Image_url;
    }

    public void setImage_url(String image_url) {
        Image_url = image_url;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }
}
