package com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateTeacherPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.geral_area.collegemanagementadmin.MainActivity;
import com.geral_area.collegemanagementadmin.Model.teacherModel;

import com.geral_area.collegemanagementadmin.application.addTeacherActivity;
import com.geral_area.collegemanagementadmin.databinding.ActivityUpdateTeacherBinding;
import com.geral_area.collegemanagementadmin.databinding.ActivityUpdateTeacherListBinding;
import com.geral_area.collegemanagementadmin.delete_dir.adapters.teacherAdapters;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class updateTeacherListActivity extends AppCompatActivity {
    private ActivityUpdateTeacherListBinding binding;
    private ArrayList<teacherModel> arrayList;
    private teacherAdapters adapters;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String course;
    private String college;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateTeacherListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        binding.myRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.myRecyclerview.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        databaseReference.child(course).child(college).child(auth.getUid()).child("Teachers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   binding.myRecyclerview.setVisibility(View.VISIBLE);
                   binding.teacherNotFound.setVisibility(View.GONE);

                   arrayList.clear();
                   for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                       teacherModel model = dataSnapshot.getValue(teacherModel.class);

                       //this is line to get perticular item get key in recyclerview in android studio in java
                       model.setTeacher_uniqueKey(dataSnapshot.getKey());

                       arrayList.add(model);
                   }
                   adapters.notifyDataSetChanged();
               }else{
                   binding.myRecyclerview.setVisibility(View.GONE);
                   binding.teacherNotFound.setVisibility(View.VISIBLE);

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapters = new teacherAdapters(getApplicationContext(), arrayList);
        binding.myRecyclerview.setAdapter(adapters);

    }

    public void goto_AddTeacherActivity(View view) {
        startActivity(new Intent(getApplicationContext(), addTeacherActivity.class));
    }
}