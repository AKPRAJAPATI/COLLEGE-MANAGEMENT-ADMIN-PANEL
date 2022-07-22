package com.geral_area.collegemanagementadmin.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.geral_area.collegemanagementadmin.Model.noticeModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.ActivityAddLocationBinding;
import com.geral_area.collegemanagementadmin.databinding.ActivityAddNoticeBinding;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

public class addLocation extends AppCompatActivity {
    private ActivityAddLocationBinding binding;
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
        binding = ActivityAddLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Location");
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
        binding.addUploadLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.addLocationLink.getText().toString().isEmpty()) {
                    Toast.makeText(addLocation.this, "Please enter any text", Toast.LENGTH_SHORT).show();
                    binding.addLocationLink.requestFocus();
                } else if (IMAGE_URI == null) {
                    Toast.makeText(addLocation.this, "Please choose any image", Toast.LENGTH_SHORT).show();
                    openGallery();
                } else {
                    progressDialog.show();
                    storageReference.child("location").child(auth.getUid() + " " + new Date().getTime()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_uri = uri.toString();
                                    HashMap<String,Object> model = new HashMap<>();
                                    model.put("link",binding.addLocationLink.getEditableText().toString());
                                    model.put("mapImage",image_uri);
//                                    databaseReference.child(course).child(college).child(auth.getUid()).child("map").child(auth.getUid()).updateChildren(model)
                                    databaseReference.child(course).child(college).child(auth.getUid()).child("map").updateChildren(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                binding.addLocationLink.setText("");
                                                Toast.makeText(addLocation.this, "Location Added", Toast.LENGTH_SHORT).show();
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


        getOurData();

    }

    private void getOurData() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("map").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String mapLink = snapshot.child("link").getValue(String.class);
                    String map = snapshot.child("mapImage").getValue(String.class);

                    binding.addLocationLink.setText(mapLink);
                    Picasso.get().load(map).into(binding.imageNotice);
                    binding.imageCard.setVisibility(View.VISIBLE);

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