package com.geral_area.collegemanagementadmin.delete_dir.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geral_area.collegemanagementadmin.Model.slideModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.SlideItemBinding;
import com.geral_area.collegemanagementadmin.databinding.TeacherSlideItemBinding;
import com.geral_area.collegemanagementadmin.local.slideOnclickInterface;
import com.geral_area.collegemanagementadmin.local.updateTeacherSliders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class update_teacher_SlideAdapters extends RecyclerView.Adapter<update_teacher_SlideAdapters.slideViewHolder> {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private Context context;
    private ArrayList<slideModel> arrayList;
    private updateTeacherSliders clickListener;

    public update_teacher_SlideAdapters(Context context, ArrayList<slideModel> arrayList, updateTeacherSliders clickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public slideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new slideViewHolder(LayoutInflater.from(context).inflate(R.layout.teacher_slide_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull slideViewHolder holder, int position) {
         slideModel model = arrayList.get(position);

         holder.binding.teacherTitleEditText.setText(model.getSlideTitle());
        Picasso.get().load(model.getSlideImage()).into(holder.binding.imageSlidersItem);

        sharedPreferences = context.getSharedPreferences("ourUserData", MODE_PRIVATE);
        String collgename = sharedPreferences.getString("college_", "");
        String coursename = sharedPreferences.getString("course_", "");

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

//        holder.binding.updateSlideImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(context, updateNotice.class);
////                intent.putExtra("my_course", coursename);
////                intent.putExtra("my_college", collgename);
////                intent.putExtra("uniqueKey",model.getNoticeUnqueKey());
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                context.startActivity(intent);
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                ((Activity) context).startActivityForResult(intent, 100);
//            }
//        });
//
//        holder.binding.deleteSlideImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                databaseReference.child(coursename).child(collgename).child(auth.getUid()).child("Slider Image").child(model.getSlideUniqueKey()).removeValue();
//            }
//        });

        holder.binding.deleteSlideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.deleteClickLister(position,model.getSlideUniqueKey(),model.getSlideTitle());
            }
        });
        holder.binding.updateSlideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.updateClickLister(position,model.getSlideUniqueKey(),model.getSlideTitle(),model.getSlideImage() , holder.binding.teacherTitleEditText);
            }
        });
        holder.binding.selectSlideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.selectImage(position,model.getSlideUniqueKey(),model.getSlideTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class slideViewHolder extends RecyclerView.ViewHolder {
        private TeacherSlideItemBinding binding;

        public slideViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TeacherSlideItemBinding.bind(itemView);
        }
    }
    //////////onpen gallery/////////////

    /////////////////close work //////////////////
}
