package com.geral_area.collegemanagementadmin.Model;

public class courseModel {
    String course ;
    String uniqueId;

    public courseModel() {
    }

    public courseModel(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
