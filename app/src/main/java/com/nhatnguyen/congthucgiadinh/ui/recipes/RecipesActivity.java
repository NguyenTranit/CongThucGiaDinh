package com.nhatnguyen.congthucgiadinh.ui.recipes;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nhatnguyen.congthucgiadinh.BuildConfig;
import com.nhatnguyen.congthucgiadinh.R;
import com.nhatnguyen.congthucgiadinh.model.RecipesModel;
import com.nhatnguyen.congthucgiadinh.ui.Main.MainActivity;
import com.nhatnguyen.congthucgiadinh.ui.login.LoginActivity;
import com.nhatnguyen.congthucgiadinh.ui.note.AddNoteActivity;
import com.nhatnguyen.congthucgiadinh.ui.search.SearchActivity;
import com.nhatnguyen.congthucgiadinh.utils.CheckConnect;
import com.nhatnguyen.congthucgiadinh.utils.YoutubeConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RecipesActivity extends YouTubeBaseActivity {
    private CounterClass timer;
    private boolean mTimerRunning;
    private LinearLayout linearLayoutCounttime, toolbar;
    private MediaPlayer mediaPlayer;
    private ImageView cancelBell, cancel;
    private TextView txtStarttime, txtTimeShow;
    private boolean isOpen = false;
    private ImageView imgTitle;
    private RoundedImageView imgStep1, imgStep2, imgStep3, imgStep4, imgStep5, imgStep6;
    private TextView ingredients, describe, describe2, describe3, describe4, describe5, describe6, name, txtTimer, txt1, txt2, txt3, txt4, txt5, txt6;
    private RecipesModel recipesModel = null;
    private FirebaseAuth auth;
    private FrameLayout adContainerView;
    private AdView adView;
    private DatabaseReference dbView;
    private ImageView backArrow, imgFav, imgSearch;
    private FloatingActionButton fab, fab1, fab3, fab4, fab5;
    private Animation fabOpen, fabClose;
    private YouTubePlayerView myouTubePlayerView;
    private YouTubePlayer.OnInitializedListener monInitializedListener;
    private ImageView speakCT, speakCT_Off, speak1, speak1_Off, speak2, speak2_Off, speak3, speak3_Off, speak4, speak4_Off, speak5, speak5_Off, speak6, speak6_Off;
    private TextToSpeech toSpeech;
    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        checkInternet();
        anhXa();
        floatingButton();
        hideFab();
        QuangCao();
        SpeakData();
        countDownTime();
        final Object object = getIntent().getSerializableExtra("recipes");
        if (object instanceof RecipesModel) {
            recipesModel = (RecipesModel) object;
        }
        DataFill();
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipesActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() == null) {
                    Toast.makeText(RecipesActivity.this, "Bạn đăng nhập để lưu lại món ăn mình thích nhé", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RecipesActivity.this, LoginActivity.class));
                } else {
                    addToFav();
                }
            }
        });

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int minute, int second) {
            timer = new CounterClass((minute * 60 * 1000) + (second * 1000), 1000);
            long millis = (minute * 60 * 1000) + (second * 1000);
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            txtTimeShow.setText(hms);
            linearLayoutCounttime.setVisibility(View.VISIBLE);

        }
    };

    private void hideFab() {
        if (nestedScrollView != null) {
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY < oldScrollY) {//  Log.i(TAG, "Scroll UP");
                        fab.show();
                    }
                    if (scrollY > oldScrollY) {//Log.i(TAG, "Scroll DOWN");
                        fab.hide();
                    }
                }
            });
        }
    }


    private void SpeakData() {

        speakCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakCongThuc = "Bạn chuẩn bị nguyên liệu như sau:" + ingredients.getText().toString();
                toSpeech = new TextToSpeech(RecipesActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            toSpeech.setLanguage(Locale.forLanguageTag("vi-VI"));
                            toSpeech.speak(speakCongThuc, TextToSpeech.QUEUE_FLUSH, null);
                            speakCT.setVisibility(View.GONE);
                            speakCT_Off.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }
        });

        speakCT_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toSpeech.isSpeaking()) {
                    toSpeech.stop();
                    speakCT.setVisibility(View.VISIBLE);
                    speakCT_Off.setVisibility(View.GONE);
                } else {
                    speakCT.setVisibility(View.VISIBLE);
                }
            }
        });
