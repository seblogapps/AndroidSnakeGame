package com.example.stognacci.snakegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Settings extends AppCompatActivity {

    private RelativeLayout settingsLayout;
    private AdView adView;

    private TextView titleLeft;
    private TextView titleMiddle;

    private TextView titleRight;
    private ImageView swipeButtonImageView;
    private ImageView musicButtonImageView;

    private ImageView mainMenuImageView;

    private Animation compileAnimation;

    private SharedPreferences preferences;
    private boolean isMusicOn;

    private boolean isSwipeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_settings);
        settingsLayout = (RelativeLayout) findViewById(R.id.settings_layout);
        preferences = getApplicationContext().getSharedPreferences(GameSettings.SHAREDPREFS_NAME, Context.MODE_PRIVATE);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        //adView.setAdUnitId(GameSettings.MY_AD_UNIT_ID);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
        RelativeLayout.LayoutParams adViewParams = new RelativeLayout.LayoutParams(
                AdView.LayoutParams.WRAP_CONTENT,
                AdView.LayoutParams.WRAP_CONTENT);
        adViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        settingsLayout.addView(adView, adViewParams);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("100B9A3771CFE9E721A3AEB227193F7D").build();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        settingsLayout = (RelativeLayout) findViewById(R.id.settings_layout);
        initTitle();
        initSwipeButton();
        initMusicSwitch();
        initHomeButton();
    }

    private void initSwipeButton() {
        swipeButtonImageView = (ImageView) findViewById(R.id.settings_swipe);

        compileAnimation = AnimationUtils.loadAnimation(Settings.this, R.anim.anim_for_classic_button);
        compileAnimation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                isSwipeOn = preferences.getBoolean(GameSettings.SHAREDPREFS_CONTROLS, true);
                swipeButtonImageView.setImageResource(isSwipeOn ? R.drawable.swipe : R.drawable.buttons);
                swipeButtonImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeButtonImageView.setImageDrawable(null);
                        if (isSwipeOn) {
                            swipeButtonImageView.setImageResource(R.drawable.buttons);
                            isSwipeOn = false;
                        } else {
                            swipeButtonImageView.setImageResource(R.drawable.swipe);
                            isSwipeOn = true;
                        }
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(GameSettings.SHAREDPREFS_CONTROLS, isSwipeOn);
                        editor.apply();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        swipeButtonImageView.startAnimation(compileAnimation);
    }

    private void initMusicSwitch() {
        musicButtonImageView = (ImageView) findViewById(R.id.settings_music);
        compileAnimation = AnimationUtils.loadAnimation(Settings.this, R.anim.anim_for_no_button);
        compileAnimation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                isMusicOn = preferences.getBoolean(GameSettings.SHAREDPREFS_MUSIC, true);
                musicButtonImageView.setImageResource(isMusicOn ? (R.drawable.music_on) : (R.drawable.music_off));
                musicButtonImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        musicButtonImageView.setImageDrawable(null);
                        if (isMusicOn) {
                            isMusicOn = false;
                            musicButtonImageView.setImageResource(R.drawable.music_off);
                        } else {
                            isMusicOn = true;
                            musicButtonImageView.setImageResource(R.drawable.music_on);
                        }
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(GameSettings.SHAREDPREFS_MUSIC, isMusicOn);
                        editor.apply();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        musicButtonImageView.startAnimation(compileAnimation);
    }

    private void initHomeButton() {
        mainMenuImageView = (ImageView) findViewById(R.id.goto_main_menu);
        compileAnimation = AnimationUtils.loadAnimation(Settings.this, R.anim.anim_for_bomb_button);
        compileAnimation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                mainMenuImageView.setImageResource(R.drawable.menu);
                mainMenuImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeButtonImageView.setImageResource(R.drawable.menu_options);
                        musicButtonImageView.setImageResource(R.drawable.menu_options);
                        mainMenuImageView.setImageResource(R.drawable.menu_options);

                        // Reverse the animation
                        Animation animationTopLeft = AnimationUtils.loadAnimation(Settings.this, R.anim.reverse_for_classic_button);
                        animationTopLeft.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationTopRight = AnimationUtils.loadAnimation(Settings.this, R.anim.reverse_for_no_button);
                        animationTopRight.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationBottomLeft = AnimationUtils.loadAnimation(Settings.this, R.anim.reverse_for_settings_button);
                        animationBottomLeft.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationBottomRight = AnimationUtils.loadAnimation(Settings.this, R.anim.reverse_for_bomb_button);
                        animationBottomRight.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationLeft = AnimationUtils.loadAnimation(Settings.this, R.anim.anim_for_title_left);
                        animationLeft.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);

                        Animation animationMiddle = AnimationUtils.loadAnimation(Settings.this, R.anim.anim_for_title_middle);
                        animationMiddle.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);

                        Animation animationRight = AnimationUtils.loadAnimation(Settings.this, R.anim.anim_for_title_right);
                        animationRight.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);

                        swipeButtonImageView.startAnimation(animationTopLeft);
                        musicButtonImageView.startAnimation(animationTopRight);
                        mainMenuImageView.startAnimation(animationBottomRight);
                        titleLeft.startAnimation(animationLeft);
                        titleMiddle.startAnimation(animationMiddle);
                        titleRight.startAnimation(animationRight);

                        Handler myHandler = new Handler();
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentMainMenu = new Intent(Settings.this, MainMenu.class);
                                intentMainMenu.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intentMainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intentMainMenu);
                            }
                        }, GameSettings.START_NEW_ACTIVITY);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mainMenuImageView.startAnimation(compileAnimation);

    }

    private void initTitle() {
        titleLeft = (TextView) findViewById(R.id.settings_title_left);
        titleMiddle = (TextView) findViewById(R.id.settings_title_middle);
        titleRight = (TextView) findViewById(R.id.settings_title_right);

        Animation animationLeft = AnimationUtils.loadAnimation(Settings.this, R.anim.back_anim_for_title_left);
        animationLeft.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
        titleLeft.startAnimation(animationLeft);

        Animation animationMiddle = AnimationUtils.loadAnimation(Settings.this, R.anim.back_anim_for_title_middle);
        animationMiddle.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
        titleMiddle.startAnimation(animationMiddle);

        Animation animationRight = AnimationUtils.loadAnimation(Settings.this, R.anim.back_anim_for_title_right);
        animationRight.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
        titleRight.startAnimation(animationRight);
    }

    @Override
    public void onBackPressed() {
    }
}
