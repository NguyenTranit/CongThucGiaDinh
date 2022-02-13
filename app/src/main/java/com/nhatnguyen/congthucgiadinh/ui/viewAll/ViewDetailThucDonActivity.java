package com.nhatnguyen.congthucgiadinh.ui.viewAll;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.adapters.DetailThucDonAdapter;
import com.nhatnguyen.congthucgiadinh.model.DetailThucDonModel;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewDetailThucDonActivity extends AppCompatActivity {
    private List<DetailThucDonModel> detailThucDonModelList;
    private DetailThucDonAdapter adapter;
    private RecyclerView recyclerView;
   private DatabaseReference dbView;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private SearchView searchView;
    private FrameLayout adContainerView;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_thucdon);
        //
        if (!CheckConnect.haveNetworkConnection(getApplicationContext())) {
            CheckConnect.WarningCheckInternet(this);
        }

        progressBar = findViewById(R.id.progressbarHome);
       progressBar.setVisibility(View.VISIBLE);
       All();
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

    private void All() {
        detailThucDonModelList =new ArrayList<>();
        recyclerView=findViewById(R.id.View_all);
        recyclerView.setHasFixedSize(true);
//RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter=new DetailThucDonAdapter(this, detailThucDonModelList);
        recyclerView.setAdapter(adapter);

        Intent intent=getIntent();
        String thucdon=intent.getStringExtra("ThucDonView");
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrowleft2);
        dbView= FirebaseDatabase.getInstance().getReference("DetailThucDon")
                .child(thucdon);
        dbView.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if(dataSnapshot.exists()){
                    for (DataSnapshot viewAllSnapshot:dataSnapshot.getChildren()){
                        DetailThucDonModel detailThucDonModel =viewAllSnapshot.getValue(DetailThucDonModel.class);
                        detailThucDonModelList.add(detailThucDonModel);
                        setTitle(thucdon);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

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
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_search_white));
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
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
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