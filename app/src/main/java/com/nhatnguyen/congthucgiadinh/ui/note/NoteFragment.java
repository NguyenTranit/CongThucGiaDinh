package com.nhatnguyen.congthucgiadinh.ui.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.adapters.NoteAdapter;
import com.nhatnguyen.congthucgiadinh.model.NoteModel;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteFragment extends Fragment {
    private FloatingActionButton add_note;
    private RecyclerView ryc_note_list;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private ImageView imgNone;
    private NoteAdapter noteAdapter;
    private List<NoteModel> noteModelList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_note, container, false);
        CheckInternet();
        AnhXa(root);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                startActivity(intent);
            }
        });
        return root;

    }
    private void AnhXa(View root) {
        add_note = root.findViewById(R.id.add_note);
        auth = FirebaseAuth.getInstance();
        progressBar = root.findViewById(R.id.progressbarHome);
        progressBar.setVisibility(View.VISIBLE);
        imgNone = root.findViewById(R.id.imgNone);
        ryc_note_list = root.findViewById(R.id.ryc_note_list);
    }

    private void CheckInternet() {
        if (!CheckConnect.haveNetworkConnection(getActivity())) {
            CheckConnect.WarningCheckInternet(getActivity());
        }
    }

    private void datafill() {
        noteModelList = new ArrayList<>();
        ryc_note_list.setHasFixedSize(true);
        noteAdapter = new NoteAdapter(noteModelList, getContext());
        ryc_note_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ryc_note_list.setAdapter(noteAdapter);

        reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Note");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    imgNone.setVisibility(View.GONE);
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        String note = ds.child("note").getValue(String.class);
                        String timer = ds.child("timer").getValue(String.class);
                        String title = ds.child("title").getValue(String.class);
                        NoteModel noteModel1 = new NoteModel(id, note, timer, title);
                        noteModelList.add(noteModel1);
                    }
                    noteAdapter.notifyDataSetChanged();
                    Collections.sort(noteModelList, NoteModel.BY_NAME_ALPHABETICAL);
                    Collections.reverse(noteModelList);
                } else {
                    imgNone.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null) {
            progressBar.setVisibility(View.GONE);
            imgNone.setVisibility(View.VISIBLE);
            add_note.setVisibility(View.GONE);
        } else {
            add_note.setVisibility(View.VISIBLE);
            datafill();
        }

    }
}