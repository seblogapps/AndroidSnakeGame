package com.seblogapps.stognacci.snakegame;

/**
 * Created by stognacci on 11/04/2016.
 */
public final class GameSettings {

    public static final int FOOD_POINTS = 3;

    public static final int NUMBER_POINTS = 20;

    public static final int POINTS_BOMB_ANIMATION = 15;

    public static final int POINTS_ANIMATION = 10;

    public static final int NUMBER_BOMBS = 5;

    public static float initial_speed = 5f;

    public static final String MY_AD_UNIT_ID = "TBA";

    // Flash animation for the app
    public static final int ANIMATION_OPEN_BUTTON_DURATION = 400;
    public static final int ANIMATION_CLOSE_BUTTON_DURATION = 400;
    public static final int ANIMATION_SHOW_HOME_BUTTON_DURATION = 500;
    public static final int ANIMATION_HIDE_HOME_BUTTON_DURATION = 500;
    public static final int ANIMATION_SHOW_TITLE_DURATION = 400;
    public static final int ANIMATION_HIDE_TITLE_DURATION = 400;

    public static final int SHAKE_DURATION = 500;

    public static final int GAME_THREAD = 80;

    public static final int GAME_THREAD_BOMB = 50;

    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 100;
    public static final int LAYOUT_PADDING = 0;


    public static final int START_NEW_ACTIVITY = 200;

    // Shared preferences
    public static final String SHAREDPREFS_NAME = "SnakeSharedPreferences";
    public static final String SHAREDPREFS_MUSIC = "MusicEnabled";
    public static final String SHAREDPREFS_CONTROLS = "SwipeControlsEnabled";
    public static final String SHAREDPREFS_LASTSCORE = "Score";
    public static final String SHAREDPREFS_HIGHSCORE_CLASSIC = "HighScoreClassic";
    public static final String SHAREDPREFS_HIGHSCORE_NOWALLS = "HighScoreNoWalls";
    public static final String SHAREDPREFS_HIGHSCORE_BOMB = "HighScoreBomb";
}
