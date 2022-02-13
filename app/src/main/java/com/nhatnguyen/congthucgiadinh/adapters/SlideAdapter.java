package com.nhatnguyen.congthucgiadinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.ui.recipes.RecipesActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder>{

    private Context context;
    private List<RecipesModel> recipesModelList;
    private ViewPager2 viewPager2;
    private RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);

    public SlideAdapter(Context context, List<RecipesModel> recipesModelList, ViewPager2 viewPager2) {
        this.context = context;
        this.recipesModelList = recipesModelList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @NotNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SlideViewHolder holder, int position) {
        RecipesModel s=recipesModelList.get(position);
        if (s==null){
            return;
        }
        if(s.getImage().isEmpty()){
            Glide.with(context).load(R.drawable.image).into(holder.imageView);
        }
        else {
            Glide.with(context).load(s.getImage()).apply(requestOptions).placeholder(R.drawable.image).error(R.drawable.image).into(holder.imageView);
        }
        holder.name.setText(s.getName());
        if(position== recipesModelList.size()-2){
            viewPager2.post(runnable);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipesActivity.class);
                intent.putExtra("recipes",s);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipesModelList!=null){
            return recipesModelList.size();
        }
        return 0;

    }

    class SlideViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;
        private TextView name;


        public SlideViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlide);
            name=itemView.findViewById(R.id.nameSlide);
        }
    }
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            recipesModelList.addAll(recipesModelList);
            notifyDataSetChanged();
        }
    };
}
