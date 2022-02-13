package com.nhatnguyen.congthucgiadinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.CategoryModel;
import com.nhatnguyen.congthucgiadinh.ui.viewAll.ViewDetailCategoryActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.ViewHolderCate>{
    Context context;
    List<CategoryModel> categoryModelList;
    private  RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);
    public CategoryAdapter2(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderCate onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderCate(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category2, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderCate holder, int position) {
        CategoryModel s=categoryModelList.get(position);
        if (s==null){
            return;
        }
        if(s.getImage().isEmpty()){
            Glide.with(context).load(R.drawable.image).into(holder.cateImg);
        }
        else {
            Glide.with(context).load(s.getImage()).apply(requestOptions).placeholder(R.drawable.image).into(holder.cateImg);
        }

        holder.txtname.setText(s.getName());
       //setFadeAnimation(holder.itemView);
    }

    @Override
    public int getItemCount() {
            return categoryModelList.size();
    }
//    private void setFadeAnimation(View view) {
//        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(1500);
//        view.startAnimation(anim);
//    }
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
            CategoryModel categoryModel = categoryModelList.get(position);
            Intent intent=new Intent(context, ViewDetailCategoryActivity.class);
            intent.putExtra("Category", categoryModel.name);
            context.startActivity(intent);

        }
    }
}
