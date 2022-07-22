package com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateImages;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.geral_area.collegemanagementadmin.Model.ImageModel;
import com.geral_area.collegemanagementadmin.databinding.ActivityUpdateImageListBinding;
import com.geral_area.collegemanagementadmin.delete_dir.adapters.updateImageAdapters.augustAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class updateImageListActivity extends AppCompatActivity {
    private ActivityUpdateImageListBinding binding;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String course;
    private String college;

    private augustAdapter augustAdapteR, januaryAdapteR,otherAdapteR;
    private ArrayList<ImageModel> arrayListAugust, arrayListJanuary,arrayListOther;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateImageListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        ///////////////////////////15 AUGUST//////////////////////////////////////////
        binding.recyclerview15August.setLayoutManager(new GridLayoutManager(this,3));
        binding.recyclerview15August.setHasFixedSize(true);
        arrayListAugust = new ArrayList<>();
        databaseReference.child(course).child(college).child(auth.getUid()).child("Images").child("15 August").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.recyclerview15August.setVisibility(View.VISIBLE);
                    binding.noDataFound15august.setVisibility(View.GONE);

                    arrayListAugust.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ImageModel model = dataSnapshot.getValue(ImageModel.class);
                        model.setUniqueId(dataSnapshot.getKey());
                        arrayListAugust.add(model);

                    }
                    augustAdapteR.notifyDataSetChanged();
                }else{
                    binding.recyclerview15August.setVisibility(View.GONE);
                    binding.noDataFound15august.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        augustAdapteR = new augustAdapter(getApplicationContext(), arrayListAugust);
        binding.recyclerview15August.setAdapter(augustAdapteR);
        /////////////////////////////////////////////////////////////////////////////

        /////////////////////////26 JANUARY//////////////////////////////////////////
        binding.recyclerview26January.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview26January.setHasFixedSize(true);
        arrayListJanuary = new ArrayList<>();
        databaseReference.child(course).child(college).child(auth.getUid()).child("Images").child("26 January").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.recyclerview26January.setVisibility(View.VISIBLE);
                    binding.noDataFound26january.setVisibility(View.GONE);

                    arrayListJanuary.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ImageModel model = dataSnapshot.getValue(ImageModel.class);
                        model.setUniqueId(dataSnapshot.getKey());
                        arrayListJanuary.add(model);
                    }
                    januaryAdapteR.notifyDataSetChanged();
                }else{
                    binding.recyclerview26January.setVisibility(View.GONE);
                    binding.noDataFound26january.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        januaryAdapteR = new augustAdapter(getApplicationContext(), arrayListJanuary);
        binding.recyclerview26January.setAdapter(januaryAdapteR);
        ///////////////////////////////////////////////////////////////////////////////

 /////////////////////////26 JANUARY//////////////////////////////////////////
        binding.recyclerviewOther.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewOther.setHasFixedSize(true);
        arrayListOther = new ArrayList<>();
        databaseReference.child(course).child(college).child(auth.getUid()).child("Images").child("Other").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    binding.recyclerviewOther.setVisibility(View.VISIBLE);
                    binding.noDataFoundOther.setVisibility(View.GONE);


                    arrayListOther.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ImageModel model = dataSnapshot.getValue(ImageModel.class);
                        arrayListOther.add(model);

                    }
                    otherAdapteR.notifyDataSetChanged();
                }else{
                    binding.recyclerviewOther.setVisibility(View.GONE);
                    binding.noDataFoundOther.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        otherAdapteR = new augustAdapter(getApplicationContext(), arrayListOther);
        binding.recyclerviewOther.setAdapter(otherAdapteR);
        ///////////////////////////////////////////////////////////////////////////////


    }
}