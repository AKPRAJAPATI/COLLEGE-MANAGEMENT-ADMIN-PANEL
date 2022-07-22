package com.geral_area.collegemanagementadmin.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.ActivityAddCourseBinding;
import com.geral_area.collegemanagementadmin.databinding.ActivityCollegeAboutBinding;
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

import java.util.HashMap;

public class college_About_Activity extends AppCompatActivity {
    private ActivityCollegeAboutBinding binding;
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
        binding = ActivityCollegeAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Storing About Info");
        progressDialog.setCancelable(false);

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        binding.sumbitAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.collegeAboutEditText.getText().toString().equals("")){
                    Toast.makeText(college_About_Activity.this, "Please enter eny value", Toast.LENGTH_SHORT).show();
                    binding.collegeAboutEditText.requestFocus();
                    binding.collegeAboutEditText.setHint("Enter About your college in this area....");
                }else{
                    progressDialog.show();
                    saveAbout(binding.collegeAboutEditText.getText().toString());
                }
            }
        });

        getAbout();

    }

    private void getAbout() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("College About").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
           if (snapshot.exists())
           {
               String myabout = snapshot.child("about").getValue(String.class);
               binding.collegeAboutEditText.setText(myabout);
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveAbout(String about) {
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("about",about);

        databaseReference.child(course).child(college).child(auth.getUid()).child("College About").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    binding.collegeAboutEditText.setText("");
                    binding.collegeAboutEditText.setHint("Type Here");
                    Toast.makeText(college_About_Activity.this, "Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}