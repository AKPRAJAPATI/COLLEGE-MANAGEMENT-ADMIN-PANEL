package com.geral_area.collegemanagementadmin.delete_dir.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geral_area.collegemanagementadmin.Model.pdfModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.PdfItmesBinding;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updatePdf.showPdfActivity;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updatePdf.updatePdfActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pdfAdapters extends RecyclerView.Adapter<pdfAdapters.slideViewHolder> {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private Context context;
    private ArrayList<pdfModel> arrayList;

private int indexPosition = 0;

    public pdfAdapters(Context context, ArrayList<pdfModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public slideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new slideViewHolder(LayoutInflater.from(context).inflate(R.layout.pdf_itmes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull slideViewHolder holder, int position) {
        pdfModel model = arrayList.get(position);
        String getPdfpdfUrl;

        holder.binding.pdfname.setText(model.getPdf_name());

        //////////////marque work ////////////////////
        sharedPreferences = context.getSharedPreferences("ourUserData", MODE_PRIVATE);
        String collgename = sharedPreferences.getString("college_", "");
        String coursename = sharedPreferences.getString("course_", "");

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        holder.binding.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(coursename).child(collgename).child(auth.getUid()).child("pdf").child(model.getUniqueKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String pdfurl = snapshot.child("pdf_url").getValue(String.class);
                            Intent intent = new Intent(context, showPdfActivity.class);
//                            intent.putExtra("my_course", coursename);
//                            intent.putExtra("my_college", collgename);
//                            intent.putExtra("uniqueKey",model.getNoticeUnqueKey());
                            intent.putExtra("pdfUrl", pdfurl);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        holder.binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // String  pdfurl= snapshot.child("pdf_url").getValue(String.class);
                databaseReference.child(coursename).child(collgename).child(auth.getUid()).child("pdf").child(model.getUniqueKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String pdfUrl = snapshot.child("pdf_url").getValue(String.class);
                            String pdfName = snapshot.child("pdf_name").getValue(String.class);

                            Intent intent = new Intent(context, updatePdfActivity.class);
                            intent.putExtra("my_course", coursename);
                            intent.putExtra("my_college", collgename);
                            intent.putExtra("pdf_url", pdfUrl);
                            intent.putExtra("pdf_name", pdfName);
                            intent.putExtra("pdfUniquecKey", model.getUniqueKey());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(coursename).child(collgename).child(auth.getUid()).child("pdf").child(model.getUniqueKey()).removeValue();

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class slideViewHolder extends RecyclerView.ViewHolder {
        private PdfItmesBinding binding;

        public slideViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PdfItmesBinding.bind(itemView);
        }
    }
}
