package com.geral_area.collegemanagementadmin.Model;

public class teacherModel {
    String teacherProfile;
    String teacherName;
    String teacherPhone;
    String teacherPost;
    String teacherSubjects;
    String authid;
    String teacher_uniqueKey ;

    public teacherModel(String teacherProfile, String teacherName, String teacherPhone, String teacherPost, String teacherSubjects, String authid) {
        this.teacherProfile = teacherProfile;
        this.teacherName = teacherName;
        this.teacherPhone = teacherPhone;
        this.teacherPost = teacherPost;
        this.teacherSubjects = teacherSubjects;
        this.authid = authid;
    }

    public teacherModel() {
    }

    public teacherModel(String teacherProfile, String teacherName, String teacherPhone, String teacherPost, String teacherSubjects, String authid, String teacher_uniqueKey) {
        this.teacherProfile = teacherProfile;
        this.teacherName = teacherName;
        this.teacherPhone = teacherPhone;
        this.teacherPost = teacherPost;
        this.teacherSubjects = teacherSubjects;
        this.authid = authid;
        this.teacher_uniqueKey = teacher_uniqueKey;
    }

    public String getTeacherProfile() {
        return teacherProfile;
    }

    public void setTeacherProfile(String teacherProfile) {
        this.teacherProfile = teacherProfile;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public String getTeacher_uniqueKey() {
        return teacher_uniqueKey;
    }

    public void setTeacher_uniqueKey(String teacher_uniqueKey) {
        this.teacher_uniqueKey = teacher_uniqueKey;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public String getTeacherPost() {
        return teacherPost;
    }

    public void setTeacherPost(String teacherPost) {
        this.teacherPost = teacherPost;
    }

    public String getTeacherSubjects() {
        return teacherSubjects;
    }

    public void setTeacherSubjects(String teacherSubjects) {
        this.teacherSubjects = teacherSubjects;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }
}
