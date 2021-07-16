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
import com.example.congthucgiadinh.model.ViewAllModel;
import com.example.congthucgiadinh.ui.recipes.RecipesActivity;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolderFamily> {
    private Context context;
    private List<ViewAllModel> viewAllModelList;

    public FamilyAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderFamily onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderFamily(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_family, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderFamily holder, int position) {

        //Glide.with(context).load(familyModelList.get(position).getImage()).into(holder.famiImg);
        Picasso.with(context).load(viewAllModelList.get(position).getImage()).into(holder.famiImg);
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

    public class ViewHolderFamily extends RecyclerView.ViewHolder {
        ImageView famiImg;
        TextView name;

        public ViewHolderFamily(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameFamily);
            famiImg = itemView.findViewById(R.id.imgfamily);

        }
    }
}
