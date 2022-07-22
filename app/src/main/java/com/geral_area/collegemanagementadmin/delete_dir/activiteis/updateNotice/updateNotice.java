package com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateNotice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geral_area.collegemanagementadmin.Model.teacherModel;
import com.geral_area.collegemanagementadmin.databinding.ActivityUpdateNoticeBinding;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateTeacherPackage.updateTeacher;
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
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

public class updateNotice extends AppCompatActivity {
    private ActivityUpdateNoticeBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String course;
    private String college;
    private String selected_Subjects;
    private String uniqueKey;

    private String noticeImage;
    private String noticeDescription;

    private int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Teacher");
        progressDialog.setCancelable(false);

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setCancelable(false);


        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");
        uniqueKey = getIntent().getStringExtra("uniqueKey");
        getOurValueFromDatabase();

        binding.updateChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        binding.updateNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             if (binding.noticeEditText.getText().toString().equals("")){
                 Toast.makeText(updateNotice.this, "please enter any message", Toast.LENGTH_SHORT).show();
             }else{
                 HashMap<String,Object> hashMap = new HashMap<>();
                 hashMap.put("noticeImageText",binding.noticeEditText.getText().toString());
                 databaseReference.child(course).child(college).child(auth.getUid()).child("Notice").child(uniqueKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()){
                             Toast.makeText(updateNotice.this, "Data Update Success", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
             }
            }
        });



    }

    private void getOurValueFromDatabase() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("Notice").child(uniqueKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String myNoticeImage = snapshot.child("noticeImage").getValue(String.class);
                    String myNoticeDescription = snapshot.child("noticeImageText").getValue(String.class);

                    Picasso.get().load(myNoticeImage).into(binding.noticeImageItem);
                    binding.noticeEditText.setText(myNoticeDescription);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void saveDataToDatabase() {
//        storageReference.child("Notice").child(auth.getUid() + " " + new Date().getTime()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
//                IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        image_uri = uri.toString();
//
//
//
//                        databaseReference.child(course).child(college).child(auth.getUid()).child("Teachers").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//
//                                    sharedPreferences = getSharedPreferences("teacher_subjects", MODE_PRIVATE);
//                                    editor = sharedPreferences.edit();
//                                    editor.putString("teacherSub", selected_Subjects);
//                                    editor.apply();
//
//                                    HashMap<String, Object> hashMap = new HashMap<>();
//                                    hashMap.put("subjects", selected_Subjects);
//                                    databaseReference.child("CLIP DATA").child(auth.getUid()).child("subjects").push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                progressDialog.dismiss();
//
//                                                Toast.makeText(updateTeacher.this, "Teacher Added", Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//                                }
//                            }
//                        });
//                    }
//                });
//            }
//        });
//    }

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
            binding.noticeImageItem.setImageURI(IMAGE_URI);

            progressDialog2.show();
            storageReference.child("Notice").child(auth.getUid() + " " + new Date().getTime()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageLink = uri.toString();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("noticeImage",imageLink);

                            databaseReference.child(course).child(college).child(auth.getUid()).child("Notice").child(uniqueKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog2.dismiss();
                                        Toast.makeText(updateNotice.this, "Image Save Success", Toast.LENGTH_SHORT).show();
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
                    progressDialog2.setMessage("Saving Image " + (int) progress + "%");
                }
            });
        }
    }
    /////////////////close work //////////////////

}