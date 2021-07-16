package com.example.congthucgiadinh.ui.ViewAll;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.congthucgiadinh.R;
import com.example.congthucgiadinh.adapters.ViewAllAdapter;
import com.example.congthucgiadinh.model.ViewAllModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AllOfficeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private RecyclerView rcy_all_office;
    private ViewAllAdapter adapter;
    private List<ViewAllModel> viewAllModelList;
    private DatabaseReference dbAll;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_office);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        progressBar =findViewById(R.id.progressbarHome);
        progressBar.setVisibility(View.VISIBLE);

        //
        rcy_all_office=findViewById(R.id.rcy_all_office);
        rcy_all_office.setLayoutManager(new GridLayoutManager(this,2));
        rcy_all_office.setHasFixedSize(true);
        viewAllModelList = new ArrayList<>();
        adapter = new ViewAllAdapter(this,viewAllModelList);
        rcy_all_office.setAdapter(adapter);
        dbAll = FirebaseDatabase.getInstance().getReference("OfficeMeal");
        dbAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapShot) {
                if (dataSnapShot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapShot.getChildren()) {
                        String name = ds.child("name").getValue(String.class);
                        String image = ds.child("image").getValue(String.class);
                        String timer=ds.child("timer").getValue(String.class);
                        String ingredient=ds.child("ingredient").getValue(String.class);
                        String describe=ds.child("describe").getValue(String.class);
                        String youtube=ds.child("youtube").getValue(String.class);
                        ViewAllModel viewAllModel = new ViewAllModel(image, name, timer, ingredient, describe, youtube);
                        viewAllModelList.add(viewAllModel);
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
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView= (SearchView) menu.findItem(R.id.ic_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

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
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}