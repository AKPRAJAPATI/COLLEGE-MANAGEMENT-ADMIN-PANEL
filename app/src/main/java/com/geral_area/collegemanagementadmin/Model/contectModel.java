package com.geral_area.collegemanagementadmin.Model;

public class contectModel {
    private String authId;
    private String address;
    private String state;
    private String pincode;
    private String gmail;
    private String phone;

    public contectModel() {
    }

    public contectModel(String authId, String address, String state, String pincode, String gmail, String phone) {
        this.authId = authId;
        this.address = address;
        this.state = state;
        this.pincode = pincode;
        this.gmail = gmail;
        this.phone = phone;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
