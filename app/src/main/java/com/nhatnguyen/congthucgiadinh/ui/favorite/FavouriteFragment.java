package com.nhatnguyen.congthucgiadinh.ui.favorite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.adapters.FavouriteAdapter;
import com.nhatnguyen.congthucgiadinh.adapters.FavouriteAdapter2;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.model.DetailThucDonModel;
import com.nhatnguyen.congthucgiadinh.ui.home.HomeFragment;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment {

    private RecyclerView ryc_favourite, rcy_favourite2;
    private FavouriteAdapter favouriteAdapter;
    private ArrayList<RecipesModel> recipesModelList;
    private ArrayList<DetailThucDonModel> detailThucDonModelList;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FavouriteAdapter2 favouriteAdapter2;
    private DatabaseReference dbFav;
    private ImageView imgNone;
    private FrameLayout adContainerView;
    private AdView adView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);
        CheckInternet();
        AnhXa(root);
        QuangCao();
        progressBar.setVisibility(View.VISIBLE);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        ryc_favourite.setHasFixedSize(true);
        recipesModelList = new ArrayList<>();
        favouriteAdapter = new FavouriteAdapter(getActivity(), recipesModelList);
        ryc_favourite.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        ryc_favourite.setAdapter(favouriteAdapter);
        //
        rcy_favourite2.setHasFixedSize(true);
        detailThucDonModelList = new ArrayList<>();
        favouriteAdapter2 = new FavouriteAdapter2(getActivity(), detailThucDonModelList);
        rcy_favourite2.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rcy_favourite2.setAdapter(favouriteAdapter2);

        if (auth.getCurrentUser() == null) {
            progressBar.setVisibility(View.GONE);
            imgNone.setVisibility(View.VISIBLE);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    adContainerView.setVisibility(View.GONE);
                }
            });
        } else {

            dbFav = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("FavRecipes");
            dbFav.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        progressBar.setVisibility(View.GONE);
                        imgNone.setVisibility(View.GONE);
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String name = ds.child("name").getValue(String.class);
                            String ingredient = ds.child("ingredient").getValue(String.class);
                            String youtube = ds.child("youtube").getValue(String.class);
                            String image = ds.child("image").getValue(String.class);
                            String imgStep1 = ds.child("imgStep1").getValue(String.class);
                            String imgStep2 = ds.child("imgStep2").getValue(String.class);
                            String imgStep3 = ds.child("imgStep3").getValue(String.class);
                            String imgStep4 = ds.child("imgStep4").getValue(String.class);
                            String imgStep5 = ds.child("imgStep5").getValue(String.class);
                            String imgStep6 = ds.child("imgStep6").getValue(String.class);
                            String describe = ds.child("describe").getValue(String.class);
                            String describe2 = ds.child("describe2").getValue(String.class);
                            String describe3 = ds.child("describe3").getValue(String.class);
                            String describe4 = ds.child("describe4").getValue(String.class);
                            String describe5 = ds.child("describe5").getValue(String.class);
                            String describe6 = ds.child("describe6").getValue(String.class);
                            String timer = ds.child("timer").getValue(String.class);
                            String search = ds.child("search").getValue(String.class);
                            RecipesModel recipesModel = new RecipesModel(name, ingredient, youtube, describe, describe2, describe3, describe4, describe5, describe6,
                                    image, imgStep1, imgStep2, imgStep3, imgStep4, imgStep5, imgStep6, timer, search);
                            recipesModelList.add(recipesModel);
                            favouriteAdapter.notifyDataSetChanged();

                        }
                    }
                    else {
                        imgNone.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
            dbFav = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("FavThucDon");
            dbFav.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        progressBar.setVisibility(View.GONE);
                        imgNone.setVisibility(View.GONE);
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String name = ds.child("name").getValue(String.class);
                            String image = ds.child("image").getValue(String.class);
                            String ingredient = ds.child("ingredient").getValue(String.class);
                            DetailThucDonModel detailThucDonModel = new DetailThucDonModel(name, image, ingredient);
                            detailThucDonModelList.add(detailThucDonModel);
                            favouriteAdapter2.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        return root;
    }

    private void AnhXa(View root) {
        progressBar = root.findViewById(R.id.progressbarHome);
        imgNone = root.findViewById(R.id.imgNone);
        rcy_favourite2 = root.findViewById(R.id.rcy_favourite2);
        ryc_favourite = root.findViewById(R.id.rcy_favourite);
        adContainerView = root.findViewById(R.id.framelayout);
    }

    private void CheckInternet() {
        if (!CheckConnect.haveNetworkConnection(getActivity())) {
            CheckConnect.WarningCheckInternet(getActivity());
        }
    }

    private void QuangCao() {
        adContainerView.setVisibility(View.GONE);
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(getActivity());
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id));
        adView.setAdSize(adSize());
        adContainerView.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adContainerView.setVisibility(View.VISIBLE);
            }
        });
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadBanner();
            }

        });
    }

    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    private AdSize adSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getActivity(), adWidth);

    }
}