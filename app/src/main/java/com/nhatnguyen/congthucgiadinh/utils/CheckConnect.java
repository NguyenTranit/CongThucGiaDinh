package com.nhatnguyen.congthucgiadinh.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.nhatnguyen.congthucgiadinh.R;

public class CheckConnect {
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void CheckedSignIn(Context context) {
        SpannableString title = new SpannableString("Thông báo");
        title.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red)), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage("Bạn đã đăng nhập rồi ")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }
    public static void WarningCheckInternet(Context context) {
        SpannableString title = new SpannableString("Thông báo");
        title.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red)), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_wifi)
                .setTitle(title)
                .setMessage("Không có tín hiệu internet . Bạn hãy kiểm tra lại kết nối !")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).show();
    }

//    public static void openBackAppDialog(Context context, int gravity) {
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.back_app);
//        Window window = dialog.getWindow();
//        if (window == null) {
//            return;
//        }
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = gravity;
//        window.setAttributes(windowAttributes);
//        if (Gravity.BOTTOM == gravity) {
//            dialog.setCancelable(false);
//        }
//        Button btnCancel = dialog.findViewById(R.id.cancel);
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        Button btnRatingApp = dialog.findViewById(R.id.ratingApp);
//        Button btnShareApp = dialog.findViewById(R.id.shareApp);
//        btnRatingApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +
//                            getPackageName())));
//                } catch (Exception ex) {
//                    startActivity(new
//                            Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" +
//                            getPackageName())));
//                }
//            }
//        });
//        btnShareApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(Intent.ACTION_SEND);
//                myIntent.setType("text/plain");
//                String shareBody = "Đây là ứng dụng nấu ăn 3 bữa 1 ngày . Nó rất hữu dụng và còn miễn phí nữa !\nBạn hãy tải nó về và sử dụng ngay nhé ! \n" + "https://play.google.com/store/apps/details?id=com.nhatnguyen.congthucgiadinh&hl=en";
//                String shareSub = "Ứng dụng nấu ăn tốt nhất hiện nay";
//                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
//                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(myIntent, "Chia sẻ tới bạn bè"));
//            }
//        });
//        dialog.show();
//
//
//    }
}
