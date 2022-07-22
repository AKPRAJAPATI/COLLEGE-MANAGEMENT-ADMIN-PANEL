package com.geral_area.collegemanagementadmin.splashActiviy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.geral_area.collegemanagementadmin.MainActivity;

import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.ActivitySplashActiviyBinding;

public class splashActiviy extends AppCompatActivity {
ActivitySplashActiviyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashActiviyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        getSupportActionBar().hide();
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.hand_writing);
        binding.akashkumartext.setTypeface(typeface);
        binding.textView3.setTypeface(typeface);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },3000);

    }
}