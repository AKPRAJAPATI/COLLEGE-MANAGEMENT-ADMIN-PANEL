package com.geral_area.collegemanagementadmin.delete_dir.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geral_area.collegemanagementadmin.Model.teacherModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.TeacherItemBinding;
import com.geral_area.collegemanagementadmin.delete_dir.activiteis.updateTeacherPackage.updateTeacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class teacherAdapters extends RecyclerView.Adapter<teacherAdapters.slideViewHolder> {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private Context context;
    private ArrayList<teacherModel> arrayList;

    public teacherAdapters(Context context, ArrayList<teacherModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public slideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new slideViewHolder(LayoutInflater.from(context).inflate(R.layout.teacher_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull slideViewHolder holder, int position) {
        teacherModel model = arrayList.get(position);
        Picasso.get().load(model.getTeacherProfile()).into(holder.binding.updateTeacherImage);
        holder.binding.updateTeacherNameTextView.setText(model.getTeacherName());
        holder.binding.updateTeacherNameTextView2.setText(model.getTeacherName());
        holder.binding.updateTeacherPhoneNumber.setText(model.getTeacherPhone());
        holder.binding.updateTeacherPost.setText(model.getTeacherPost());
        holder.binding.updateTeacherSubjects.setText(model.getTeacherSubjects());

        //////////////marque work ////////////////////
        holder.binding.updateTeacherNameTextView.setSelected(true);
        holder.binding.updateTeacherNameTextView.setSelected(true);
        holder.binding.updateTeacherPhoneNumber.setSelected(true);
        holder.binding.updateTeacherPost.setSelected(true);
        holder.binding.updateTeacherSubjects.setSelected(true);

        sharedPreferences = context.getSharedPreferences("ourUserData", MODE_PRIVATE);
        String collgename = sharedPreferences.getString("college_", "");
        String coursename = sharedPreferences.getString("course_", "");

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        holder.binding.updateTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, updateTeacher.class);
                intent.putExtra("my_course", coursename);
                intent.putExtra("my_college", collgename);
                intent.putExtra("uniqueKey",model.getTeacher_uniqueKey());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.binding.deleteTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(coursename).child(collgename).child(auth.getUid()).child("Teachers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()){
                       databaseReference.child(coursename).child(collgename).child(auth.getUid()).child("Teachers").child(model.getTeacher_uniqueKey()).removeValue();
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
        private TeacherItemBinding binding;

        public slideViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TeacherItemBinding.bind(itemView);
        }
    }
}