//        bước1
        speak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakB1 = "Bước 1 :" + describe.getText().toString();
                toSpeech = new TextToSpeech(RecipesActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            toSpeech.setLanguage(Locale.forLanguageTag("vi-VI"));
                            toSpeech.speak(speakB1, TextToSpeech.QUEUE_FLUSH, null);
                            speak1.setVisibility(View.GONE);
                            speak1_Off.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        speak1_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toSpeech.isSpeaking()) {
                    toSpeech.stop();
                    speak1.setVisibility(View.VISIBLE);
                    speak1_Off.setVisibility(View.GONE);
                } else {
                    speak1.setVisibility(View.VISIBLE);
                }
            }
        });
        //        bước2
        speak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakB2 = "Bước 2 :" + describe2.getText().toString();
                toSpeech = new TextToSpeech(RecipesActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            toSpeech.setLanguage(Locale.forLanguageTag("vi-VI"));
                            toSpeech.speak(speakB2, TextToSpeech.QUEUE_FLUSH, null);
                            speak2.setVisibility(View.GONE);
                            speak2_Off.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        speak2_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toSpeech.isSpeaking()) {
                    toSpeech.stop();
                    speak2.setVisibility(View.VISIBLE);
                    speak2_Off.setVisibility(View.GONE);
                } else {
                    speak2.setVisibility(View.VISIBLE);
                }
            }
        });
        //        bước3
        speak3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakB3 = "Bước 3 :" + describe3.getText().toString();
                toSpeech = new TextToSpeech(RecipesActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            toSpeech.setLanguage(Locale.forLanguageTag("vi-VI"));
                            toSpeech.speak(speakB3, TextToSpeech.QUEUE_FLUSH, null);
                            speak3.setVisibility(View.GONE);
                            speak3_Off.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        speak3_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toSpeech.isSpeaking()) {
                    toSpeech.stop();
                    speak3.setVisibility(View.VISIBLE);
                    speak3_Off.setVisibility(View.GONE);
                } else {
                    speak3.setVisibility(View.VISIBLE);
                }
            }
        });
        //        bước4
        speak4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakB4 = "Bước 4 :" + describe4.getText().toString();
                toSpeech = new TextToSpeech(RecipesActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            toSpeech.setLanguage(Locale.forLanguageTag("vi-VI"));
                            toSpeech.speak(speakB4, TextToSpeech.QUEUE_FLUSH, null);
                            speak4.setVisibility(View.GONE);
                            speak4_Off.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        speak4_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toSpeech.isSpeaking()) {
                    toSpeech.stop();
                    speak4.setVisibility(View.VISIBLE);
                    speak4_Off.setVisibility(View.GONE);
                } else {
                    speak4.setVisibility(View.VISIBLE);
                }
            }
        });
        //        bước5
        speak5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakB5 = "Bước 5 :" + describe5.getText().toString();
                toSpeech = new TextToSpeech(RecipesActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            toSpeech.setLanguage(Locale.forLanguageTag("vi-VI"));
                            toSpeech.speak(speakB5, TextToSpeech.QUEUE_FLUSH, null);
                            speak5.setVisibility(View.GONE);
                            speak5_Off.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        speak5_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toSpeech.isSpeaking()) {
                    toSpeech.stop();
                    speak5.setVisibility(View.VISIBLE);
                    speak5_Off.setVisibility(View.GONE);
                } else {
                    speak5.setVisibility(View.VISIBLE);
                }
            }
        });
        //        bước6
        speak6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakB6 = "Bước 6 :" + describe6.getText().toString();
                toSpeech = new TextToSpeech(RecipesActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            toSpeech.setLanguage(Locale.forLanguageTag("vi-VI"));
                            toSpeech.speak(speakB6, TextToSpeech.QUEUE_FLUSH, null);
                            speak6.setVisibility(View.GONE);
                            speak6_Off.setVisibility(View.VISIBLE);

                        }
                    }
                });
            }
        });
        speak6_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toSpeech.isSpeaking()) {
                    toSpeech.stop();
                    speak6.setVisibility(View.VISIBLE);
                    speak6_Off.setVisibility(View.GONE);
                } else {
                    speak6.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    protected void onPause() {
        if (toSpeech != null) {
            toSpeech.stop();
        }
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toSpeech != null) {
            toSpeech.stop();
        }
    }

    private void countDownTime() {
        cancelBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                timer.cancel();
            }
        });
        txtStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.start();
                txtStarttime.setVisibility(View.GONE);
                Drawable[] drawables = txtTimeShow.getCompoundDrawables();
                for (Drawable drawable : drawables) {
                    if (drawable != null && drawable instanceof Animatable) {
                        ((Animatable) drawable).start();
                    }
                }
