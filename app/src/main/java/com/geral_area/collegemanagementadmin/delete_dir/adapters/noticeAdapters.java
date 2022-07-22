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

import com.geral_area.collegemanagementadmin.Model.noticeModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.NoticeItemBinding;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateNotice.updateNotice;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateTeacherPackage.updateTeacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class noticeAdapters extends RecyclerView.Adapter<noticeAdapters.slideViewHolder> {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private Context context;
    private ArrayList<noticeModel> arrayList;

    public noticeAdapters(Context context, ArrayList<noticeModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public slideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new slideViewHolder(LayoutInflater.from(context).inflate(R.layout.notice_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull slideViewHolder holder, int position) {
        noticeModel model = arrayList.get(position);
        Picasso.get().load(model.getNoticeImage()).into(holder.binding.noticeImageItem);
        holder.binding.noticeTextViewItem.setText(model.getNoticeImageText());
        //////////////marque work ////////////////////

        sharedPreferences = context.getSharedPreferences("ourUserData", MODE_PRIVATE);
        String collgename = sharedPreferences.getString("college_", "");
        String coursename = sharedPreferences.getString("course_", "");

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        holder.binding.updateNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, updateNotice.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.putExtra("uniqueKey",model.getNoticeUnqueKey());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.binding.updateDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(coursename).child(collgename).child(auth.getUid()).child("Notice").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()){
                       databaseReference.child(coursename).child(collgename).child(auth.getUid()).child("Notice").child(model.getNoticeUnqueKey()).removeValue();
                       Toast.makeText(context, "Notice Removed", Toast.LENGTH_SHORT).show();
                   }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class slideViewHolder extends RecyclerView.ViewHolder {
        private NoticeItemBinding binding;

        public slideViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = NoticeItemBinding.bind(itemView);
        }
    }
}
