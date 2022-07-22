package com.geral_area.collegemanagementadmin.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.geral_area.collegemanagementadmin.Model.slideModel;
import com.geral_area.collegemanagementadmin.databinding.ActivityTeacherSliderBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class teacherSlider extends AppCompatActivity {
    private ActivityTeacherSliderBinding binding;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String course;
    private String college;

    private int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;

    private List<SlideModel> arrayList;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherSliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

//        sharedPreferences = getSharedPreferences("ourUserData", MODE_PRIVATE);
//          COURSE = sharedPreferences.getString("course_","");
//          COLLEGE = sharedPreferences.getString("college_","");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        arrayList = new ArrayList<>();

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");


        if (IMAGE_URI == null) {
            binding.cardView.setVisibility(View.GONE);
            binding.cardViewAddSlidesButton.setVisibility(View.VISIBLE);
        } else if (binding.teacherPosition.getText().toString().equals("")) {
            binding.cardView.setVisibility(View.VISIBLE);
            binding.cardViewAddSlidesButton.setVisibility(View.GONE);
        } else {
            binding.cardViewAddSlidesButton.setVisibility(View.GONE);
            binding.cardView.setVisibility(View.VISIBLE);
        }


        binding.selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        binding.uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.teacherPosition.getText().toString().equals("")) {
                    Toast.makeText(teacherSlider.this, "Please enter position", Toast.LENGTH_SHORT).show();
                    binding.teacherPosition.requestFocus();
                } else {
                    progressDialog.show();
                    storageReference.child(course).child(auth.getUid() + new Date().getTime()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_uri = uri.toString();
                                    //slideModel model = new slideModel(image_uri, binding.teacherPosition.getText().toString());
                                    slideModel model = new slideModel( binding.teacherPosition.getText().toString(),image_uri);

                                    databaseReference.child(course).child(college).child(auth.getUid()).child("Teacher Slider Image").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                binding.showImagePriview.setImageURI(null);
                                                binding.cardView.setVisibility(View.GONE);
                                                binding.cardViewAddSlidesButton.setVisibility(View.VISIBLE);
                                                Toast.makeText(teacherSlider.this, "Image Uploaded Success", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploading : " + (int) progress + "%");
                        }
                    });
                }
            }
        });

        getsliderImages();

    }

    //////////onpen gallery/////////////
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE || resultCode == RESULT_OK) {
            IMAGE_URI = data.getData();
            binding.showImagePriview.setImageURI(IMAGE_URI);

            binding.cardView.setVisibility(View.VISIBLE);
            binding.cardViewAddSlidesButton.setVisibility(View.GONE);
        }
    }

    /////////////////close work //////////////////
    private void getsliderImages() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("Teacher Slider Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String image_data = dataSnapshot.child("slideImage").getValue(String.class);
                    String image_text = dataSnapshot.child("slideTitle").getValue(String.class);
                    arrayList.add(new SlideModel(image_data , image_text, ScaleTypes.CENTER_CROP));
                    binding.imageSlider.setImageList(arrayList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}