package com.nhatnguyen.congthucgiadinh.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.NoteModel;
import com.nhatnguyen.congthucgiadinh.ui.note.UpdateNoteActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolderOffice> {
    private List<NoteModel> noteModelList;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private Context context;
    private int lastPosition = -1;
    public NoteAdapter(List<NoteModel> noteModelList, Context context) {
        this.noteModelList = noteModelList;
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderOffice onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolderOffice(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderOffice holder, int position) {
        NoteModel current = noteModelList.get(position);
        if (current == null) {
            return;
        }
        holder.txtnote.setText(current.getNote());
        holder.txtnote.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        holder.txtnote.setSelected(true);
        holder.txtTimer.setText(current.getTimer());
        holder.txtTitle.setText(current.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateNoteActivity.class);
                intent.putExtra("Note", noteModelList.get(position));
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDilog( position);


            }
        });
        setAnimation(holder.itemView, position);
    }

    private void openDeleteDilog( int position) {
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
                                .child("Note").
                                child(noteModelList.get(position).getId())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    noteModelList.remove(noteModelList.get(position));
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Đã xóa ghi chú!", Toast.LENGTH_SHORT).show();
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

        if (noteModelList != null) {
            return noteModelList.size();
        }
        return 0;
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
    public  class ViewHolderOffice extends RecyclerView.ViewHolder {

        TextView txtnote, txtTimer, txtTitle;
        ImageView delete;

        public ViewHolderOffice(@NonNull @NotNull View itemView) {
            super(itemView);
            txtnote = itemView.findViewById(R.id.txtnote);
            delete = itemView.findViewById(R.id.delete);
            txtTimer = itemView.findViewById(R.id.txtTimer);
            txtTitle = itemView.findViewById(R.id.txtTitle);

        }
    }
}