//                txtStarttime.setText("Playing");
//                txtStarttime.setEnabled(false);
            }
        });
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerRunning) {
                    timer.cancel();
                    openTimePickerDialog(false);
                    animationFab();
                    cancelBell.setVisibility(View.GONE);
                    txtStarttime.setVisibility(View.VISIBLE);
                    txtStarttime.setText("Play");
                    txtStarttime.setEnabled(true);
                    Drawable[] drawables = txtTimeShow.getCompoundDrawables();
                    for (Drawable drawable : drawables) {
                        if (drawable != null && drawable instanceof Animatable) {
                            ((Animatable) drawable).stop();
                        }
                    }
                } else {

                    openTimePickerDialog(false);
                    animationFab();
                    cancelBell.setVisibility(View.GONE);
                    txtStarttime.setVisibility(View.VISIBLE);
                    txtStarttime.setText("Play");
                    txtStarttime.setEnabled(true);
                    Drawable[] drawables = txtTimeShow.getCompoundDrawables();
                    for (Drawable drawable : drawables) {
                        if (drawable != null && drawable instanceof Animatable) {
                            ((Animatable) drawable).stop();
                        }
                    }
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    timer.cancel();
                    linearLayoutCounttime.setVisibility(View.INVISIBLE);
                } else {
                    linearLayoutCounttime.setVisibility(View.INVISIBLE);
                }
            }
        });

    }


    private void checkInternet() {
        if (!CheckConnect.haveNetworkConnection(getApplicationContext())) {
            CheckConnect.WarningCheckInternet(this);
        }
    }

    private void DataFill() {
        if (recipesModel != null) {

            monInitializedListener = new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    if (!b) {
                        youTubePlayer.loadVideo(recipesModel.getYoutube());
                    }
                    youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {

                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {
                            youTubePlayer.cueVideo(recipesModel.getYoutube());
                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                }
            };
            myouTubePlayerView.initialize(YoutubeConfig.getApiKey(), monInitializedListener);
            name.setText(recipesModel.getName());
            txtTimer.setText(recipesModel.getTimer());
            Glide.with(getApplicationContext()).load(recipesModel.getImage()).placeholder(R.drawable.image).into(imgTitle);
            if (recipesModel.getImgStep1().isEmpty()) {
                imgStep1.setVisibility(View.GONE);
            } else {
                imgStep1.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(recipesModel.getImgStep1()).apply(new RequestOptions().override(750, 0)).into(imgStep1);
            }
            if (recipesModel.getImgStep2().isEmpty()) {
                imgStep2.setVisibility(View.GONE);
            } else {
                imgStep2.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(recipesModel.getImgStep2()).apply(new RequestOptions().override(750, 0)).into(imgStep2);
            }
            if (recipesModel.getImgStep3().isEmpty()) {
                imgStep3.setVisibility(View.GONE);
            } else {
                imgStep3.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(recipesModel.getImgStep3()).apply(new RequestOptions().override(750, 0)).into(imgStep3);
            }
            if (recipesModel.getImgStep4().isEmpty()) {
                imgStep4.setVisibility(View.GONE);
            } else {
                imgStep4.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(recipesModel.getImgStep4()).apply(new RequestOptions().override(750, 0)).into(imgStep4);
            }
            if (recipesModel.getImgStep5().isEmpty()) {
                imgStep5.setVisibility(View.GONE);
            } else {
                imgStep5.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(recipesModel.getImgStep5()).apply(new RequestOptions().override(750, 0)).into(imgStep5);
            }

            if (recipesModel.getImgStep6().isEmpty()) {
                imgStep6.setVisibility(View.GONE);
            } else {
                imgStep6.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(recipesModel.getImgStep6()).apply(new RequestOptions().override(750, 0)).into(imgStep6);
            }
            ingredients.setText(recipesModel.getIngredient());
            if (recipesModel.getIngredient().isEmpty()) {
                speakCT.setVisibility(View.GONE);

            }
            describe.setText(recipesModel.getDescribe());
            if (recipesModel.getDescribe().isEmpty()) {
                txt1.setVisibility(View.GONE);
                speak1.setVisibility(View.GONE);
                describe.setVisibility(View.GONE);

            }
            describe2.setText(recipesModel.getDescribe2());
            if (recipesModel.getDescribe2().isEmpty()) {
                txt2.setVisibility(View.GONE);
                speak2.setVisibility(View.GONE);
                describe2.setVisibility(View.GONE);
            }
            describe3.setText(recipesModel.getDescribe3());
            if (recipesModel.getDescribe3().isEmpty()) {
                txt3.setVisibility(View.GONE);
                speak3.setVisibility(View.GONE);
                describe3.setVisibility(View.GONE);
            }
            describe4.setText(recipesModel.getDescribe4());
            if (recipesModel.getDescribe4().isEmpty()) {
                txt4.setVisibility(View.GONE);
                speak4.setVisibility(View.GONE);
                describe4.setVisibility(View.GONE);
            }
            describe5.setText(recipesModel.getDescribe5());
            if (recipesModel.getDescribe5().isEmpty()) {
                txt5.setVisibility(View.GONE);
                speak5.setVisibility(View.GONE);
                describe5.setVisibility(View.GONE);
            }
            describe6.setText(recipesModel.getDescribe6());
            if (recipesModel.getDescribe6().isEmpty()) {
                txt6.setVisibility(View.GONE);
                speak6.setVisibility(View.GONE);
                describe6.setVisibility(View.GONE);
            }
        }
    }

    private void QuangCao() {
        adContainerView = findViewById(R.id.framelayout);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id2));
        adView.setAdSize(adSize());
        adContainerView.addView(adView);
        adView.setAdListener(new AdListener() {
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

    private void floatingButton() {
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationFab();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationFab();
                if (auth.getCurrentUser() == null) {
                    startActivity(new Intent(RecipesActivity.this, LoginActivity.class));
                } else {
                    Intent intent = new Intent(RecipesActivity.this, AddNoteActivity.class);
                    startActivity(intent);
                }
            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationFab();
                if (imgTitle.getDrawable() == null) {
                    Toast.makeText(RecipesActivity.this, "Hãy chờ cho dữ liệu được hiện thị đầy đủ !", Toast.LENGTH_SHORT).show();
                } else {
                    Bitmap bitmap = ((BitmapDrawable) imgTitle.getDrawable()).getBitmap();
                    try {
                        String s = name.getText().toString();
                        String shareBody = " Đây là món ăn " + s + " có trong ứng dụng 3 bữa 1 ngày. Bạn hãy tải nó và trải nghiệm ngay nhé ! \n" + "https://play.google.com/store/apps/details?id=com.nhatnguyen.congthucgiadinh&hl=en";
                        File file = new File(getExternalCacheDir(), s + ".png");
                        FileOutputStream fOut = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                        file.setReadable(true, false);
                        Uri uriPhoto = Uri.fromFile(file);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            uriPhoto = FileProvider.getUriForFile(RecipesActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_STREAM, uriPhoto);
                        intent.putExtra(Intent.EXTRA_TEXT, s);
                        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        intent.setType("image/png");
                        startActivity(Intent.createChooser(intent, "Chia sẻ tới bạn bè "));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                R.style.TimePickerTheme,
                onTimeSetListener,
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.MILLISECOND),
                true);
        timePickerDialog.setTitle("Hẹn giờ...");
        timePickerDialog.show();
    }

    private void anhXa() {
        toolbar = findViewById(R.id.toolbar);
        backArrow = findViewById(R.id.backArrow);
        imgFav = findViewById(R.id.imgFav);
        imgSearch = findViewById(R.id.imgSearch);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        cancel = findViewById(R.id.cancel);
        cancelBell = findViewById(R.id.cancelBell);
        txtTimeShow = findViewById(R.id.txtTimeShow);
        linearLayoutCounttime = findViewById(R.id.linearCountTime);
        txtStarttime = findViewById(R.id.txtPLAY);
        auth = FirebaseAuth.getInstance();
        myouTubePlayerView = findViewById(R.id.youtube_player_view);
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab3 = findViewById(R.id.fab3);
        fab4 = findViewById(R.id.fab4);
        fab5 = findViewById(R.id.fab5);
        name = findViewById(R.id.txtName);
        imgTitle = findViewById(R.id.imgTitle);
        imgStep1 = findViewById(R.id.imgStep1);
        imgStep2 = findViewById(R.id.imgStep2);
        imgStep3 = findViewById(R.id.imgStep3);
        imgStep4 = findViewById(R.id.imgStep4);
        imgStep5 = findViewById(R.id.imgStep5);
        imgStep6 = findViewById(R.id.imgStep6);
        txtTimer = findViewById(R.id.txttimer);
        ingredients = findViewById(R.id.txtIngredients);
        describe = findViewById(R.id.txtDescribe);
        describe2 = findViewById(R.id.txtDescribe2);
        describe3 = findViewById(R.id.txtDescribe3);
        describe4 = findViewById(R.id.txtDescribe4);
        describe5 = findViewById(R.id.txtDescribe5);
        describe6 = findViewById(R.id.txtDescribe6);
        //
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        txt5 = findViewById(R.id.txt5);
        txt6 = findViewById(R.id.txt6);
        //
        speakCT = findViewById(R.id.speakCT);
        speakCT_Off = findViewById(R.id.speakCT_Off);
        speak1 = findViewById(R.id.speak1);
        speak1_Off = findViewById(R.id.speak1_Off);
        speak2 = findViewById(R.id.speak2);
        speak2_Off = findViewById(R.id.speak2_Off);
        speak3 = findViewById(R.id.speak3);
        speak3_Off = findViewById(R.id.speak3_Off);
        speak4 = findViewById(R.id.speak4);
        speak4_Off = findViewById(R.id.speak4_Off);
        speak5 = findViewById(R.id.speak5);
        speak5_Off = findViewById(R.id.speak5_Off);
        speak6 = findViewById(R.id.speak6);
        speak6_Off = findViewById(R.id.speak6_Off);
    }

    private void animationFab() {
        if (isOpen) {
            fab1.startAnimation(fabClose);
            fab3.startAnimation(fabClose);
            fab4.startAnimation(fabClose);
            fab5.startAnimation(fabClose);
            fab1.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            fab5.setClickable(false);
            isOpen = false;
        } else {
            fab1.startAnimation(fabOpen);
            fab3.startAnimation(fabOpen);
            fab4.startAnimation(fabOpen);
            fab5.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);
            fab5.setClickable(true);
            isOpen = true;
        }
    }

    private void addToFav() {

        final HashMap<String, Object> carMap = new HashMap<>();
        carMap.put("name", recipesModel.getName());
        carMap.put("image", recipesModel.getImage());
        carMap.put("describe", recipesModel.getDescribe());
        carMap.put("describe2", recipesModel.getDescribe2());
        carMap.put("describe3", recipesModel.getDescribe3());
        carMap.put("describe4", recipesModel.getDescribe4());
        carMap.put("describe5", recipesModel.getDescribe5());
        carMap.put("describe6", recipesModel.getDescribe6());
        carMap.put("imgStep1", recipesModel.getImgStep1());
        carMap.put("imgStep2", recipesModel.getImgStep2());
        carMap.put("imgStep3", recipesModel.getImgStep3());
        carMap.put("imgStep4", recipesModel.getImgStep4());
        carMap.put("imgStep5", recipesModel.getImgStep5());
        carMap.put("imgStep6", recipesModel.getImgStep6());
        carMap.put("ingredient", recipesModel.getIngredient());
        carMap.put("youtube", recipesModel.getYoutube());
        carMap.put("timer", recipesModel.getTimer());
        dbView = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("FavRecipes").child(recipesModel.getName());
        dbView.setValue(carMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RecipesActivity.this, "Đã lưu vào trang yêu thích ", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadBanner() {
        AdRequest adRequest =
                new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize adSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
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

    public class CounterClass extends CountDownTimer {

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onTick(long millisUntilFinished) {
            mTimerRunning = true;
            long millis = millisUntilFinished;
            String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));


            txtTimeShow.setText(hms);
        }

        @Override
        public void onFinish() {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.nhacchuongdemgio);
            mediaPlayer.start();
            cancelBell.setVisibility(View.VISIBLE);
            txtStarttime.setVisibility(View.GONE);
            Drawable[] drawables = txtTimeShow.getCompoundDrawables();
            for (Drawable drawable : drawables) {
                if (drawable != null && drawable instanceof Animatable) {
                    ((Animatable) drawable).stop();
                }
            }
        }
    }

}