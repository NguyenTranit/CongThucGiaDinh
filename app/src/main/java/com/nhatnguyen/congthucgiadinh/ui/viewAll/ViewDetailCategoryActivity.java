package com.nhatnguyen.congthucgiadinh.ui.viewAll;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.adapters.DetailCategoryAdapter;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;
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
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewDetailCategoryActivity extends AppCompatActivity {
    private List<RecipesModel> recipesModelList;
    private DetailCategoryAdapter detailCategoryAdapter;
    private RecyclerView recyclerView;
   private DatabaseReference dbView;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private SearchView searchView;
    private FrameLayout adContainerView;
    private AdView adView;
    int sum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);
        CheckInternet();
        AnhXa();
        All();
        QuangCao();
    }

    private void AnhXa() {
        progressBar = findViewById(R.id.progressbarHome);
        recyclerView=findViewById(R.id.View_all_rec);
        toolbar = findViewById(R.id.toolbar);
    }

    private void CheckInternet() {
        if (!CheckConnect.haveNetworkConnection(getApplicationContext())) {
        CheckConnect.WarningCheckInternet(this);
    }
    }

    private void QuangCao() {
        // quang cao
        adContainerView = findViewById(R.id.framelayout);
        adContainerView.setVisibility(View.GONE);
        // Step 1 - Create an AdView and set the ad unit ID on it.
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id2));
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
        recipesModelList =new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        detailCategoryAdapter =new DetailCategoryAdapter(this, recipesModelList);
        recyclerView.setAdapter(detailCategoryAdapter);

        //
        Intent intent=getIntent();
        String category=intent.getStringExtra("Category");
        //
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrowleft2);
        progressBar.setVisibility(View.VISIBLE);
        dbView= FirebaseDatabase.getInstance().getReference("view").child("DetailCategory")
                .child(category);
        dbView.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds:dataSnapshot.getChildren()){

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
                        String describe2=ds.child("describe2").getValue(String.class);
                        String describe3=ds.child("describe3").getValue(String.class);
                        String describe4=ds.child("describe4").getValue(String.class);
                        String describe5=ds.child("describe5").getValue(String.class);
                        String describe6=ds.child("describe6").getValue(String.class);
                        String timer=ds.child("timer").getValue(String.class);
                        String search=ds.child("search").getValue(String.class);

                        RecipesModel recipesModel = new RecipesModel(name, ingredient, youtube,describe,describe2,describe3,describe4,describe5,describe6,
                                image, imgStep1,imgStep2,imgStep3,imgStep4,imgStep5,imgStep6,timer,search);
                        recipesModelList.add(recipesModel);
                        sum= (int) dataSnapshot.getChildrenCount();
                        setTitle(category+" ("+sum+" món)");

                    }
                    Collections.sort(recipesModelList,RecipesModel.BY_NAME_ALPHABETICAL);
                    Collections.reverse(recipesModelList);
                    detailCategoryAdapter.notifyDataSetChanged();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.ic_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        //
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._18sp));
        searchView.setQueryHint("Tìm kiếm món ăn");
        ImageView iconClose =  searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        iconClose.setColorFilter(getResources().getColor(R.color.white));
        //change search icon color
        ImageView iconSearch = searchView.findViewById(androidx.appcompat.R.id.search_button);
        iconSearch.setColorFilter(getResources().getColor(R.color.white));
        //
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                detailCategoryAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                detailCategoryAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}