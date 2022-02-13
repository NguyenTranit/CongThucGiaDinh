package com.nhatnguyen.congthucgiadinh.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.adapters.CategoryAdapter;
import com.nhatnguyen.congthucgiadinh.adapters.SlideAdapter;
import com.nhatnguyen.congthucgiadinh.adapters.ThucDonAdapter;
import com.nhatnguyen.congthucgiadinh.model.CategoryModel;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.model.ThucDonModel;
import com.nhatnguyen.congthucgiadinh.ui.search.SearchActivity;
import com.nhatnguyen.congthucgiadinh.ui.viewAll.ViewAllCategoryActivity;
import com.nhatnguyen.congthucgiadinh.ui.viewAll.ViewAllThucDonActivity;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private List<CategoryModel> categoryModelList;
    private List<ThucDonModel> thucDonModelList;
    private List<RecipesModel> recipesModelList;
    private ProgressBar progressBar;
    private DatabaseReference dbAll;
    private RecyclerView ryc_category, ryc_view;
    private CategoryAdapter categoryAdapter;
    private ThucDonAdapter thucDonAdapter;
    private SlideAdapter slideAdapter;
    private ViewPager2 viewPager2;
    private Handler sliderhandle = new Handler();
    private FrameLayout adContainerView;
    private AdView adView;
    private VideoView videoView;
    private TextView search;
    private ImageView imgViewAll, imgViewAll_TT;


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CheckInternet();
        AnhXa(view);
        VideoPlay();
        QuangCao();
        BlinkImage();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ExitApp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        imgViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewAllCategoryActivity.class);
                startActivity(intent);
            }
        });
        imgViewAll_TT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewAllThucDonActivity.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);

            }
        });
        progressBar.setVisibility(View.VISIBLE);
        viewPager2.getParent().requestDisallowInterceptTouchEvent(true);

        ReadCategory();
        ryc_category.setHasFixedSize(true);
        ryc_category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(getActivity(), categoryModelList);
        ryc_category.setAdapter(categoryAdapter);
        //

        ReadThucDon();
        ryc_view.setHasFixedSize(true);
        thucDonAdapter = new ThucDonAdapter(getActivity(), thucDonModelList);
        ryc_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        ryc_view.setAdapter(thucDonAdapter);
        //
        ReadSlide();


    }

    private void ExitApp() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.back_app);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        if (window==null){
            return;
        }

        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == Gravity.BOTTOM) {
            dialog.setCancelable(false);
        }
        ImageView btnCancel = dialog.findViewById(R.id.cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        AdView mAdView;
        TextView btnRatingApp = dialog.findViewById(R.id.ratingApp);
        TextView btnShareApp = dialog.findViewById(R.id.shareApp);
        TextView btnExitApp = dialog.findViewById(R.id.ExitApp);
        mAdView = dialog.findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
                super.onAdLoaded();
            }
        });
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        });

        btnExitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        btnRatingApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +
                            getActivity().getPackageName())));
                } catch (Exception ex) {
                    startActivity(new
                            Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" +
                            getActivity().getPackageName())));
                }
            }
        });
        btnShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Đây là ứng dụng nấu ăn 3 bữa 1 ngày . Nó rất hữu dụng và còn miễn phí nữa !\nBạn hãy tải nó về và sử dụng ngay nhé ! \n" + "https://play.google.com/store/apps/details?id=com.nhatnguyen.congthucgiadinh&hl=en";
                String shareSub = "Ứng dụng nấu ăn tốt nhất hiện nay";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Chia sẻ tới bạn bè"));
            }
        });
        dialog.show();
    }

    private void BlinkImage() {
        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(400); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        imgViewAll.startAnimation(animation); //to start animation
        imgViewAll_TT.startAnimation(animation);
    }

    private void VideoPlay() {
        videoView.setVideoPath("https://firebasestorage.googleapis.com/v0/b/congthucgiadinh.appspot.com/o/mp3%2Fvideo.mp4?alt=media&token=d944b6a4-7ae0-470d-bdc0-90e582436291");
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    private void QuangCao() {
        adContainerView.setVisibility(View.GONE);
        adView = new AdView(getActivity());
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id));
        adView.setAdSize(adSize());
        adContainerView.addView(adView);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adContainerView.setVisibility(View.VISIBLE);
            }
        });
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadBanner();
            }

        });
    }

    private void AnhXa(View view) {
        adContainerView = view.findViewById(R.id.framelayout);
        progressBar = view.findViewById(R.id.progressbarHome);
        imgViewAll = view.findViewById(R.id.imgViewAll);
        imgViewAll_TT = view.findViewById(R.id.imgViewAll_TT);
        search = view.findViewById(R.id.editSearch);
        videoView = view.findViewById(R.id.video);
        ryc_view = view.findViewById(R.id.ryc_view);
        ryc_category = view.findViewById(R.id.rcy_category);
        viewPager2 = view.findViewById(R.id.viewpager);
    }

    private void CheckInternet() {
        if (!CheckConnect.haveNetworkConnection(getActivity())) {
            CheckConnect.WarningCheckInternet(getActivity());
        }
    }

    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize adSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getActivity(), adWidth);

    }

    private void ReadSlide() {
        recipesModelList = new ArrayList<>();
        dbAll = FirebaseDatabase.getInstance().getReference("SliderMeal");
        dbAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapShot) {
                if (dataSnapShot.exists()) {

                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapShot.getChildren()) {
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
                        recipesModelList.add(recipesModel);
                    }
                    Collections.sort(recipesModelList, RecipesModel.BY_NAME_ALPHABETICAL);
                    Collections.reverse(recipesModelList);
                    slideAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        //
        slideAdapter = new SlideAdapter(getActivity(), recipesModelList, viewPager2);
        viewPager2.setAdapter(slideAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull @NotNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderhandle.removeCallbacks(sliderRunnable);
                sliderhandle.postDelayed(sliderRunnable, 3000);

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        sliderhandle.removeCallbacks(sliderRunnable);
        videoView.suspend();

    }

    @Override
    public void onResume() {
        super.onResume();
        sliderhandle.postDelayed(sliderRunnable, 3000);
        videoView.resume();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();

    }

    private void ReadCategory() {

        categoryModelList = new ArrayList<>();
        dbAll = FirebaseDatabase.getInstance().getReference("Category");
        dbAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapShot) {
                if (dataSnapShot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapShot.getChildren()) {
                        String name = ds.getKey();
                        String image = ds.child("image").getValue(String.class);
                        CategoryModel categoryModel = new CategoryModel(name, image);
                        categoryModelList.add(categoryModel);
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void ReadThucDon() {
        thucDonModelList = new ArrayList<>();
        dbAll = FirebaseDatabase.getInstance().getReference("ThucDon");
        dbAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapShot) {
                if (dataSnapShot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapShot.getChildren()) {
                        String name = ds.child("name").getValue(String.class);
                        ThucDonModel thucDonModel = new ThucDonModel(name);
                        thucDonModelList.add(thucDonModel);

                    }
                    thucDonAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

}