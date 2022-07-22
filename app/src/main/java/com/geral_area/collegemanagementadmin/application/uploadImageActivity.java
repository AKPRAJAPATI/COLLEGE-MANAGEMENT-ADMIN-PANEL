package com.geral_area.collegemanagementadmin.application;

import androidx.annotation.NonNull;
import androidx.annotation.NonUiContext;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.geral_area.collegemanagementadmin.Model.ImageModel;
import com.geral_area.collegemanagementadmin.Model.noticeModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.ActivityUploadImageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class uploadImageActivity extends AppCompatActivity {

    private ActivityUploadImageBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;
    private String course;
    private String college;
    private String[] image_categories = {"Select Category","15 August","26 January","Other"};
    private String selectedImageCate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        binding.imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedImageCate = binding.imageCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,image_categories);
        binding.imageCategory.setAdapter(adapter);

        binding.chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
       binding.addUploadImg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (IMAGE_URI == null){
                   Toast.makeText(uploadImageActivity.this, "Please choose image", Toast.LENGTH_SHORT).show();
                    openGallery();;
               }else if (selectedImageCate.equals("Select Category")){
                   Toast.makeText(uploadImageActivity.this, "Select Image Category", Toast.LENGTH_SHORT).show();
               }else{
                   progressDialog.show();
                   storageReference.child("Images").child(auth.getUid() + " " +new Date().getTime()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                           IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   image_uri = uri.toString();
                                   ImageModel model = new ImageModel(image_uri,auth.getUid());
                                   databaseReference.child(course).child(college).child(auth.getUid()).child("Images").child(selectedImageCate).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()){
                                               progressDialog.dismiss();
                                               Toast.makeText(uploadImageActivity.this, "Image Added", Toast.LENGTH_SHORT).show();
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
                           progressDialog.setMessage("Saving Image " + (int) progress + "%");
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
        if (requestCode == GALLERY_REQUEST_CODE || resultCode == RESULT_OK || data != null) {
            IMAGE_URI = data.getData();
            binding.imageImage.setImageURI(IMAGE_URI);
        }
    }
    /////////////////close work //////////////////
}