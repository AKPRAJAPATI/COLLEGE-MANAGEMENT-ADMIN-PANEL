package com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateNotice;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.geral_area.collegemanagementadmin.Model.noticeModel;
import com.geral_area.collegemanagementadmin.databinding.ActivityUpdateNoticeBinding;
import com.geral_area.collegemanagementadmin.databinding.ActivityUpdateNoticeListBinding;
import com.geral_area.collegemanagementadmin.delete_dir.adapters.noticeAdapters;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class updateNoticeListActivity extends AppCompatActivity {
    private ActivityUpdateNoticeListBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String course;
    private String college;
    private String selected_Subjects;

    private ArrayList<noticeModel> arrayList;
    private noticeAdapters adapters;

    private int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog2;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNoticeListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Notice");
        progressDialog.setCancelable(false);

        progressDialog2 = new ProgressDialog(this);
        progressDialog2.setCancelable(false);

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        binding.getNoticeRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.getNoticeRecyclerview.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        adapters = new noticeAdapters(getApplicationContext(),arrayList);
        getOurNotice();
        binding.getNoticeRecyclerview.setAdapter(adapters);

    }

    private void getOurNotice() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("Notice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    binding.getNoticeRecyclerview.setVisibility(View.VISIBLE);
                    binding.noDataFoundUpdateNotice.setVisibility(View.GONE);

                    arrayList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        noticeModel model = dataSnapshot.getValue(noticeModel.class);

                        model.setNoticeUnqueKey(dataSnapshot.getKey());

                        arrayList.add(model);

                    }
                    adapters.notifyDataSetChanged();
                }else{
                    binding.getNoticeRecyclerview.setVisibility(View.GONE);
                    binding.noDataFoundUpdateNotice.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}