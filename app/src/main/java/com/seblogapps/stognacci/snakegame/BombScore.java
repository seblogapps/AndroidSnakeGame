package com.seblogapps.stognacci.snakegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class BombScore extends AppCompatActivity {

    private TextView scoreTextView;
    private TextView highScoreTextView;
    private ImageView playAgainImageView;
    private ImageView mainMenuImageView;

    private Animation animation;
    private SharedPreferences preferences;

    private TextView gameOverTitleLeft;
    private TextView gameOverTitleMiddle;
    private TextView gameOverTitleRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bomb_score);
        preferences = getApplicationContext().getSharedPreferences(GameSettings.SHAREDPREFS_NAME, Context.MODE_PRIVATE);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initTitle();
        initScore();
        initHighScore();
        initPlayAgain();
        initMainMenu();
    }

    private void initScore() {
        scoreTextView = (TextView) findViewById(R.id.player_score);
        animation = AnimationUtils.loadAnimation(BombScore.this, R.anim.anim_for_classic_button);
        animation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int playerScore = preferences.getInt(GameSettings.SHAREDPREFS_LASTSCORE, 0);
                scoreTextView.setText("Score: " + String.valueOf(playerScore));
                scoreTextView.setTextColor(Color.WHITE);
                scoreTextView.setGravity(Gravity.CENTER);
                scoreTextView.setBackgroundResource(R.drawable.menu_options);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scoreTextView.startAnimation(animation);
    }

    private void initHighScore() {
        highScoreTextView = (TextView) findViewById(R.id.mode_high_score);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_for_no_button);
        animation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setHighScore();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        highScoreTextView.startAnimation(animation);
    }

    private void setHighScore() {

        int highScore = preferences.getInt(GameSettings.SHAREDPREFS_HIGHSCORE_BOMB, 0);
        int lastScore = preferences.getInt(GameSettings.SHAREDPREFS_LASTSCORE, 0);

        if (lastScore > highScore) {
            highScore = lastScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(GameSettings.SHAREDPREFS_HIGHSCORE_BOMB, lastScore);
            editor.apply();
        }
        highScoreTextView.setText("High: " + String.valueOf(highScore));
        highScoreTextView.setTextColor(Color.WHITE);
        highScoreTextView.setGravity(Gravity.CENTER);
        highScoreTextView.setBackgroundResource(R.drawable.menu_options);

    }

    private void initPlayAgain() {
        playAgainImageView = (ImageView) findViewById(R.id.play_again);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_for_settings_button);
        animation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playAgainImageView.setImageResource(R.drawable.again);
                playAgainImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentBombMode = new Intent(BombScore.this, BombSnake.class);
                        intentBombMode.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intentBombMode.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentBombMode);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        playAgainImageView.startAnimation(animation);
    }


    private void initMainMenu() {
        mainMenuImageView = (ImageView) findViewById(R.id.goto_main_menu);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_for_bomb_button);
        animation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                mainMenuImageView.setImageResource(R.drawable.menu);
                mainMenuImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        scoreTextView.setBackgroundResource(R.drawable.menu_options);
                        scoreTextView.setText("");
                        scoreTextView.setTextColor(Color.BLACK);
                        highScoreTextView.setBackgroundResource(R.drawable.menu_options);
                        highScoreTextView.setText("");
                        highScoreTextView.setTextColor(Color.BLACK);
                        playAgainImageView.setImageResource(R.drawable.menu_options);
                        mainMenuImageView.setImageResource(R.drawable.menu_options);

                        //Reverse the animation
                        Animation animationTopLeft = AnimationUtils.loadAnimation(BombScore.this, R.anim.reverse_for_classic_button);
                        animationTopLeft.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationTopRight = AnimationUtils.loadAnimation(BombScore.this, R.anim.reverse_for_no_button);
                        animationTopRight.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationBottomLeft = AnimationUtils.loadAnimation(BombScore.this, R.anim.reverse_for_settings_button);
                        animationBottomLeft.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationBottomRight = AnimationUtils.loadAnimation(BombScore.this, R.anim.reverse_for_bomb_button );
                        animationBottomRight.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationLeft = AnimationUtils.loadAnimation(BombScore.this, R.anim.anim_for_title_left);
                        animationLeft.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);

                        Animation animationMiddle = AnimationUtils.loadAnimation(BombScore.this, R.anim.anim_for_title_middle);
                        animationMiddle.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);

                        Animation animationRight = AnimationUtils.loadAnimation(BombScore.this, R.anim.anim_for_title_right);
                        animationRight.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);

                        scoreTextView.startAnimation(animationTopLeft);
                        highScoreTextView.startAnimation(animationTopRight);
                        playAgainImageView.startAnimation(animationBottomLeft);
                        mainMenuImageView.startAnimation(animationBottomRight);
                        gameOverTitleLeft.startAnimation(animationLeft);
                        gameOverTitleMiddle.startAnimation(animationMiddle);
                        gameOverTitleRight.startAnimation(animationRight);

                        Handler myHandler = new Handler();
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentMainMenu = new Intent(BombScore.this, MainMenu.class);
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
        mainMenuImageView.startAnimation(animation);
    }

    private void initTitle() {
        gameOverTitleLeft = (TextView) findViewById(R.id.gameover_left);
        gameOverTitleMiddle = (TextView) findViewById(R.id.gameover_middle);
        gameOverTitleRight = (TextView) findViewById(R.id.gameover_right);

        Animation animationLeft = AnimationUtils.loadAnimation(BombScore.this, R.anim.back_anim_for_title_left);
        animationLeft.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
        gameOverTitleLeft.startAnimation(animationLeft);

        Animation animationMiddle = AnimationUtils.loadAnimation(BombScore.this, R.anim.back_anim_for_title_middle);
        animationMiddle.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
        gameOverTitleMiddle.startAnimation(animationMiddle);

        Animation animationRight = AnimationUtils.loadAnimation(BombScore.this, R.anim.back_anim_for_title_right);
        animationRight.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
        gameOverTitleRight.startAnimation(animationRight);
    }

    @Override
    public void onBackPressed() {
    }
}