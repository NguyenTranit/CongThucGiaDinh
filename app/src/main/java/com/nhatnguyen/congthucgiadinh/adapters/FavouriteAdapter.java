package com.nhatnguyen.congthucgiadinh.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.databinding.FragmentFavouriteBinding;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.ui.favorite.FavouriteFragment;
import com.nhatnguyen.congthucgiadinh.ui.recipes.RecipesActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    Context context;
    List<RecipesModel> recipesModelList;
    FirebaseAuth auth;
    DatabaseReference ref;
    private int lastPosition = -1;
    private RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);

    public FavouriteAdapter(Context context, List<RecipesModel> recipesModelList) {
        this.context = context;
        this.recipesModelList = recipesModelList;

        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new FavouriteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ryc_favourite, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FavouriteViewHolder holder, int position) {

        RecipesModel current = recipesModelList.get(position);
        if (current == null) {
            return;
        }
        if (current.getImage().isEmpty()) {
            Glide.with(context).load(R.drawable.image).into(holder.image);
        } else {
            Glide.with(context).load(current.getImage()).apply(requestOptions).placeholder(R.drawable.image).into(holder.image);
        }
        holder.name.setText(current.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecipesActivity.class);
                intent.putExtra("recipes", current);
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openDeleteDilog(position);

            }
        });
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    private void openDeleteDilog(int position) {
        FavouriteFragment object= (FavouriteFragment) ((FragmentActivity)context).getSupportFragmentManager().findFragmentById(R.id.nav_favourite);
        SpannableString title = new SpannableString("Xóa mục đã chọn");
        title.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red)), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage("Bạn muốn xóa mục này ?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("FavRecipes")
                                .child(recipesModelList.get(position).getName())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    recipesModelList.remove(recipesModelList.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Đã xóa !", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        if (recipesModelList != null) {
            return recipesModelList.size();
        }
        return 0;
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView  delete;
        RoundedImageView image;

        public FavouriteViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageCate);
            name = itemView.findViewById(R.id.mealName);
            delete = itemView.findViewById(R.id.delete);

        }

    }
}
