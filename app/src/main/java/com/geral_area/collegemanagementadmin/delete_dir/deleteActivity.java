package com.geral_area.collegemanagementadmin.delete_dir;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.geral_area.collegemanagementadmin.MainActivity;
import com.geral_area.collegemanagementadmin.databinding.ActivityDeleteBinding;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.teacherSliders.updateTeacherSlidersActivity;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateImages.updateImageListActivity;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateNotice.updateNoticeListActivity;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updatePdf.pdfListActivity;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateSlides.updateSlidesActivity;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateTeacherPackage.updateTeacherListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class deleteActivity extends AppCompatActivity {
    private ActivityDeleteBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private final int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;
    private String course;
    private String college;
    private List<SlideModel> arrayList,arrayListTeacher;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Notice");
        progressDialog.setCancelable(false);

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        arrayList = new ArrayList<>();
        arrayListTeacher = new ArrayList<>();

        ///////////////////ALL METHODS IS HERE//////////////////////
        getsliderImages();
        getsliderTeacherImages();

        binding.btnChangeSlideImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), updateSlidesActivity.class);
                intent.putExtra("my_course", course);
                intent.putExtra("my_college", college);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.btnChangeTeacherSlideImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), updateTeacherSlidersActivity.class);
                intent.putExtra("my_course", course);
                intent.putExtra("my_college", college);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////////////
//        binding.btnChangeSlideImages.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(deleteActivity.this);
//                builder.setCancelable(false);
//                builder.setTitle("Warning");
//                builder.setMessage("You want to really reset all images");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                        databaseReference.child(course).child(college).child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                arrayList.clear();
//                                if (snapshot.hasChild("Slider Image")) {
//                                    if (snapshot.exists()) {
//                                        snapshot.child("Slider Image").getRef().setValue(null);
//                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                                    }
//                                } else {
//                                    Toast.makeText(deleteActivity.this, "Images Not Found Please add any Image", Toast.LENGTH_LONG).show();
//                                }
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(deleteActivity.this, "OK No problem", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
//            }
//        });
        binding.btnUpdateTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), updateTeacherListActivity.class);
                intent.putExtra("my_course", course);
                intent.putExtra("my_college", college);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        binding.btnUpdateNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), updateNoticeListActivity.class);
                intent.putExtra("my_course", course);
                intent.putExtra("my_college", college);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), updateImageListActivity.class);
                intent.putExtra("my_course", course);
                intent.putExtra("my_college", college);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.btnUpdatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), pdfListActivity.class);
                intent.putExtra("my_course", course);
                intent.putExtra("my_college", college);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    private void getsliderImages() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("Slider Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String image_data = dataSnapshot.child("slideImage").getValue(String.class);
                    arrayList.add(new SlideModel(image_data, ScaleTypes.CENTER_CROP));
                    binding.imageSlider.setImageList(arrayList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void getsliderTeacherImages() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("Teacher Slider Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListTeacher.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String image_data = dataSnapshot.child("slideImage").getValue(String.class);
                    String teacherPos = dataSnapshot.child("slideTitle").getValue(String.class);
                    arrayListTeacher.add(new SlideModel(image_data, teacherPos, ScaleTypes.CENTER_CROP));
                    binding.imageteacherSlider.setImageList(arrayListTeacher);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}