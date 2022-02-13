package com.nhatnguyen.congthucgiadinh.ui.viewAll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.adapters.CategoryAdapter;
import com.nhatnguyen.congthucgiadinh.adapters.ThucDonAdapter;
import com.nhatnguyen.congthucgiadinh.adapters.ThucDonAdapter2;
import com.nhatnguyen.congthucgiadinh.model.CategoryModel;
import com.nhatnguyen.congthucgiadinh.model.ThucDonModel;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewAllThucDonActivity extends AppCompatActivity {
    private List<ThucDonModel> thucDonModelList;
    private ThucDonAdapter2 thucDonAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference dbAll;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private FrameLayout adContainerView;
    private AdView adView;
    private int sum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_thuc_don);
        CheckInternet();
        Anhxa();
        All();
        Quangcao();
    }
    private void Anhxa() {
        progressBar = findViewById(R.id.progressbarHome);
        recyclerView=findViewById(R.id.View_all_rec);
        toolbar = findViewById(R.id.toolbar);
    }

    private void CheckInternet() {
        if (!CheckConnect.haveNetworkConnection(getApplicationContext())) {
            CheckConnect.WarningCheckInternet(this);
        }
    }

    private void Quangcao() {
        adContainerView = findViewById(R.id.framelayout);
        adContainerView.setVisibility(View.GONE);
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id));
        adView.setAdSize(adSize());
        adContainerView.addView(adView);
        adView.setAdListener(new AdListener() {
            //            @Override
//            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
//            }
            @Override
            public void onAdLoaded() {
                adContainerView.setVisibility(View.VISIBLE);
            }
        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadBanner();
            }

        });
    }
    private void All() {
        thucDonModelList =new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        //
        thucDonAdapter=new ThucDonAdapter2(this, thucDonModelList);
        recyclerView.setAdapter(thucDonAdapter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrowleft2);
        progressBar.setVisibility(View.VISIBLE);
        dbAll = FirebaseDatabase.getInstance().getReference("ThucDon");
        dbAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapShot) {
                if (dataSnapShot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapShot.getChildren()) {
                        String name = ds.getKey();
                        ThucDonModel thucDonModel = new ThucDonModel(name);
                        thucDonModelList.add(thucDonModel);
                        sum= (int) dataSnapShot.getChildrenCount();
                        toolbar.setTitle(sum+" danh mục thực đơn");
                    }

                    thucDonAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    private AdSize adSize() {
        Display display =getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}