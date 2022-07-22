package com.geral_area.collegemanagementadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geral_area.collegemanagementadmin.Model.courseModel;
import com.geral_area.collegemanagementadmin.databinding.CourseItemsBinding;
import com.geral_area.collegemanagementadmin.local.courseInterface;

import java.util.ArrayList;

public class addCourseAdapter extends RecyclerView.Adapter<addCourseAdapter.courseViewHolder> {

    private Context context;
    private ArrayList<courseModel> arrayList;
    private courseInterface clicklistner;

    public addCourseAdapter(Context context, ArrayList<courseModel> arrayList, courseInterface clicklistner) {
        this.context = context;
        this.arrayList = arrayList;
        this.clicklistner = clicklistner;
    }

    @NonNull
    @Override
    public courseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new courseViewHolder(LayoutInflater.from(context).inflate(R.layout.course_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull courseViewHolder holder, int position) {
        courseModel model = arrayList.get(position);
        holder.binding.course.setText(model.getCourse());

        holder.binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicklistner.updateCourse(model.getUniqueId(),position,holder.binding.course);
            }
        });
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicklistner.deleteCourse(model.getUniqueId(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class courseViewHolder extends RecyclerView.ViewHolder {
        CourseItemsBinding binding;

        public courseViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CourseItemsBinding.bind(itemView);
        }
    }
}
