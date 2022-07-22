package com.geral_area.collegemanagementadmin.share;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.models.SlideModel;
import com.geral_area.collegemanagementadmin.databinding.ActivityShareKeyBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class shareKeyActivity extends AppCompatActivity {
    private ActivityShareKeyBinding binding;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private final int GALLERY_REQUEST_CODE = 1;
    private Uri IMAGE_URI;
    private String image_uri;
    private ProgressDialog progressDialog;
    private String course;
    private String college;
    private List<SlideModel> arrayList;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShareKeyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.shareKeyTextView.setSelected(true);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Notice");
        progressDialog.setCancelable(false);

        course = getIntent().getStringExtra("my_course");
        college = getIntent().getStringExtra("my_college");
        getUserInformation();

        binding.copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",binding.shareKeyTextView.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(shareKeyActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message =
                        "College Management App ==> \n\n Admin Auth id is "+binding.shareKeyTextView.getText().toString()+"\n\nThis is id is most importent  for you \n Do' not share anyone \n Only share this id trusted person \n\n Key is "+auth.getUid()+" \n\nThank You";


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        });
    }

    private void getUserInformation() {
        databaseReference.child(course).child(college).child(auth.getUid()).child("our info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userprofille = snapshot.child("profile_image").getValue(String.class);
                    binding.userNameText.setText(snapshot.child("name").getValue(String.class));
                    binding.shareKeyTextView.setText(snapshot.child("auth_id").getValue(String.class));
                    Picasso.get().load(userprofille).into(binding.userProfileRegister);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}