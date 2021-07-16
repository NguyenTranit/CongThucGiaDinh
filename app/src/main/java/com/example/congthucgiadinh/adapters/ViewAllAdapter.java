package com.example.congthucgiadinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.congthucgiadinh.R;
import com.example.congthucgiadinh.model.ViewAllModel;
import com.example.congthucgiadinh.ui.recipes.RecipesActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder> implements Filterable {

    Context context;
    List<ViewAllModel> viewAllModelList;
    List<ViewAllModel> viewAllModelListOld;

    public ViewAllAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
        this.viewAllModelListOld = viewAllModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ryc_category_detail, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
       if(viewAllModelList.get(position).getImage().isEmpty()){
           Picasso.with(context).load(R.drawable.salad2).into(holder.img);
       }
       else {
           Picasso.with(context).load(viewAllModelList.get(position).getImage()).into(holder.img);
       }

        holder.name.setText(viewAllModelList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipesActivity.class);
                intent.putExtra("recipes", viewAllModelList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return viewAllModelList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strsearch = constraint.toString();
                if (strsearch.isEmpty()) {
                    viewAllModelList = viewAllModelListOld;
                } else {
                    List<ViewAllModel> list = new ArrayList<>();
                    for (ViewAllModel viewAllModel : viewAllModelListOld) {
                        if (viewAllModel.getName().toLowerCase().contains(strsearch.toLowerCase())) {
                            list.add(viewAllModel);
                        }
                    }
                    viewAllModelList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = viewAllModelList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                viewAllModelList = (List<ViewAllModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageCate);
            name = itemView.findViewById(R.id.mealName);

        }
    }
}
