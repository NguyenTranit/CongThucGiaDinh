package com.example.congthucgiadinh.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.congthucgiadinh.MainActivity;
import com.example.congthucgiadinh.R;

public class SlashActivity extends AppCompatActivity {

    private static int SPLASH_SCCREEN = 5000;
    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView cooking, from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slash);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        imageView = findViewById(R.id.imageView);
        cooking = findViewById(R.id.cooking);
        from = findViewById(R.id.from);
        imageView.setAnimation(topAnim);
        cooking.setAnimation(bottomAnim);
        from.setAnimation(bottomAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SlashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCCREEN);
    }
}