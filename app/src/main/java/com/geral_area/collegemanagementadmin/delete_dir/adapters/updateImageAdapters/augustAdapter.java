package com.geral_area.collegemanagementadmin.delete_dir.adapters.updateImageAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geral_area.collegemanagementadmin.Model.ImageModel;
import com.geral_area.collegemanagementadmin.R;
import com.geral_area.collegemanagementadmin.databinding.ImageItemBinding;
import com.geral_area.collegemanagementadmin.databinding.SingleImageBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class augustAdapter extends RecyclerView.Adapter<augustAdapter.augustViewHolder> {
    private Context context;
    private ArrayList<ImageModel> arrayList;

    public augustAdapter(Context context, ArrayList<ImageModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public augustViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new augustViewHolder(LayoutInflater.from(context).inflate(R.layout.single_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull augustViewHolder holder, int position) {
        ImageModel model = arrayList.get(position);
        Picasso.get().load(model.getImage_url()).into(holder.binding.myInnerImgae);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class augustViewHolder extends RecyclerView.ViewHolder {
        private SingleImageBinding binding;

        public augustViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleImageBinding.bind(itemView);


        }
    }
}
