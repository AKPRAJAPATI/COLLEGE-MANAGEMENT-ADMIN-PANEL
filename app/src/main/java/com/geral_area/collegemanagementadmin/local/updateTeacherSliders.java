package com.geral_area.collegemanagementadmin.local;

import android.net.Uri;
import android.widget.EditText;

public interface updateTeacherSliders {
    void updateClickLister(int position, String uniqueKey , String editData , String imageUrl , EditText editText);
    void deleteClickLister(int position, String uniqueKey , String editData);
    void selectImage(int position, String uniqueKey , String editData);

}

