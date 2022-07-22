package com.geral_area.collegemanagementadmin.application;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.geral_area.collegemanagementadmin.Model.courseModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.addCourseAdapter;
import com.geral_area.collegemanagementadmin.databinding.ActivityAddCourseBinding;
import com.geral_area.collegemanagementadmin.local.courseInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class addCourse extends AppCompatActivity implements courseInterface {
    private ActivityAddCourseBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;
    private String course;
    private String college;

    private ArrayList<courseModel> arrayListCourse;
    private addCourseAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Course");
        progressDialog.setCancelable(false);

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        binding.addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.courseEditText.getText().toString().equals("")) {
                    Toast.makeText(addCourse.this, "Please enter any course", Toast.LENGTH_SHORT).show();
                    binding.courseEditText.requestFocus();
                } else {
                    progressDialog.show();
                    HashMap<String, Object> myHashmap = new HashMap<>();
                    myHashmap.put("course", binding.courseEditText.getText().toString());
                    databaseReference.child(course).child(college).child(auth.getUid()).child("Course List").push().setValue(myHashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                binding.courseEditText.setText("");
                                Toast.makeText(addCourse.this, "Course Added", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        binding.recyclerviewCourseList.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewCourseList.setHasFixedSize(true);
        arrayListCourse = new ArrayList<>();
        getOurCourse();
        adapter = new addCourseAdapter(getApplicationContext(), arrayListCourse, this);
        binding.recyclerviewCourseList.setAdapter(adapter);


    }

    private void getOurCourse() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("Course List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListCourse.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    courseModel myCourse = dataSnapshot.getValue(courseModel.class);
                    myCourse.setUniqueId(dataSnapshot.getKey());
                    arrayListCourse.add(myCourse);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void updateCourse(String uniqueKey, int position, EditText editText) {
         if (editText.getText().equals("")){
             Toast.makeText(this, "Enter any course", Toast.LENGTH_SHORT).show();
         }else{
             HashMap<String,Object> hashMap = new HashMap<>();
             hashMap.put("course",editText.getText().toString());

             databaseReference.child(course).child(college).child(auth.getUid()).child("Course List").child(uniqueKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()){
                         Toast.makeText(addCourse.this, "Update success", Toast.LENGTH_SHORT).show();
                     }
                 }
             });
         }
    }

    @Override
    public void deleteCourse(String uniqueKey, int position) {
        databaseReference.child(course).child(college).child(auth.getUid()).child("Course List").child(uniqueKey).removeValue();

    }
}