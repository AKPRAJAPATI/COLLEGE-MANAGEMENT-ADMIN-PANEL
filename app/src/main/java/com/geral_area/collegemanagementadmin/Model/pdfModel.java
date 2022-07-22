package com.geral_area.collegemanagementadmin.Model;

public class pdfModel {

    String pdf_url;
    String pdf_name;
    String authid;
    String uniqueKey;

    public pdfModel() {
    }

    public pdfModel(String pdf_url, String pdf_name, String authid) {
        this.pdf_url = pdf_url;
        this.pdf_name = pdf_name;
        this.authid = authid;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getPdf_name() {
        return pdf_name;
    }

    public void setPdf_name(String pdf_name) {
        this.pdf_name = pdf_name;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getAuthid() {
        return authid;
    }

    public void setAuthid(String authid) {
        this.authid = authid;
    }
}
