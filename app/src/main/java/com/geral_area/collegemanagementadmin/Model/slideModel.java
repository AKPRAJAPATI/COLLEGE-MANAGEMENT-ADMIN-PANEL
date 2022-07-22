package com.geral_area.collegemanagementadmin.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

public class slideModel{
    String slideTitle;
    String slideImage;
    String  slideUniqueKey;

    public slideModel(String slideImage) {
        this.slideImage = slideImage;
    }

    public slideModel(String slideTitle, String slideImage) {
        this.slideTitle = slideTitle;
        this.slideImage = slideImage;
    }

    public slideModel() {
    }

    public String getSlideTitle() {
        return slideTitle;
    }

    public void setSlideTitle(String slideTitle) {
        this.slideTitle = slideTitle;
    }

    public String getSlideUniqueKey() {
        return slideUniqueKey;
    }

    public void setSlideUniqueKey(String slideUniqueKey) {
        this.slideUniqueKey = slideUniqueKey;
    }

    public String getSlideImage() {
        return slideImage;
    }

    public void setSlideImage(String slideImage) {
        this.slideImage = slideImage;
    }

}
