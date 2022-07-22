package com.geral_area.collegemanagementadmin.Model;

public class registerModel {
   private String  profile_image;
   private String name;
   private String  email;
   private String password;
   private String college;
   private String course;
   private String auth_id;

    public registerModel() {
    }

    public registerModel(String profile_image, String name, String email, String password, String college, String course, String auth_id) {
        this.profile_image = profile_image;
        this.name = name;
        this.email = email;
        this.password = password;
        this.college = college;
        this.course = course;
        this.auth_id = auth_id;
    }

    public registerModel(String college, String course, String auth_id) {
        this.college = college;
        this.course = course;
        this.auth_id = auth_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }
}
