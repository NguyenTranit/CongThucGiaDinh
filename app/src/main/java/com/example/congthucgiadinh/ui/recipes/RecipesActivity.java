package com.example.congthucgiadinh.ui.recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.congthucgiadinh.R;
import com.example.congthucgiadinh.model.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;


public class RecipesActivity extends AppCompatActivity {
    private  ImageView imgMeal;
    private  TextView mealname, timers, ingredients, describes, youtubes;
    private  TextView favourite;
    private Toolbar toolbar;
    private ViewAllModel viewAllModel = null;
   private FirebaseFirestore firestore;
   private FirebaseAuth auth;
   String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = auth.getCurrentUser();

        currentUserID = mFirebaseUser.getUid(); //Do what you need to do with the id

        //
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        final Object object = getIntent().getSerializableExtra("recipes");
        if (object instanceof ViewAllModel) {
            viewAllModel = (ViewAllModel) object;
        }


        imgMeal = findViewById(R.id.imgMealDetail);
        mealname = findViewById(R.id.txtMealname);
        timers = findViewById(R.id.txtTimer);
        ingredients = findViewById(R.id.txtIngredients);
        describes = findViewById(R.id.txtDescribe);
        youtubes = findViewById(R.id.txtyoutube);
        favourite = findViewById(R.id.txtfavourite);
        //
        if (viewAllModel != null) {
            Picasso.with(getApplicationContext()).load(viewAllModel.getImage()).into(imgMeal);
            mealname.setText(viewAllModel.getName());
            timers.setText(viewAllModel.getTimer());
            ingredients.setText(viewAllModel.getIngredient());
            describes.setText(viewAllModel.getDescribe());

            youtubes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(viewAllModel.getYoutube())));
                }
            });

        }


        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFav();
            }
        });

    }

    private void addToFav() {
        final HashMap<String ,Object> carMap=new HashMap<>();
        carMap.put("name",viewAllModel.getName());
        carMap.put("image",viewAllModel.getImage());
        carMap.put("describe",viewAllModel.getDescribe());
        carMap.put("timer",viewAllModel.getTimer());
        carMap.put("ingredient",viewAllModel.getIngredient());
        carMap.put("youtube",viewAllModel.getYoutube());

        firestore.collection("AddToFavourites").document(currentUserID)
                .collection("CurrentUsers").add(carMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {

                        Toast.makeText(RecipesActivity.this, "Đã lưu vào trang yêu thích!", Toast.LENGTH_SHORT).show();
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
}