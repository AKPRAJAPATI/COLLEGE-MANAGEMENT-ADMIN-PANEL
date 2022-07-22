package com.geral_area.collegemanagementadmin.delete_dir.activiteis.updatePdf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.geral_area.collegemanagementadmin.Model.pdfModel;
import com.geral_area.collegemanagementadmin.application.addPdfActivity;
import com.geral_area.collegemanagementadmin.databinding.ActivityPdfListBinding;
import com.geral_area.collegemanagementadmin.delete_dir.adapters.pdfAdapters;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class pdfListActivity extends AppCompatActivity {
    private ActivityPdfListBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String course;
    private String college;
    private  pdfModel modellingValue;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        binding.pdfRecyclerView.setLayoutManager(mLinearLayoutManager);


        binding.pdfRecyclerView.setHasFixedSize(true);
        ArrayList<pdfModel> arrayList = new ArrayList<>();
        pdfAdapters pdfAdapters = new pdfAdapters(getApplicationContext(), arrayList);

        databaseReference.child(course).child(college).child(auth.getUid()).child("pdf").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.pdfRecyclerView.setVisibility(View.VISIBLE);
                    binding.pdfNotFoundCardView.setVisibility(View.GONE);

                    arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                          modellingValue = dataSnapshot.getValue(pdfModel.class);
                        modellingValue.setUniqueKey(dataSnapshot.getKey());
                        arrayList.add(modellingValue);
                    }

                    pdfAdapters.notifyDataSetChanged();
                } else {
                    binding.pdfRecyclerView.setVisibility(View.GONE);
                    binding.pdfNotFoundCardView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.pdfRecyclerView.setAdapter(pdfAdapters);


    }

    public void addPdf(View view) {
        Intent intent = new Intent(getApplicationContext(), addPdfActivity.class);
        intent.putExtra("my_course", course);
        intent.putExtra("my_college", college);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}