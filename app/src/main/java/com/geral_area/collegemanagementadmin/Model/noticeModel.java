package com.geral_area.collegemanagementadmin.Model;

public class noticeModel {
    String noticeImageText;
    String noticeImage;
    String authid;
    String noticeUnqueKey ;

    public noticeModel() {
    }

    public noticeModel(String noticeImageText, String noticeImage, String authid) {
        this.noticeImageText = noticeImageText;
        this.noticeImage = noticeImage;
        this.authid = authid;
    }

    public String getNoticeUnqueKey() {
        return noticeUnqueKey;
    }

    public void setNoticeUnqueKey(String noticeUnqueKey) {
        this.noticeUnqueKey = noticeUnqueKey;
    }

    public String getNoticeImageText() {
        return noticeImageText;
    }

    public void setNoticeImageText(String noticeImageText) {
        this.noticeImageText = noticeImageText;
    }

    public String getNoticeImage() {
        return noticeImage;
    }

    public void setNoticeImage(String noticeImage) {
        this.noticeImage = noticeImage;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }
}
