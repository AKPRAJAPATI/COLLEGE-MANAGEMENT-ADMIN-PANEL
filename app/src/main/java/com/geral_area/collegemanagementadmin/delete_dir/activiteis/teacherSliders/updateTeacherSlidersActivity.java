package com.geral_area.collegemanagementadmin.delete_dir.activiteis.teacherSliders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.geral_area.collegemanagementadmin.Model.pdfModel;
import com.geral_area.collegemanagementadmin.Model.slideModel;
import com.geral_area.collegemanagementadmin.databinding.ActivityUpdateTeacherSlidersBinding;
import com.geral_area.collegemanagementadmin.delete_dir.adapters.update_teacher_SlideAdapters;
import com.geral_area.collegemanagementadmin.local.updateTeacherSliders;
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
import java.util.HashMap;

public class updateTeacherSlidersActivity extends AppCompatActivity implements updateTeacherSliders {
    private ActivityUpdateTeacherSlidersBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String course;
    private String college;
    private pdfModel modellingValue;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressDialog progressDialog;
    private ArrayList<slideModel> arrayList;
    private update_teacher_SlideAdapters updateSlideAdapters;

    int GALLERY_REQUEST_CODE;
    private Uri IMAGE_URI;
    String imageUniqueKey;
    String teacherPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateTeacherSlidersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Slide");
        progressDialog.setCancelable(false);
        modellingValue = new pdfModel();


        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");
        binding.updateSlidesRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.updateSlidesRecyclerview.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        updateSlideAdapters = new update_teacher_SlideAdapters(getApplicationContext(), arrayList, this);
        getOurSlides();
        binding.updateSlidesRecyclerview.setAdapter(updateSlideAdapters);


    }

    private void getOurSlides() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("Teacher Slider Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        slideModel model = dataSnapshot.getValue(slideModel.class);
                        model.setSlideUniqueKey(dataSnapshot.getKey());
                        arrayList.add(model);
                    }

                    updateSlideAdapters.notifyDataSetChanged();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //////////onpen gallery/////////////
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void updateClickLister(int position, String uniqueKey, String imageTitle, String imageUrl, EditText editText) {

        if (editText.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter any position", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("slideTitle", editText.getText().toString());
            databaseReference.child(course).child(college).child(auth.getUid()).child("Teacher Slider Image").child(uniqueKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(updateTeacherSlidersActivity.this, "Position Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    @Override
    public void deleteClickLister(int position, String uniqueKey, String editData) {
        arrayList.clear();
        databaseReference.child(course).child(college).child(auth.getUid()).child("Teacher Slider Image").child(uniqueKey).removeValue();

    }

    public void selectImage(int position, String uniqueKey, String editData) {
        imageUniqueKey = uniqueKey;
        openGallery();
    }

    /////////////////close work //////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE || resultCode == RESULT_OK) {
            IMAGE_URI = data.getData();
progressDialog.show();


            storageReference.child(course).child(auth.getUid() + new Date().getTime()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String image_uri = uri.toString();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("slideImage", image_uri);
                            databaseReference.child(course).child(college).child(auth.getUid()).child("Teacher Slider Image").child(imageUniqueKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();

                                        Toast.makeText(updateTeacherSlidersActivity.this, "Image Uploaded Success", Toast.LENGTH_SHORT).show();

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
}