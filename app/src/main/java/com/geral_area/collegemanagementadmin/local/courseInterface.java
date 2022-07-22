package com.geral_area.collegemanagementadmin.local;

import android.widget.EditText;

public interface courseInterface {
    void updateCourse (String uniqueKey, int position , EditText editText);
    void deleteCourse(String uniqueKey, int position);
}
