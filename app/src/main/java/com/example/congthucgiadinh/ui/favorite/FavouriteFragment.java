package com.example.congthucgiadinh.ui.favorite;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.congthucgiadinh.R;
import com.example.congthucgiadinh.adapters.FavouriteAdapter;
import com.example.congthucgiadinh.model.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends Fragment {

    private RecyclerView ryc_favourite;
    private FavouriteAdapter favouriteAdapter;
    private ArrayList<ViewAllModel> viewAllModelList;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    String currentUserID;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);
        progressBar = root.findViewById(R.id.progressbarHome);
        progressBar.setVisibility(View.VISIBLE);


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = auth.getCurrentUser();

            currentUserID = mFirebaseUser.getUid(); //Do what you need to do with the id

        ryc_favourite = root.findViewById(R.id.rcy_favourite);
        ryc_favourite.setHasFixedSize(true);
        viewAllModelList = new ArrayList<>();
        favouriteAdapter = new FavouriteAdapter(getActivity(), viewAllModelList);
        ryc_favourite.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ryc_favourite.setAdapter(favouriteAdapter);

        firestore.collection("AddToFavourites").document(currentUserID).collection("CurrentUsers")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        String documentId = documentSnapshot.getId();
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModel.setDocumentId(documentId);
                        viewAllModelList.add(viewAllModel);
                        favouriteAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        return root;
    }
}