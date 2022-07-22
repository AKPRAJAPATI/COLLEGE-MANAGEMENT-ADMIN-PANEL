package com.geral_area.collegemanagementadmin.authenticationArea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geral_area.collegemanagementadmin.MainActivity;
import com.geral_area.collegemanagementadmin.Model.registerModel;
import com.geral_area.collegemanagementadmin.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    // and store name of courses
    private String[] courses = {"Select Semester", "BCA I", "BCA II", "BCA III", "BCA IV", "BCA V", "BCA VI"};
    private String[] colleges2 = {"Select College", "GCMT Aligarh", "Dharam Samaj Aligarh", "AMU Aligarh"};
    private String[] colleges = {
            "Select College"
            ,"GCMT Aligarh"
            ,"DS Degree College, Aligarh", "Shri Varshney College, Aligarh"
            ,"Aligarh College of Education, Aligarh"
            ,"Gyan Mahavidyalaya, Aligarh"
            ,"Institute of Information Management and Technology, Aligarh"
            ,"PM College of Education, Aligarh"
            ,"SSITM Aligarh - Shivdan Singh Institute of Technology and Management"
            ,"Amrita Singh Memorial Degree College, Aligarh"
            ,"Gramodhar Mahavidyalaya, Aligarh"
            ,"National P.G. College Lucknow - National Post Graduate College"
            ,"Raja Balwant Singh College, Agra"
    };
    private String selected_course;
    private String selected_college;

    /////////// FIREBASE /////////////////
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    /////////// LOCAL VARIABLE //////////
    private Uri IMAGE_URI;
    private String image_uri;
    private int GALLERY_REQUEST_CODE = 100;
    private ProgressDialog progressDialog, progressDialogRegister;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        progressDialogRegister = new ProgressDialog(this);
        progressDialogRegister.setCancelable(false);
        progressDialogRegister.setMessage("Register Account");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Saving Data");

        ///////////////GET INSTANCE OF OUR DATABASE///////////////
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        //This is work area for courses
        binding.coursesspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_course = binding.coursesspinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courses);
        binding.coursesspinner.setAdapter(ad);

        //This is work area for colleges
        binding.courseCollegespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_college = binding.courseCollegespinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter collegeAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, colleges);
        binding.courseCollegespinner.setAdapter(collegeAdapter);

///////////////////////////////////////////////MAIN WORK START HERE//////////////////////////////////////////////////
        binding.userProfileRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        binding.registersumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.userNameEdit.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                } else if (binding.userEmailEdit.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Email is empty", Toast.LENGTH_SHORT).show();
                } else if (!binding.userEmailEdit.getText().toString().contains("@gmail.com")) {
                    Toast.makeText(RegisterActivity.this, "Email is wrong", Toast.LENGTH_SHORT).show();
                } else if (binding.userPasswordEdit.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                } else if (binding.userPasswordEdit.getText().toString().length() < 8) {
                    Toast.makeText(RegisterActivity.this, "Password more than 8 character", Toast.LENGTH_SHORT).show();
                } else if (binding.userConfirmPasswordEdit.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Confirm password is empty", Toast.LENGTH_SHORT).show();
                } else if (!binding.userPasswordEdit.getText().toString().equals(binding.userConfirmPasswordEdit.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Password no matches", Toast.LENGTH_SHORT).show();
                } else if (selected_course.equals("Select Semester")) {
                    Toast.makeText(RegisterActivity.this, "Please select your course", Toast.LENGTH_SHORT).show();
                } else if (selected_college.equals("Select College")) {
                    Toast.makeText(RegisterActivity.this, "Please select your college", Toast.LENGTH_SHORT).show();
                } else if (IMAGE_URI == null) {
                    Toast.makeText(RegisterActivity.this, "Please select your Image", Toast.LENGTH_SHORT).show();
                    openGallery();
                } else {
                    progressDialogRegister.show();
                    auth.createUserWithEmailAndPassword(binding.userEmailEdit.getText().toString(), binding.userPasswordEdit.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialogRegister.dismiss();
                                progressDialog.show();
                                saveMyData();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialogRegister.dismiss();
                            Toast.makeText(RegisterActivity.this, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void saveMyData() {
        storageReference.child("profile").child(auth.getUid()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image_uri = uri.toString();
                        registerModel model = new registerModel
                                (
                                        image_uri,
                                        binding.userNameEdit.getText().toString(),
                                        binding.userEmailEdit.getText().toString(),
                                        binding.userPasswordEdit.getText().toString(),
                                        selected_college, selected_course, auth.getUid()
                                );

                        databaseReference.child(selected_course).child(selected_college).child(auth.getUid()).child("our info").setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ///////////// i will use shayerd Prefrence in our database
                                    sharedPreferences = getSharedPreferences("ourUserData", MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.putString("course_", selected_course);
                                    editor.putString("college_", selected_college);
                                    editor.apply();

//                                    //registerModel myModel = new registerModel(selected_college,selected_course,auth.getUid());
//                                    HashMap<String, Object> hashMap = new HashMap<>();
//                                    hashMap.put("course", selected_course);
//                                    hashMap.put("college", selected_college);
//
                                    Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Image uri not get for you", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Error is " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            binding.userProfileRegister.setImageURI(IMAGE_URI);
        }
    }



    /////////////////close work //////////////////

}