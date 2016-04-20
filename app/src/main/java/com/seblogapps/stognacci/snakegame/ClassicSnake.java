package com.seblogapps.stognacci.snakegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ClassicSnake extends AppCompatActivity {

    private static final String LOG_TAG = ClassicSnake.class.getSimpleName();

    private boolean playMusic;
    private MediaPlayer musicPlayer;

    private RelativeLayout classicSnakeRelativeLayout;
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

    private Handler myHandler;
    private ImageView head;

    private TextView textScore;

    private int speedX = 16;
    private int speedY = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_classic_snake);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        preferences = getApplicationContext().getSharedPreferences(GameSettings.SHAREDPREFS_NAME, Context.MODE_PRIVATE);
        musicOnOff();
        classicSnakeRelativeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
        classicSnakeRelativeLayout.setBackgroundResource(R.drawable.background_for_snake);
        classicSnakeRelativeLayout.setPaddingRelative(GameSettings.LAYOUT_PADDING,
                GameSettings.LAYOUT_PADDING, GameSettings.LAYOUT_PADDING, GameSettings.LAYOUT_PADDING);

        textScore = (TextView) findViewById(R.id.score);

        isInitialized = false;
    }


    private void musicOnOff() {
        playMusic = preferences.getBoolean(GameSettings.SHAREDPREFS_MUSIC, true);
        musicPlayer = MediaPlayer.create(ClassicSnake.this, R.raw.music);

        if (playMusic) {
            musicPlayer.setLooping(true);
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
        musicPlayer.release();
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

    private void shake() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        shake.setDuration(GameSettings.SHAKE_DURATION);
        classicSnakeRelativeLayout.setBackgroundResource(R.drawable.background_for_snake);
        classicSnakeRelativeLayout.startAnimation(shake);
    }

    private void fadeAnimation() {
        if (playerScore % GameSettings.POINTS_ANIMATION == 0) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            classicSnakeRelativeLayout.setBackgroundResource(R.drawable.background_for_snake_change);
            classicSnakeRelativeLayout.startAnimation(fadeIn);
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation fadeOut = AnimationUtils.loadAnimation(ClassicSnake.this, R.anim.fade_out);
                    classicSnakeRelativeLayout.setBackgroundResource(R.drawable.background_for_snake);
                    classicSnakeRelativeLayout.startAnimation(fadeOut);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }

    private void collide() {
        gameOver = true;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(GameSettings.SHAREDPREFS_LASTSCORE, playerScore);
        editor.apply();
        Intent intentScore = new Intent(ClassicSnake.this, ClassicScore.class);
        intentScore.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentScore);
    }

    private void checkBitten() {
        ImageView snakeHead = parts.get(0);
        ImageView snakeTile;// = new ImageView(ClassicSnake.this);

        for (int i = 1; i < parts.size(); i++) {
            snakeTile = parts.get(i);
            if (snakeHead.getX() == snakeTile.getX() &&
                    snakeHead.getY() == snakeTile.getY()) {
                collide();
                break;
            }
        }
    }

    private void addTail() {
        ImageView tailImageView = new ImageView(ClassicSnake.this);
        tailImageView.setImageResource(R.drawable.head);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams((screenWidth * 20) / 450, (screenHeight * 30) / 450);
        tailImageView.setLayoutParams(layoutParams);
        classicSnakeRelativeLayout.addView(tailImageView);
        parts.add(tailImageView);
    }

    private void setNewPoint() {
        Random random = new Random();
        ImageView newFoodPoint = new ImageView(ClassicSnake.this);
        float x = random.nextFloat() * (screenWidth - newFoodPoint.getWidth());
        float y = random.nextFloat() * (screenHeight - newFoodPoint.getHeight());
        newFoodPoint.setImageResource(R.drawable.food);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ((screenWidth * 20) / 450), ((screenHeight * 30) / 450));
        newFoodPoint.setLayoutParams(layoutParams);
        newFoodPoint.setX(x);
        newFoodPoint.setY(y);
        isCollide = false;
        classicSnakeRelativeLayout.addView(newFoodPoint);
        foodPoints.add(foodPoints.size(), newFoodPoint);
    }

    private void setFoodPoints() {
        for (int i = 0; i < GameSettings.FOOD_POINTS; i++) {
            Random random = new Random();
            ImageView foodItem = new ImageView(ClassicSnake.this);
            float x = random.nextFloat() * (screenWidth - foodItem.getWidth());
            float y = random.nextFloat() * (screenHeight - foodItem.getHeight());
            foodItem.setImageResource(R.drawable.food);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ((screenWidth * 20) / 450), ((screenHeight * 30) / 450));
            foodItem.setLayoutParams(layoutParams);
            foodItem.setX(x);
            foodItem.setY(y);
            classicSnakeRelativeLayout.addView(foodItem);
            foodPoints.add(i, foodItem);
        }
    }

    private Rect getDrawableRect(ImageView imageView) {
        float left = imageView.getX();// - (imageView.getDrawable().getIntrinsicWidth() / 2);
        float top = imageView.getY() + (imageView.getDrawable().getIntrinsicHeight() / 2);
        float right = imageView.getX() + (imageView.getDrawable().getIntrinsicWidth() / 1);
        float bottom = imageView.getY() + (imageView.getDrawable().getIntrinsicHeight() / 2);
        Rect drawableRect = new Rect();
        drawableRect.set((int) left, (int) top, (int) right, (int) bottom);
        Log.d(LOG_TAG, "getDrawableRect:           " + drawableRect.flattenToString());
        return drawableRect;
    }

    private void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!gameOver && !isPaused) {
                    try {
                        Thread.sleep(GameSettings.GAME_THREAD);
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
//                                .getHitRect is not returning correct Rect of head object, so I evaluate the containing Rect manually
//                                Rect headRect = new Rect();
//                                head.getHitRect(headRect);
//                                Log.d(LOG_TAG, "Head Rect getHitRect:           " + headRect.flattenToString());
                                float leftHead = head.getX() - (head.getDrawable().getIntrinsicWidth() / 2);
                                float topHead = head.getY() - (head.getDrawable().getIntrinsicHeight() / 2);
                                float rightHead = head.getX() + (head.getDrawable().getIntrinsicWidth() / 2);
                                float bottomHead = head.getY() + (head.getDrawable().getIntrinsicHeight() / 2);

                                for (int i = 0; i < foodPoints.size(); i++) {
                                    if (!isCollide) {
                                        ImageView p = foodPoints.get(i);
//                                        Rect pRect = new Rect();
//                                        p.getHitRect(pRect);
                                        float leftPoint = p.getX() - p.getDrawable().getIntrinsicWidth();
                                        float topPoint = p.getY() - p.getDrawable().getIntrinsicHeight();
                                        float rightPoint = p.getX() + p.getDrawable().getIntrinsicWidth();
                                        float bottomPoint = p.getY() + p.getDrawable().getIntrinsicHeight();
                                        // Player bounding rectangle
                                        //Rect rc1 = new Rect();
                                        Rect headRect = getDrawableRect(head);
                                        //rc1.set((int) leftHead, (int) topHead, (int) rightHead, (int) bottomHead);
                                        // Food bounding rectangle
                                        Rect rc2 = new Rect();
                                        rc2.set((int) leftPoint, (int) topPoint, (int) rightPoint, (int) bottomPoint);

                                        if (Rect.intersects(headRect, rc2)) {
                                            //if (Rect.intersects(headRect, pRect)) {
                                            classicSnakeRelativeLayout.removeView(p);
                                            foodPoints.remove(i);
                                            playerScore++;
                                            isCollide = true;
                                            textScore.setText(getString(R.string.gamescreen_score) + playerScore);
                                            setNewPoint();
                                            addTail();
                                            shake();
                                            fadeAnimation();
                                        }
                                        checkBitten();
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
                                            if (currentPart.getX() + (currentPart.getDrawable().getIntrinsicWidth()) >= screenWidth) {
                                                currentPart.setX(screenWidth - currentPart.getDrawable().getIntrinsicWidth());
                                                collide();
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
                                                collide();
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
                                            if (currentPart.getY() + currentPart.getDrawable().getIntrinsicHeight() >= screenHeight) {
                                                currentPart.setY(screenHeight - currentPart.getDrawable().getIntrinsicHeight());
                                                collide();
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
                                            if (currentPart.getY() + currentPart.getDrawable().getIntrinsicHeight() <= 0) {
                                                currentPart.setY(0 - currentPart.getDrawable().getIntrinsicHeight());
                                                collide();
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
            myHandler = new Handler();
            mGestureDetector = new GestureDetector(null, new SwipeGestureDetector());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ((screenWidth * 20) / 450), ((screenHeight * 30) / 450));
            head = new ImageView(this);
            head.setLayoutParams(layoutParams);
            head.setImageResource(R.drawable.head);
            head.setX((screenWidth / 2) - head.getWidth());
            head.setY((screenHeight / 2) - head.getHeight());
            classicSnakeRelativeLayout.addView(head);

            parts = new ArrayList<ImageView>();
            foodPoints = new ArrayList<ImageView>();
            parts.add(0, head);

            layoutParams.setMargins(GameSettings.LAYOUT_PADDING,
                    GameSettings.LAYOUT_PADDING,
                    GameSettings.LAYOUT_PADDING,
                    GameSettings.LAYOUT_PADDING);

            setFoodPoints();
            buttonsDirectionInit();
            if (hasFocus) {
                isPaused = false;
                update();
            }
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {
    }
}