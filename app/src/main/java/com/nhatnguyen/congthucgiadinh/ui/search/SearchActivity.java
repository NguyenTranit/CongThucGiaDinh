package com.nhatnguyen.congthucgiadinh.ui.search;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.adapters.SearchAdapter;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;
import com.nhatnguyen.congthucgiadinh.utils.WrapContentLinearLayoutManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    protected static final int RESULT_SPEECH = 1;
    private DatabaseReference ref;
    private RecyclerView ryc_search;
    private ImageView txtHome;
    private List<RecipesModel> searchelist ;
    private ImageView img, imgRecord;
    private TextView textView;
    private SearchView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        CheckInternet();
        img = findViewById(R.id.imgView);
        textView = findViewById(R.id.textview);
        txtHome = findViewById(R.id.txtHome);
        txtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("view").child("DetailCategory");
        search = findViewById(R.id.search);
        search.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        ryc_search = findViewById(R.id.recycle_search);
        searchelist = new ArrayList<>();
        SearchAdapter adapter = new SearchAdapter(SearchActivity.this, searchelist);
        RecyclerView.LayoutManager layoutManager = new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ryc_search.setLayoutManager(layoutManager);
        imgRecord = findViewById(R.id.imgRecord);
        imgRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VI");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    search.setQuery("", true);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Thiết bị của bạn không hỗ trợ chuyển lời nói thành văn bản", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


//        EditText searchEditText = findViewById(androidx.appcompat.R.id.search_src_text);
//        searchEditText.setTextColor(getResources().getColor(R.color.black));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                if (s != null) {
                    if (adapter.getItemCount() <= 0) {
                        ryc_search.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Kết quả tìm kiếm không tồn tại trong kho dữ liệu ứng dụng. Bạn hãy thử tìm kiếm theo từ khóa hoặc nguyên liệu...");
                    } else {
                        ryc_search.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.GONE);
                        img.setVisibility(View.GONE);
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                if (s.equals("")) {
                    ryc_search.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Khám phá muôn vàn món ăn ngon tại đây .");
                } else {

                    if (adapter.getItemCount() <= 0) {
                        ryc_search.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("Kết quả tìm kiếm không tồn tại trong kho dữ liệu ứng dụng. Bạn hãy thử tìm kiếm theo từ khóa hoặc nguyên liệu...");
                    } else {
                        ryc_search.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.GONE);
                        img.setVisibility(View.GONE);
                    }
                }
//                ryc_search.setVisibility(View.GONE);
//                img.setVisibility(View.VISIBLE);
//                textView.setVisibility(View.VISIBLE);
//                textView.setText("Khám phá muôn vàn món ăn ngon tại đây .");
                return false;
            }
        });
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot view : snapshot.getChildren()) {
                        for (DataSnapshot ds : view.getChildren()) {
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

                            searchelist.add(recipesModel);

                        }
                    }

                   ryc_search.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    ryc_search.setVisibility(View.GONE);
                    ryc_search.stopScroll();
                    img.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    Collections.sort(searchelist, RecipesModel.BY_NAME_ALPHABETICAL);
                    Collections.reverse(searchelist);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

    }

    private void CheckInternet() {
        if (!CheckConnect.haveNetworkConnection(getApplicationContext())) {
            CheckConnect.WarningCheckInternet(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search.setQuery(text.get(0), true);
                }
                break;
        }
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