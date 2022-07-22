package com.geral_area.collegemanagementadmin.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geral_area.collegemanagementadmin.Model.noticeModel;
import com.geral_area.collegemanagementadmin.databinding.ActivityAddNoticeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class addNoticeActivity extends AppCompatActivity {
    private ActivityAddNoticeBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;
    private String course;
    private String college;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoticeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Notice");
        progressDialog.setCancelable(false);

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        if (IMAGE_URI == null) {
            binding.imageCard.setVisibility(View.GONE);
        }

        binding.chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        binding.addUploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.addNoticeText.getText().toString().isEmpty()) {
                    Toast.makeText(addNoticeActivity.this, "Please enter any text", Toast.LENGTH_SHORT).show();
                    binding.addNoticeText.requestFocus();
                    binding.addNoticeText.setError("Enter any image name");
                } else if (IMAGE_URI == null) {
                    Toast.makeText(addNoticeActivity.this, "Please choose any image", Toast.LENGTH_SHORT).show();
                    openGallery();
                } else {
                    progressDialog.show();
                    storageReference.child("Notice").child(auth.getUid() + " " + new Date().getTime()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_uri = uri.toString();
                                    noticeModel model = new noticeModel(binding.addNoticeText.getText().toString(), image_uri, auth.getUid());
                                    databaseReference.child(course).child(college).child(auth.getUid()).child("Notice").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                binding.addNoticeText.setText("");
                                                Toast.makeText(addNoticeActivity.this, "Notice Added", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE || resultCode == RESULT_OK) {
            IMAGE_URI = data.getData();
            binding.imageNotice.setImageURI(IMAGE_URI);
            binding.imageCard.setVisibility(View.VISIBLE);
        }
    }
    /////////////////close work //////////////////
}