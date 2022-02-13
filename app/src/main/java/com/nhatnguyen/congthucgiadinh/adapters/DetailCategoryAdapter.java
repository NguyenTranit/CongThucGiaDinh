package com.nhatnguyen.congthucgiadinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.ui.recipes.RecipesActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DetailCategoryAdapter extends RecyclerView.Adapter<DetailCategoryAdapter.ViewHolder> implements Filterable {

    Context context;
    List<RecipesModel> recipesModelList;
    List<RecipesModel> recipesModelListOld;
    String filterPattern = "";
    private RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);

    public DetailCategoryAdapter(Context context, List<RecipesModel> recipesModelList) {
        this.context = context;
        this.recipesModelList = recipesModelList;
        this.recipesModelListOld = recipesModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ryc_category_detail, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        RecipesModel s = recipesModelList.get(position);
        if (s == null) {
            return;
        }
        if (!filterPattern.equals("")) {
            String tmpCname = s.getName();
            int startPos = tmpCname.toLowerCase().indexOf(filterPattern.toLowerCase());
            int endPos = startPos + filterPattern.length();
            if (startPos != -1) {
                Spannable spannable = new SpannableString(tmpCname);
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.name.setText(spannable);
            } else {
                holder.name.setText(s.getName());
            }
        } else {
            holder.name.setText(s.getName());
        }
        //
        if (s.getImage().isEmpty()) {
            Glide.with(context).load(R.drawable.image).into(holder.img);
        } else {
            Glide.with(context).load(s.getImage()).apply(requestOptions).placeholder(R.drawable.image).into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipesActivity.class);
                intent.putExtra("recipes", s);
                context.startActivity(intent);
            }
        });
        setFadeAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        if (recipesModelList != null) {
            return recipesModelList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filterPattern = constraint.toString().toLowerCase().trim();
                if (TextUtils.isEmpty(constraint)) {
                    filterPattern = "";
                    recipesModelList.addAll(recipesModelListOld);
                } else {
                    List<RecipesModel> list = new ArrayList<>();
                    for (RecipesModel recipesModel : recipesModelListOld) {
                        if (recipesModel.getName().toLowerCase().contains(filterPattern.toLowerCase())) {
                            list.add(recipesModel);
                        }
                    }
                    recipesModelList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = recipesModelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                recipesModelList = (List<RecipesModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView img;
        TextView name;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageCate);
            name = itemView.findViewById(R.id.mealName);

        }
    }
}
