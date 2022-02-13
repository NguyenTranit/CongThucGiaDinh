package com.nhatnguyen.congthucgiadinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.nhatnguyen.congthucgiadinh.BuildConfig;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.DetailThucDonModel;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.ui.viewAll.ViewAllCategoryActivity;
import com.nhatnguyen.congthucgiadinh.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nhatnguyen.congthucgiadinh.ui.viewAll.ViewExpandThucDonActivity;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailThucDonAdapter extends RecyclerView.Adapter<DetailThucDonAdapter.ViewHolderOffice> implements Filterable {
    List<DetailThucDonModel> viewAllModelListOld;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    private Context context;
    private List<DetailThucDonModel> viewAllModelList2;
    String filterPattern = "";
    private RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);



    public DetailThucDonAdapter(Context context, List<DetailThucDonModel> viewAllModelList2) {
        this.context = context;
        this.viewAllModelList2 = viewAllModelList2;
        this.viewAllModelListOld = viewAllModelList2;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderOffice onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderOffice(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ryc_thuc_don, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderOffice holder, int position) {
        DetailThucDonModel s=viewAllModelList2.get(position);
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
                holder.title.setText(spannable);
            } else {
                holder.title.setText(s.getName());
            }
        } else {
            holder.title.setText(s.getName());
        }
        //
        if (s.getImage().isEmpty()) {
            Glide.with(context).load(R.drawable.image).into(holder.famiImg);
        } else {
            Glide.with(context).load(s.getImage()).apply(requestOptions).placeholder(R.drawable.image).into(holder.famiImg);
        }

        if (s.getIngredient().isEmpty()){
            holder.name.setVisibility(View.GONE);
        }
        else {
            holder.name.setText(s.getIngredient());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewExpandThucDonActivity.class);
                intent.putExtra("view",s);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() == null) {
                    Toast.makeText(context, "Bạn đăng nhập để lưu lại món ăn mình thích nhé", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);

                } else {
                    final HashMap<String, Object> carMap = new HashMap<>();
                    carMap.put("name", s.getName());
                    carMap.put("image", s.getImage());
                    carMap.put("ingredient", s.getIngredient());

                    DatabaseReference dbView = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("FavThucDon").child(s.getName());
                    dbView.setValue(carMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Đã lưu vào trang yêu thích ", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.famiImg.getDrawable() == null) {
                        Toast.makeText(context, "Hãy chờ cho dữ liệu được hiện thị đầy đủ !", Toast.LENGTH_SHORT).show();
                    }
                    else {

                    Bitmap bitmap = ((BitmapDrawable) holder.famiImg.getDrawable()).getBitmap();

                    try {
                        String s = holder.name.getText().toString();
                        String shareBody = " Đây là món ăn " + s + " có trong ứng dụng 3 bữa 1 ngày. Bạn hãy tải nó và trải nghiệm ngay nhé ! \n" + "https://play.google.com/store/apps/details?id=com.nhatnguyen.congthucgiadinh&hl=en";
                        File file = new File(context.getExternalCacheDir(), s+".png");
                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();

                        Uri uriPhoto=Uri.fromFile(file);
                        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
                            uriPhoto= FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".provider",file);
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM,uriPhoto);
                        intent.putExtra(Intent.EXTRA_TEXT, s);
                        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        intent.setType("image/png");
                        context.startActivity(Intent.createChooser(intent, "Chia sẻ tới bạn bè"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                }
            });

    }


    @Override
    public int getItemCount() {
        if (viewAllModelList2!=null){
            return viewAllModelList2.size();
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
                    filterPattern="";
                    viewAllModelList2.addAll(viewAllModelListOld) ;
                } else {
                    List<DetailThucDonModel> list = new ArrayList<>();
                    for (DetailThucDonModel viewAllModel : viewAllModelListOld) {
                        if (viewAllModel.getName().toLowerCase().contains(filterPattern)) {
                            list.add(viewAllModel);
                        }
                    }
                    viewAllModelList2 = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = viewAllModelList2;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                viewAllModelList2 = (List<DetailThucDonModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolderOffice extends RecyclerView.ViewHolder {
        ImageView famiImg;
        TextView name, title;
        TextView share, save;

        public ViewHolderOffice(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            title = itemView.findViewById(R.id.title);
            famiImg = itemView.findViewById(R.id.imageCate);
            share = itemView.findViewById(R.id.share);
            save = itemView.findViewById(R.id.save);

        }


    }
}
