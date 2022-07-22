package com.geral_area.collegemanagementadmin.delete_dir.activiteis.updatePdf;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geral_area.collegemanagementadmin.Model.pdfModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.ActivityUpdatePdfBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class updatePdfActivity extends AppCompatActivity {
    ActivityUpdatePdfBinding binding;
    String pdfUrl, pdfName;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private int GALLERY_REQUEST_CODE = 1;
    private Uri selectedUri_PDF;
    private String pdf_uri;
    private String uniqueKey;
    private ProgressDialog progressDialog;
    private String course;
    private String college;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();



        pdfUrl = getIntent().getStringExtra("pdf_url");
        pdfName = getIntent().getStringExtra("pdf_name");
        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");
        uniqueKey = getIntent().getStringExtra("pdfUniquecKey");
        binding.pdfNameEdit.setText(pdfName);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        binding.selectPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a PDF "), 100);
            }
        });
        binding.addUploadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (!binding.pdfNameEdit.getText().toString().equals("")){
                   saveDataWithOurName();
               }else{
                   saveDataWithOurEnterName();
               }

            }
        });


    }

    private void saveDataWithOurName() {
        progressDialog.show();
        storageReference.child("pdf").child(auth.getUid() + " " + new Date().getTime()).putFile(selectedUri_PDF).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> pdf_Task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                pdf_Task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        pdf_uri = uri.toString();
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("pdf_url",pdf_uri);
                        hashMap.put("pdf_name",binding.pdfNameEdit.getText().toString());
                        databaseReference.child(course).child(college).child(auth.getUid()).child("pdf").child(uniqueKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(updatePdfActivity.this, "Pdf Uploaded Success", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading: " + (int) progress + "%");

            }
        });


    }

    private void saveDataWithOurEnterName() {
        progressDialog.show();
        storageReference.child("pdf").child(auth.getUid() + " " + new Date().getTime()).putFile(selectedUri_PDF).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> pdf_Task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                pdf_Task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        pdf_uri = uri.toString();
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("pdf_url",pdf_uri);
                        hashMap.put("pdf_name",binding.pdfNameTextView.getText().toString());
                        databaseReference.child(course).child(college).child(auth.getUid()).child("pdf").child(uniqueKey).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(updatePdfActivity.this, "Pdf Uploaded Success", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading: " + (int) progress + "%");

            }
        });


    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                selectedUri_PDF = data.getData();

                if (selectedUri_PDF.toString().startsWith("content://")) {
                    Cursor cursor = null;
                    cursor = updatePdfActivity.this.getContentResolver().query(selectedUri_PDF, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }

                } else if (selectedUri_PDF.toString().startsWith("file://")) {
                    pdfName = new File(selectedUri_PDF.toString()).getName();
                }
                ////////// THIS IS LOCAL ARGUMENTS ///////////
                binding.pdfNameTextView.setText(pdfName);
                binding.pdfNameEdit.setText("");
//                binding.addUploadPdfButton.setText("Upload Pdf");
//                binding.pdfPreview.setVisibility(View.VISIBLE);
            }
        }
    }

}