package com.geral_area.collegemanagementadmin.delete_dir.activiteis.updatePdf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.ActivityShowPdfBinding;

public class showPdfActivity extends AppCompatActivity {
private String pdfUrl;
private ActivityShowPdfBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityShowPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pdfUrl = getIntent().getStringExtra("pdfUrl");
        binding.pdfView.getSettings().setJavaScriptEnabled(true);
        binding.pdfView.getSettings().setBuiltInZoomControls(true);
        binding.pdfView.getSettings().setDisplayZoomControls(false);
        binding.pdfView.setWebChromeClient(new WebChromeClient());
        binding.pdfView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.pdfView.loadUrl("https://docs.google.com/gview?embedded=true&url=" +pdfUrl);
            }
        });
    }
}