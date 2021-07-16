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

public class OfficeAdapter extends RecyclerView.Adapter<OfficeAdapter.ViewHolderOffice> {
    private Context context;
    private List<ViewAllModel> viewAllModelList2;

    public OfficeAdapter(Context context, List<ViewAllModel> viewAllModelList2) {
        this.context = context;
        this.viewAllModelList2 = viewAllModelList2;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderOffice onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderOffice(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_family,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderOffice holder, int position) {

        //Glide.with(context).load(familyModelList.get(position).getImage()).into(holder.famiImg);
        Picasso.with(context).load(viewAllModelList2.get(position).getImage()).into(holder.famiImg);
        holder.name.setText(viewAllModelList2.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, RecipesActivity.class);
                intent.putExtra("recipes",viewAllModelList2.get(position));
                context.startActivity(intent);
            }
        });
        //
    }

    @Override
    public int getItemCount() {
        return viewAllModelList2.size();
    }

    public class ViewHolderOffice extends RecyclerView.ViewHolder {
        ImageView famiImg;
        TextView name;
        public ViewHolderOffice(@NonNull @NotNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.nameFamily);
            famiImg=itemView.findViewById(R.id.imgfamily);

        }
    }
}
