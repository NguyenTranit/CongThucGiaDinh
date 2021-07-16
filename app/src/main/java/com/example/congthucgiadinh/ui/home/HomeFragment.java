package com.example.congthucgiadinh.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.congthucgiadinh.R;
import com.example.congthucgiadinh.adapters.CategoryAdapter;
import com.example.congthucgiadinh.adapters.FamilyAdapter;
import com.example.congthucgiadinh.adapters.OfficeAdapter;
import com.example.congthucgiadinh.model.Category;
import com.example.congthucgiadinh.model.ViewAllModel;
import com.example.congthucgiadinh.ui.ViewAll.AllFamilyActivity;
import com.example.congthucgiadinh.ui.ViewAll.AllOfficeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<Category> categoryList;
    private List<ViewAllModel> viewAllModelList;
    private List<ViewAllModel> viewAllModelList2;
    private ProgressBar progressBar;
    private DatabaseReference dbAll;
    private RecyclerView ryc_category,ryc_familyMeal,rcy_officeMeal;
    private CategoryAdapter categoryAdapter;
    private FamilyAdapter familyAdapter;
    private OfficeAdapter officeAdapter;
    private TextView view_all_office,view_all_family;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressbarHome);
        progressBar.setVisibility(View.VISIBLE);
        view_all_family=view.findViewById(R.id.view_all_family);
        view_all_office=view.findViewById(R.id.view_all_office);
        view_all_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllOfficeActivity.class);
                startActivity(intent);
            }
        });
        view_all_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllFamilyActivity.class);
                startActivity(intent);

            }
        });

        //
        ReadCategory();
        ryc_category=view.findViewById(R.id.rcy_category);
        ryc_category.setHasFixedSize(true);
        ryc_category.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        categoryAdapter=new CategoryAdapter(getActivity(),categoryList);
        ryc_category.setAdapter(categoryAdapter);
        //
        ReadFamilyMeal();
        ryc_familyMeal=view.findViewById(R.id.ryc_family);
        ryc_familyMeal.setHasFixedSize(true);
        ryc_familyMeal.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        familyAdapter=new FamilyAdapter(getActivity(),viewAllModelList);
        ryc_familyMeal.setAdapter(familyAdapter);
        //
        ReadofficeMeal();
        rcy_officeMeal=view.findViewById(R.id.rcy_office);
        rcy_officeMeal.setHasFixedSize(true);
        rcy_officeMeal.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        officeAdapter=new OfficeAdapter(getActivity(),viewAllModelList2);
        rcy_officeMeal.setAdapter(officeAdapter);

    }

    private void ReadofficeMeal() {
        viewAllModelList2=new ArrayList<>();
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
                        viewAllModelList2.add(viewAllModel);
                    }
                    officeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void ReadFamilyMeal() {
        viewAllModelList=new ArrayList<>();
        dbAll = FirebaseDatabase.getInstance().getReference("FamilyMeal");
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
                    familyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void ReadCategory() {
        categoryList = new ArrayList<>();
        dbAll = FirebaseDatabase.getInstance().getReference("Category");
        dbAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapShot) {
                if (dataSnapShot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapShot.getChildren()) {
                        String name=ds.getKey();
                        String image = ds.child("image").getValue(String.class);
                        Category category = new Category(name,image);
                        categoryList.add(category);
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}