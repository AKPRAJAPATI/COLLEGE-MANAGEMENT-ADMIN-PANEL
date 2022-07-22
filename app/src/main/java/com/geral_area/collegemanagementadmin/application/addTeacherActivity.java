package com.geral_area.collegemanagementadmin.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geral_area.collegemanagementadmin.Model.teacherModel;
import com.geral_area.collegemanagementadmin.databinding.ActivityAddTeacherBinding;
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
import java.util.HashMap;

public class addTeacherActivity extends AppCompatActivity {
    private ActivityAddTeacherBinding binding;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String course;
    private String college;
    private String[] subjects = {"Select Subjects", "computer fundamental", "C Language", "C++", "DBMS", "java", "php"};
    private String selected_Subjects;

    private int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTeacherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Teacher");
        progressDialog.setCancelable(false);

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        ///////////////////spinner work ////////////////////////////
        binding.teacherSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_Subjects = binding.teacherSubjects.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);
        binding.teacherSubjects.setAdapter(adapter);
        ///////////////////spinner work ////////////////////////////

        //////////////////////// MAIN WORK START HERE//////////////////////////////////
        binding.teacherProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        ///////////////////////TEXT CHANGE LISTNER //////////////
        binding.teacherNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    binding.userNameTextView.setText(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //////////////////////////////////////////////////////////////////

        binding.registersumbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.teacherNameEdit.getText().toString().isEmpty()) {
                    Toast.makeText(addTeacherActivity.this, "Name is empty", Toast.LENGTH_SHORT).show();
                } else if (binding.teacherPhone.getText().toString().isEmpty()) {
                    Toast.makeText(addTeacherActivity.this, "Phone is empty", Toast.LENGTH_SHORT).show();
                } else if (binding.teacherPhone.getText().toString().length() < 10) {
                    Toast.makeText(addTeacherActivity.this, "Number more than 10 character", Toast.LENGTH_SHORT).show();
                } else if (binding.teacherPost.getText().toString().isEmpty()) {
                    Toast.makeText(addTeacherActivity.this, "Post is empty", Toast.LENGTH_SHORT).show();
                } else if (selected_Subjects.equals("Select Subjects")) {
                    Toast.makeText(addTeacherActivity.this, "Select course", Toast.LENGTH_SHORT).show();
                } else if (IMAGE_URI == null) {
                    Toast.makeText(addTeacherActivity.this, "Please select Image", Toast.LENGTH_SHORT).show();
                    openGallery();
                } else {
                    progressDialog.show();
                    saveDataToDatabase();
                }
            }
        });
    }

    private void saveDataToDatabase() {
        storageReference.child("Teachers").child(auth.getUid() +" "+new Date().getTime()).putFile(IMAGE_URI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> IMAGE_TASK = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                IMAGE_TASK.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image_uri = uri.toString();
                        teacherModel model = new teacherModel(image_uri, binding.teacherNameEdit.getText().toString(), binding.teacherPhone.getText().toString(), binding.teacherPost.getText().toString(), selected_Subjects, auth.getUid());
                        databaseReference.child(course).child(college).child(auth.getUid()).child("Teachers").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    sharedPreferences = getSharedPreferences("teacher_subjects",MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.putString("teacherSub",selected_Subjects);
                                    editor.apply();

                                    progressDialog.dismiss();
                                    Toast.makeText(addTeacherActivity.this, "Teacher Added", Toast.LENGTH_SHORT).show();


                                }
                            }
                        });
                    }
                });
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
            binding.teacherProfile.setImageURI(IMAGE_URI);
            binding.selectImageText.setVisibility(View.GONE);
        }
    }
    /////////////////close work //////////////////
}