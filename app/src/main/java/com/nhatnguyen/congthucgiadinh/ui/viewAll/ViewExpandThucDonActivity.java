package com.nhatnguyen.congthucgiadinh.ui.viewAll;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.DetailThucDonModel;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;
import com.squareup.picasso.Picasso;

public class ViewExpandThucDonActivity extends AppCompatActivity {
        private ImageView imageView;
        private TextView textView;
        private Button btnBack;
        private DetailThucDonModel detailThucDonModel = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_expand_thuc_don);
            CheckIntenet();
            AnhXa();
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            final Object object = getIntent().getSerializableExtra("view");
            if (object instanceof DetailThucDonModel) {
                detailThucDonModel = (DetailThucDonModel) object;
            }
            if (detailThucDonModel != null) {
                Picasso.with(this).load(detailThucDonModel.getImage()).into(imageView);
                if (detailThucDonModel.getIngredient().isEmpty()){
                   textView.setVisibility(View.GONE);
                }
                else {
                    textView.setText(detailThucDonModel.getIngredient());
                }
            }

        }

    private void AnhXa() {
        btnBack = findViewById(R.id.btnBack);
        imageView = findViewById(R.id.imgView);
        textView = findViewById(R.id.txtName);
    }

    private void CheckIntenet() {
        if (!CheckConnect.haveNetworkConnection(getApplicationContext())) {
            CheckConnect.WarningCheckInternet(this);
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
