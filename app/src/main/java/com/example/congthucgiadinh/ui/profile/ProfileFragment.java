package com.example.congthucgiadinh.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.congthucgiadinh.R;


public class ProfileFragment extends Fragment {
    private ImageView btnFacebook,btnEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_profile, container, false);

        btnEmail=root.findViewById(R.id.btnEmail);
        btnFacebook=root.findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.facebook.com/nhatnguyen.tran.7505/");
            }
        });
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://mail.google.com/mail/u/1/#inbox?compose=DmwnWrRpccgnBPwnrKmLbrSkWlWXpdWHlMGbVjWSzgJSprhQVZdLBHxHpsnsvqtnvqCklPStDkhq");
            }
        });

        return root;
    }
    private void gotoUrl(String s) {
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));

    }
}