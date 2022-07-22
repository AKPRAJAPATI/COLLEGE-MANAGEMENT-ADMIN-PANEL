package com.geral_area.collegemanagementadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.geral_area.collegemanagementadmin.application.addCourse;
import com.geral_area.collegemanagementadmin.application.addLocation;
import com.geral_area.collegemanagementadmin.application.addNoticeActivity;
import com.geral_area.collegemanagementadmin.application.addPdfActivity;
import com.geral_area.collegemanagementadmin.application.addSliderActivity;
import com.geral_area.collegemanagementadmin.application.addTeacherActivity;
import com.geral_area.collegemanagementadmin.application.add_contectInfoActivity;
import com.geral_area.collegemanagementadmin.application.college_About_Activity;
import com.geral_area.collegemanagementadmin.application.teacherSlider;
import com.geral_area.collegemanagementadmin.application.uploadImageActivity;
import com.geral_area.collegemanagementadmin.authenticationArea.RegisterActivity;
import com.geral_area.collegemanagementadmin.databinding.ActivityMainBinding;
import com.geral_area.collegemanagementadmin.delete_dir.deleteActivity;
import com.geral_area.collegemanagementadmin.share.shareKeyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String collgename;
    String coursename;


    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        sharedPreferences = getSharedPreferences("ourUserData", MODE_PRIVATE);

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }

        ///////////get our college name//////////////////////
        collgename = sharedPreferences.getString("college_", "");
        coursename = sharedPreferences.getString("course_", "");

        binding.textView.setText(coursename);
        binding.collegeName.setText(collgename);

        binding.collegeName.setSelected(true);
        ///////////get our college name//////////////////////


        //marquee work is here
        binding.addSlider.setSelected(true);
        binding.addTeacherText.setSelected(true);
        binding.addUploadNotice.setSelected(true);
        binding.addUploadImage.setSelected(true);
        binding.addUploadPdf.setSelected(true);
        binding.addDeleteNotice.setSelected(true);
        binding.teacherSlider.setSelected(true);
        binding.addCollegeInfo.setSelected(true);
        ///////////end////////////

        //this is slider clicked work
        binding.addSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addSliderActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.imageslide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addSliderActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //this is add teacher clicked work
        binding.addTeacherText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addTeacherActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addTeacherActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Upload Image clicked
        binding.addUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), uploadImageActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.addUploadImageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), uploadImageActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Upload notice clicked
        binding.notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addNoticeActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.addUploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addNoticeActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Upload pdf clicked
        binding.addUploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addPdfActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.pdfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addPdfActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //Update date delete clicked
        binding.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), deleteActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.addDeleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), deleteActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //add Teacher slider
        binding.imageslideTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), teacherSlider.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.teacherSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), teacherSlider.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //add course
        binding.addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addCourse.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.addCourseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addCourse.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //addContect
        binding.addContect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), add_contectInfoActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.addContectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), add_contectInfoActivity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //addAboutOfCollege

        binding.addAboutOfCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), college_About_Activity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.addCollegeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), college_About_Activity.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //add map
        binding.mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addLocation.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.addMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addLocation.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    public void shareKey(MenuItem itemclicked) {
        Intent intent = new Intent(getApplicationContext(), shareKeyActivity.class);
        intent.putExtra("my_course", coursename);
        intent.putExtra("my_college", collgename);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}