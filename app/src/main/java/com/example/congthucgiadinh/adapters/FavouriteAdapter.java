package com.example.congthucgiadinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.congthucgiadinh.R;
import com.example.congthucgiadinh.model.ViewAllModel;
import com.example.congthucgiadinh.ui.recipes.RecipesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    Context context;
    List<ViewAllModel> viewAllModelList;
    FirebaseAuth auth;
    FirebaseFirestore firestore;


    public FavouriteAdapter(Context context, List<ViewAllModel> viewAllModelList) {
        this.context = context;
        this.viewAllModelList = viewAllModelList;
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new FavouriteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ryc_favourite, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavouriteViewHolder holder, int position) {

        ViewAllModel current = viewAllModelList.get(position);
        Picasso.with(context).load(current.getImage()).into(holder.image);
        holder.name.setText(current.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, RecipesActivity.class);
                intent.putExtra("recipes",current);
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("AddToFavourites").document(auth.getCurrentUser().getUid()).collection("CurrentUsers")
                        .document(viewAllModelList.get(position).getDocumentId())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                viewAllModelList.remove(viewAllModelList.get(position));
                                notifyDataSetChanged();
                                Toast.makeText(context, "Đã xóa !", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context, "Lỗi : " +task.getException(), Toast.LENGTH_SHORT).show();
                            }
                            }
                        });
            }
        });



    }

    @Override
    public int getItemCount() {
        return viewAllModelList.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder  {
        TextView name;
        ImageView image,delete;

        public FavouriteViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageCate);
            name = itemView.findViewById(R.id.mealName);
            delete=itemView.findViewById(R.id.delete);

        }

    }
}
