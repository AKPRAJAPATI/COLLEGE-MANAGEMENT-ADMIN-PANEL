package com.geral_area.collegemanagementadmin.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.geral_area.collegemanagementadmin.Model.contectModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.ActivityAddContectInfoBinding;
import com.geral_area.collegemanagementadmin.databinding.ActivityAddCourseBinding;
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

public class add_contectInfoActivity extends AppCompatActivity {
    private ActivityAddContectInfoBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;
    private String course;
    private String college;
    boolean show = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddContectInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Contect");
        progressDialog.setCancelable(false);

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");

        binding.addContectInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.fullAddressEdit.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter address", Toast.LENGTH_SHORT).show();
                    binding.fullAddressEdit.requestFocus();
                }else if (binding.state.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter state", Toast.LENGTH_SHORT).show();
                    binding.state.requestFocus();
                }else if (binding.pincode.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter pincode", Toast.LENGTH_SHORT).show();
                    binding.pincode.requestFocus();
                }else if (binding.gmail.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter Gmail", Toast.LENGTH_SHORT).show();
                    binding.gmail.requestFocus();
                }else if (!binding.gmail.getText().toString().contains("@gmail.com")){
                    Toast.makeText(add_contectInfoActivity.this, "Gmail is wrong", Toast.LENGTH_SHORT).show();
                    binding.gmail.requestFocus();
                }else if (binding.phone.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                    binding.phone.requestFocus();
                }else{
                 progressDialog.show();

                 saveData(binding.fullAddressEdit.getText().toString(),binding.state.getText().toString(),binding.pincode.getText().toString(),binding.gmail.getText().toString(),binding.phone.getText().toString(), show);

                }
            }
        });
        binding.updateContectInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.fullAddressEdit.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter address", Toast.LENGTH_SHORT).show();
                    binding.fullAddressEdit.requestFocus();
                }else if (binding.state.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter state", Toast.LENGTH_SHORT).show();
                    binding.state.requestFocus();
                }else if (binding.pincode.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter pincode", Toast.LENGTH_SHORT).show();
                    binding.pincode.requestFocus();
                }else if (binding.gmail.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter Gmail", Toast.LENGTH_SHORT).show();
                    binding.gmail.requestFocus();
                }else if (binding.phone.getText().toString().equals("")){
                    Toast.makeText(add_contectInfoActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                    binding.phone.requestFocus();
                }else{
                 progressDialog.show();

                 saveData(binding.fullAddressEdit.getText().toString(),binding.state.getText().toString(),binding.pincode.getText().toString(),binding.gmail.getText().toString(),binding.phone.getText().toString(), show);

                }
            }
        });
       getOurData();
    }

    private void getOurData() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("Contect Info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    boolean bool = snapshot.child("bool").getValue(boolean.class);
                    if (bool == true){
                        binding.addContectInformation.setVisibility(View.GONE);
                        binding.updateContectInformation.setVisibility(View.VISIBLE);
                    }else{
                        binding.addContectInformation.setVisibility(View.VISIBLE);
                        binding.updateContectInformation.setVisibility(View.GONE);
                    }
                    String  address = snapshot.child("address").getValue(String.class);
                    String state = snapshot.child("state").getValue(String.class);
                    String pin = snapshot.child("pincode").getValue(String.class);
                    String gmail = snapshot.child("gmail").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);

                   binding.fullAddressEdit.setText(address);
                   binding.pincode.setText(pin);
                   binding.gmail.setText(gmail);
                   binding.phone.setText(phone);
                   binding.state.setText(state);
                }

//              for (DataSnapshot dataSnapshot : snapshot.getChildren())
//              {
//                  contectModel model = dataSnapshot.getValue(contectModel.class);
//                  binding.gmail.setText(model.getGmail());
//                  binding.phone.setText(model.getPhone());
//                  binding.fullAddressEdit.setText(model.getAddress());
//                  binding.state.setText(model.getState());
//                  binding.pincode.setText(model.getPincode());
//              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveData(String address, String state, String pincode, String gmail, String phone ,boolean show) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("authId",auth.getUid());
        hashMap.put("address",address);
        hashMap.put("state",state);
        hashMap.put("pincode",pincode);
        hashMap.put("gmail",gmail);
        hashMap.put("phone",phone);
        hashMap.put("bool",show);

        databaseReference.child(course).child(college).child(auth.getUid()).child("Contect Info").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();

                    binding.addContectInformation.setVisibility(View.GONE);
                    binding.updateContectInformation.setVisibility(View.VISIBLE);
                    Toast.makeText(add_contectInfoActivity.this, "Contect Added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}