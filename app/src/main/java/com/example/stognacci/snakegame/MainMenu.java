package com.example.stognacci.snakegame;

import android.content.Intent;
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

public class MainMenu extends AppCompatActivity {

    private RelativeLayout snakeLayout;
    private Animation compileAnim;
    private AdView adView;
    private ImageView classicBtn;
    private ImageView noWallsBtn;
    private ImageView bombBtn;
    private ImageView settingsBtn;

    private TextView titleLeft;
    private TextView titleMiddle;
    private TextView titleRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        snakeLayout = (RelativeLayout) findViewById(R.id.snake_layout);

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

        snakeLayout.addView(adView, adViewParams);
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice("100B9A3771CFE9E721A3AEB227193F7D").build();
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        initClassic();
        initNoWalls();
        initBomb();
        initTitle();
        initSettings();
    }

    private void initClassic() {
        classicBtn = (ImageView) findViewById(R.id.classic);
        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_classic_button);
        compileAnim.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                classicBtn.setImageResource(R.drawable.classic);
                classicBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentClassic = new Intent(MainMenu.this, ClassicSnake.class);
                        intentClassic.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentClassic);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        classicBtn.startAnimation(compileAnim);
    }

    private void initNoWalls() {
        noWallsBtn = (ImageView) findViewById(R.id.no_walls);
        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_no_button);
        compileAnim.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                noWallsBtn.setImageResource(R.drawable.no_walls);
                noWallsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentNoWalls = new Intent(MainMenu.this, NoWallsSnake.class);
                        intentNoWalls.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentNoWalls);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        noWallsBtn.startAnimation(compileAnim);
    }

    private void initBomb() {
        bombBtn = (ImageView) findViewById(R.id.bomb);
        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_bomb_button);
        compileAnim.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bombBtn.setImageResource(R.drawable.bombsnake);
                bombBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent bombSnakeIntent = new Intent(MainMenu.this, BombSnake.class);
                        bombSnakeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(bombSnakeIntent);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        bombBtn.startAnimation(compileAnim);
    }

    private void initTitle() {
        titleLeft = (TextView) findViewById(R.id.snake_left);
        titleMiddle = (TextView) findViewById(R.id.snake_middle);
        titleRight = (TextView) findViewById(R.id.snake_right);

        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.back_anim_for_title_left);
        compileAnim.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
        titleLeft.startAnimation(compileAnim);

        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.back_anim_for_title_middle);
        compileAnim.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
        titleMiddle.startAnimation(compileAnim);

        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.back_anim_for_title_right);
        compileAnim.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
        titleRight.startAnimation(compileAnim);
    }

    private void initSettings() {
        settingsBtn = (ImageView) findViewById(R.id.settings);
        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_settings_button);
        compileAnim.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                settingsBtn.setImageResource(R.drawable.settings);
                settingsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        settingsBtn.setImageResource(R.drawable.menu_options);
                        classicBtn.setImageResource(R.drawable.menu_options);
                        noWallsBtn.setImageResource(R.drawable.menu_options);
                        bombBtn.setImageResource(R.drawable.menu_options);

                        Animation animationCloseClassic = AnimationUtils.loadAnimation(MainMenu.this, R.anim.reverse_for_classic_button);
                        animationCloseClassic.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationCloseNoWalls = AnimationUtils.loadAnimation(MainMenu.this, R.anim.reverse_for_no_button);
                        animationCloseNoWalls.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationCloseBomb = AnimationUtils.loadAnimation(MainMenu.this, R.anim.reverse_for_bomb_button);
                        animationCloseBomb.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationCloseSettings = AnimationUtils.loadAnimation(MainMenu.this, R.anim.reverse_for_settings_button);
                        animationCloseSettings.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationLeft = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_title_left);
                        animationLeft.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);

                        Animation animationMiddle = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_title_middle);
                        animationMiddle.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);

                        Animation animationRight = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_title_right);
                        animationRight.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);

                        classicBtn.startAnimation(animationCloseClassic);
                        noWallsBtn.startAnimation(animationCloseNoWalls);
                        bombBtn.startAnimation(animationCloseBomb);
                        settingsBtn.startAnimation(animationCloseSettings);
                        titleLeft.startAnimation(animationLeft);
                        titleMiddle.startAnimation(animationMiddle);
                        titleRight.startAnimation(animationRight);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent settingsIntent = new Intent(MainMenu.this, Settings.class);
                                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(settingsIntent);

                            }
                        }, GameSettings.START_NEW_ACTIVITY);

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        settingsBtn.startAnimation(compileAnim);
    }
}
