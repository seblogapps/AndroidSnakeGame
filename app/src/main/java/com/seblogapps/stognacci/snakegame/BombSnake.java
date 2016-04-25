package com.seblogapps.stognacci.snakegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class BombSnake extends AppCompatActivity {

    private static final String LOG_TAG = BombSnake.class.getSimpleName();

    private boolean playMusic;
    private MediaPlayer musicPlayer;

    private RelativeLayout bombSnakeRelativeLayout;
    private boolean isInitialized;

    private GestureDetector mGestureDetector;
    private boolean isPaused;

    private boolean isGoingLeft = false;
    private boolean isGoingRight = false;
    private boolean isGoingUp = false;
    private boolean isGoingDown = false;

    private boolean clickLeft = false;
    private boolean clickRight = false;
    private boolean clickUp = false;
    private boolean clickDown = false;

    private ImageView btnLeft, btnRight, btnUp, btnDown;

    private boolean useSwipeControls;

    private int playerScore;

    private boolean gameOver = false;

    private SharedPreferences preferences;

    // Snake parts of the body
    private ArrayList<ImageView> parts;

    private int screenHeight, screenWidth;

    // Game food points
    private ArrayList<ImageView> foodPoints;
    private boolean isCollide = false;

    private ArrayList<ImageView> bombs;

    private Handler myHandler;
    private ImageView head;

    private TextView textScore;

    private int speedX;
    private int speedY;

    private SoundPool mSoundPool;
    private int soundPopId;
    private int soundCrashId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bomb_snake);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        preferences = getApplicationContext().getSharedPreferences(GameSettings.SHAREDPREFS_NAME, Context.MODE_PRIVATE);
        initializeSoundResources();
        bombSnakeRelativeLayout = (RelativeLayout) findViewById(R.id.bomb_snake_layout);
        bombSnakeRelativeLayout.setBackgroundResource(R.drawable.background_for_snake);
        bombSnakeRelativeLayout.setPaddingRelative(GameSettings.LAYOUT_PADDING,
                GameSettings.LAYOUT_PADDING, GameSettings.LAYOUT_PADDING, GameSettings.LAYOUT_PADDING);

        textScore = (TextView) findViewById(R.id.score);

        isInitialized = false;
    }

    private void initializeSoundResources() {
        playMusic = preferences.getBoolean(GameSettings.SHAREDPREFS_MUSIC, true);
        musicPlayer = MediaPlayer.create(BombSnake.this, R.raw.bombmusic);
        if (playMusic) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .build();
                mSoundPool = new SoundPool.Builder()
                        .setMaxStreams(3)
                        .setAudioAttributes(audioAttributes)
                        .build();
            } else {
                mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 1);
            }
            soundPopId = mSoundPool.load(this, R.raw.blop, 1);
            soundCrashId = mSoundPool.load(this, R.raw.crash, 1);

            musicPlayer.setLooping(true);
            musicPlayer.setVolume(0.6f, 0.6f);
            musicPlayer.start();
        } else {
            musicPlayer.stop();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector != null && mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        if (playMusic) {
            musicPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playMusic) {
            musicPlayer.release();
            mSoundPool.release();
            mSoundPool = null;
        }
    }

    private void onSwipeRight() {
        if (!isGoingRight && !isGoingLeft) {
            isGoingRight = true;
            isGoingLeft = false;
            isGoingDown = false;
            isGoingUp = false;
        }
    }

    private void onSwipeLeft() {
        if (!isGoingRight && !isGoingLeft) {
            isGoingRight = false;
            isGoingLeft = true;
            isGoingDown = false;
            isGoingUp = false;
        }
    }

    private void onSwipeDown() {
        if (!isGoingDown && !isGoingUp) {
            isGoingRight = false;
            isGoingLeft = false;
            isGoingDown = true;
            isGoingUp = false;
        }
    }

    private void onSwipeUp() {
        if (!isGoingDown && !isGoingUp) {
            isGoingRight = false;
            isGoingLeft = false;
            isGoingDown = false;
            isGoingUp = true;
        }
    }

    private void onClickRight() {
        if (!clickRight && !clickLeft) {
            clickRight = true;
            clickLeft = false;
            clickDown = false;
            clickUp = false;
        }
    }

    private void onClickLeft() {
        if (!clickRight && !clickLeft) {
            clickRight = false;
            clickLeft = true;
            clickDown = false;
            clickUp = false;
        }
    }

    private void onClickDown() {
        if (!clickUp && !clickDown) {
            clickRight = false;
            clickLeft = false;
            clickDown = true;
            clickUp = false;
        }
    }

    private void onClickUp() {
        if (!clickUp && !clickDown) {
            clickRight = false;
            clickLeft = false;
            clickDown = false;
            clickUp = true;
        }
    }

    private void buttonsDirectionInit() {
        btnRight = (ImageView) findViewById(R.id.btn_right);
        btnLeft = (ImageView) findViewById(R.id.btn_left);
        btnDown = (ImageView) findViewById(R.id.btn_down);
        btnUp = (ImageView) findViewById(R.id.btn_up);

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRight();
            }
        });
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLeft();
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDown();
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUp();
            }
        });

        useSwipeControls = preferences.getBoolean(GameSettings.SHAREDPREFS_CONTROLS, true);
        if (!useSwipeControls) {
            btnRight.setVisibility(View.VISIBLE);
            btnLeft.setVisibility(View.VISIBLE);
            btnDown.setVisibility(View.VISIBLE);
            btnUp.setVisibility(View.VISIBLE);
        } else {
            btnRight.setVisibility(View.INVISIBLE);
            btnLeft.setVisibility(View.INVISIBLE);
            btnDown.setVisibility(View.INVISIBLE);
            btnUp.setVisibility(View.INVISIBLE);
        }
    }

    private void checkBitten() {
        ImageView snakeHead = parts.get(0);
        ImageView snakeTile;

        for (int i = 1; i < parts.size(); i++) {
            snakeTile = parts.get(i);
            if (snakeHead.getX() == snakeTile.getX() &&
                    snakeHead.getY() == snakeTile.getY()) {
                gameOver();
                break;
            }
        }
    }

    private void gameOver() {
        gameOver = true;
        if (playMusic) {
            mSoundPool.play(soundCrashId, 1.0f, 1.0f, 1, 0, 1);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(GameSettings.SHAREDPREFS_LASTSCORE, playerScore);
        editor.apply();
        Intent intentScore = new Intent(BombSnake.this, BombScore.class);
        intentScore.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentScore);
    }

    private void addTail() {
        ImageView tailImageView = new ImageView(BombSnake.this);
        tailImageView.setImageResource(R.drawable.head);
        bombSnakeRelativeLayout.addView(tailImageView);
        parts.add(tailImageView);
    }

    private void setNewPoint(boolean isPopSoundEnabled) {
        Random random = new Random();
        ImageView newFoodPoint = new ImageView(BombSnake.this);
        float x = random.nextFloat() * (screenWidth - newFoodPoint.getWidth());
        float y = random.nextFloat() * (screenHeight - newFoodPoint.getHeight());
        newFoodPoint.setImageResource(R.drawable.food);
        newFoodPoint.setX(x);
        newFoodPoint.setY(y);
        isCollide = false;
        bombSnakeRelativeLayout.addView(newFoodPoint);
        foodPoints.add(foodPoints.size(), newFoodPoint);
        if (playMusic && isPopSoundEnabled) {
            mSoundPool.play(soundPopId, 1, 1, 1, 0, 1);
        }
    }

    private void setFoodPoints(int numPoints) {
        for (int i = 0; i < numPoints; i++) {
            setNewPoint(false);
        }
    }

    private void setBombs() {
        Random random = new Random();
        for (int i = 0; i < GameSettings.NUMBER_BOMBS; i++) {
            ImageView bomb = new ImageView(this);
            float x = (random.nextFloat() * screenWidth - bomb.getWidth());
            float y = (random.nextFloat() * screenHeight - bomb.getHeight());
            bomb.setImageResource(R.drawable.poison);
            bomb.setX(x);
            bomb.setY(y);
            bombSnakeRelativeLayout.addView(bomb);
            bombs.add(i, bomb);
        }
    }

    private void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!gameOver && !isPaused) {
                    try {
                        Thread.sleep(GameSettings.GAME_THREAD_BOMB);
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < foodPoints.size(); i++) {
                                    if (!isCollide) {
                                        ImageView p = foodPoints.get(i);
                                        if (GameUtils.isColliding(head, p)) {
                                            bombSnakeRelativeLayout.removeView(p);
                                            foodPoints.remove(i);
                                            playerScore++;
                                            isCollide = true;
                                            textScore.setText(getString(R.string.player_score, playerScore));
                                            speedX++;
                                            speedY++;
                                            setNewPoint(true);
                                            addTail();
                                            GameUtils.shakeScreen(BombSnake.this, bombSnakeRelativeLayout);
                                            GameUtils.fadeAnimation(BombSnake.this, bombSnakeRelativeLayout, playerScore);
                                        }
                                        checkBitten();
                                    }
                                }
                                for (int i = 0; i < bombs.size(); i++) {
                                    ImageView bomb = bombs.get(i);
                                    if (GameUtils.isColliding(head, bomb)) {
                                        if (!isCollide) {
                                            isCollide = true;
                                            gameOver();
                                        }
                                    }

                                }
                                isCollide = false;
                                if (isGoingRight || clickRight) {
                                    for (int i = parts.size() - 1; i >= 0; i--) {
                                        ImageView currentPart = parts.get(i);
                                        // All body parts except head
                                        if (i > 0) {
                                            ImageView previousPart = parts.get(i - 1);
                                            currentPart.setX(previousPart.getX());
                                            currentPart.setY(previousPart.getY());
                                        } else { // Head
                                            currentPart.setX(currentPart.getX() + speedX);
                                            if (currentPart.getX() + currentPart.getWidth() >= screenWidth) {
                                                currentPart.setX(screenWidth - currentPart.getWidth());
                                                gameOver();
                                            }
                                        }
                                    }
                                } else if (isGoingLeft || clickLeft) {
                                    for (int i = parts.size() - 1; i >= 0; i--) {
                                        ImageView currentPart = parts.get(i);
                                        // All body parts except head
                                        if (i > 0) {
                                            ImageView previousPart = parts.get(i - 1);
                                            currentPart.setX(previousPart.getX());
                                            currentPart.setY(previousPart.getY());
                                        } else { // Head
                                            currentPart.setX(currentPart.getX() - speedX);
                                            if (currentPart.getX() <= 0) {
                                                currentPart.setX(0);
                                                gameOver();
                                            }
                                        }
                                    }
                                } else if (isGoingDown || clickDown) {
                                    for (int i = parts.size() - 1; i >= 0; i--) {
                                        ImageView currentPart = parts.get(i);
                                        // All body parts except head
                                        if (i > 0) {
                                            ImageView previousPart = parts.get(i - 1);
                                            currentPart.setX(previousPart.getX());
                                            currentPart.setY(previousPart.getY());
                                        } else { // Head
                                            currentPart.setY(currentPart.getY() + speedY);
                                            if (currentPart.getY() + currentPart.getHeight() >= screenHeight) {
                                                currentPart.setY(screenHeight - currentPart.getHeight());
                                                gameOver();
                                            }
                                        }
                                    }
                                } else if (isGoingUp || clickUp) {
                                    for (int i = parts.size() - 1; i >= 0; i--) {
                                        ImageView currentPart = parts.get(i);
                                        // All body parts except head
                                        if (i > 0) {
                                            ImageView previousPart = parts.get(i - 1);
                                            currentPart.setX(previousPart.getX());
                                            currentPart.setY(previousPart.getY());
                                        } else { // Head
                                            currentPart.setY(currentPart.getY() - speedY);
                                            if (currentPart.getY() <= 0) {
                                                currentPart.setY(0);
                                                gameOver();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }

    public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            if (useSwipeControls) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    // Horizontal swipe
                    if (Math.abs(diffX) > GameSettings.SWIPE_THRESHOLD
                            && Math.abs(velocityX) > GameSettings.SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                // Vertical swipe
                else if (Math.abs(diffY) > GameSettings.SWIPE_THRESHOLD
                        && Math.abs(velocityY) > GameSettings.SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeDown();
                    } else {
                        onSwipeUp();
                    }
                    result = true;
                }
            }
            return result;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!isInitialized) {
            isInitialized = true;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
            speedX = (int) GameUtils.dpToPixel(BombSnake.this, GameSettings.initial_speed);
            speedY = (int) GameUtils.dpToPixel(BombSnake.this, GameSettings.initial_speed);
            myHandler = new Handler();
            mGestureDetector = new GestureDetector(null, new SwipeGestureDetector());
            head = new ImageView(this);
            head.setImageResource(R.drawable.head);
            head.requestLayout();
            head.setX((screenWidth / 2) - head.getWidth());
            head.setY((screenHeight / 2) - head.getHeight());
            bombSnakeRelativeLayout.addView(head);

            parts = new ArrayList<ImageView>();
            foodPoints = new ArrayList<ImageView>();
            parts.add(0, head);

            setFoodPoints(GameSettings.FOOD_POINTS);

            bombs = new ArrayList<ImageView>();
            setBombs();

            buttonsDirectionInit();
        }
        if (isInitialized && hasFocus) {
            isPaused = false;
            if (playMusic) {
                musicPlayer.start();
            }
            update();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {
    }
}
