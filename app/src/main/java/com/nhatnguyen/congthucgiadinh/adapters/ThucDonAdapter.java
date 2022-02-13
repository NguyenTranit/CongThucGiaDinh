package com.nhatnguyen.congthucgiadinh.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.ThucDonModel;
import com.nhatnguyen.congthucgiadinh.ui.viewAll.ViewDetailThucDonActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ThucDonAdapter extends RecyclerView.Adapter<ThucDonAdapter.ViewHolderCate> {
    Context context;
    List<ThucDonModel> thucDonModelList;


    public ThucDonAdapter(Context context,List<ThucDonModel> thucDonModelList) {
        this.context = context;

        this.thucDonModelList = thucDonModelList;

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderCate onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderCate(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thucdon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderCate holder, int position) {
        ThucDonModel s = thucDonModelList.get(position);
        if (s == null) {
            return;
        }
        holder.txtname.setText(s.getName());

    }

    @Override
    public int getItemCount() {
        if (thucDonModelList!=null){
            return thucDonModelList.size();
        }
       return 0;
    }

    public class ViewHolderCate extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtname;

        public ViewHolderCate(@NonNull @NotNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ThucDonModel thucDonModel = thucDonModelList.get(position);
            Intent intent = new Intent(context, ViewDetailThucDonActivity.class);
            intent.putExtra("ThucDonView", thucDonModel.name);
            context.startActivity(intent);

        }
    }
}
