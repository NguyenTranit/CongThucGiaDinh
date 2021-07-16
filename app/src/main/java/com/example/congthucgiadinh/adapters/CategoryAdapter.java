package com.example.congthucgiadinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.congthucgiadinh.R;
import com.example.congthucgiadinh.model.Category;
import com.example.congthucgiadinh.ui.ViewAll.ViewAllActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolderCate> {
    Context context;
    List<Category> categoryList;


    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderCate onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderCate(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderCate holder, int position) {

        //Glide.with(context).load(categoryList.get(position).getImage()).into(holder.cateImg);
        Picasso.with(context).load(categoryList.get(position).getImage()).into(holder.cateImg);
        holder.txtname.setText(categoryList.get(position).getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(context, ViewAllActivity.class);
//                intent.putExtra("Category",categoryList.get(position).name);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolderCate extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView cateImg;
        TextView txtname;

        public ViewHolderCate(@NonNull @NotNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.nameCate);
            cateImg = itemView.findViewById(R.id.imageViewCate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            Category category=categoryList.get(position);
            Intent intent=new Intent(context, ViewAllActivity.class);
            intent.putExtra("Category",category.name);
            context.startActivity(intent);

        }
    }
}
