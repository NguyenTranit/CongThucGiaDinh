package com.nhatnguyen.congthucgiadinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.ui.recipes.RecipesActivity;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolderCate> implements Filterable {
    Context context;
    List<RecipesModel> searchModelList;
    List<RecipesModel> searchModelListOld;
    String filterPattern = "";
    private int lastPosition = -1;
    private  RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);

    public SearchAdapter(Context context, List<RecipesModel> searchModelList) {
        this.context = context;
        this.searchModelList = searchModelList;
        this.searchModelListOld = searchModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderCate onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderCate(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderCate holder, int position) {
        RecipesModel s=searchModelList.get(position);
        List<RecipesModel> s1=new ArrayList<>();
        if (s1.size()>0){
           RecipesModel recipesModel=s1.get(0);
        }
        if (s==null){
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
                holder.txtname.setText(spannable);
            } else {
                holder.txtname.setText(s.getName());
            }
        } else {
            holder.txtname.setText(s.getName());
        }
        if(s.getImage().isEmpty()){
            Glide.with(context).load(R.drawable.image).into(holder.cateImg);
        }
        else {
            Glide.with(context).load(s.getImage()).apply(requestOptions).placeholder(R.drawable.image).into(holder.cateImg);
        }
        holder.txtSearch.setText(s.getSearch());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipesActivity.class);
                intent.putExtra("recipes",s);
                context.startActivity(intent);
            }
        });
        setAnimation(holder.itemView, position);
    }

private void setAnimation(View viewToAnimate, int position)
{
    // If the bound view wasn't previously displayed on screen, it's animated
    if (position > lastPosition)
    {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
        lastPosition = position;
    }
}

    @Override
    public int getItemCount() {
        if (searchModelList!=null){
            return searchModelList.size();
        }
      return 0;
    }
    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filterPattern = constraint.toString().toLowerCase().trim();
                //String strsearch2 = constraint.toString();if (strsearch.isEmpty()) {
                if (TextUtils.isEmpty(constraint)) {
                    filterPattern="";
                    searchModelList.addAll(searchModelListOld);
                    //searchModelList = searchModelListOld;
                } else {
                    List<RecipesModel> list = new ArrayList<>();
                    for (RecipesModel searchModel : searchModelListOld) {
//                        if ( searchModel.getName().toLowerCase().contains(strsearch) || searchModel.getSearch().toLowerCase().contains(strsearch)) {
//                            list.add(searchModel);
//                        }
                        if ( searchModel.getName().toLowerCase().contains(filterPattern)) {
                            list.add(searchModel);
                        }

                    }
                    searchModelList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = searchModelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                searchModelList = (List<RecipesModel>) results.values;
                notifyDataSetChanged();

            }

        };
    }

    public class ViewHolderCate extends RecyclerView.ViewHolder {
        ImageView cateImg;
        TextView txtname,txtSearch;

        public ViewHolderCate(@NonNull @NotNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txt);
            txtSearch = itemView.findViewById(R.id.txtsearch);
            cateImg = itemView.findViewById(R.id.img);

        }


    }
}
