package com.seblogapps.stognacci.snakegame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by stognacci on 21/04/2016.
 */
public class GameUtils {

    static boolean isColliding(ImageView imageView1, ImageView imageView2) {
        Rect imageView1Rect = new Rect();
        Rect imageView2Rect = new Rect();
        imageView1.getHitRect(imageView1Rect);
        imageView2.getHitRect(imageView2Rect);
        return (Rect.intersects(imageView1Rect, imageView2Rect));
    }

    public static float pixelsToDp(final Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float dpToPixel(final Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static void shakeScreen (final Context context, RelativeLayout relativeLayout) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        shake.setDuration(GameSettings.SHAKE_DURATION);
        relativeLayout.setBackgroundResource(R.drawable.background_for_snake);
        relativeLayout.startAnimation(shake);
    }

    public static void fadeAnimation(final Context context, final RelativeLayout relativeLayout, int playerScore) {
        if (playerScore % GameSettings.POINTS_ANIMATION == 0) {
            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            relativeLayout.setBackgroundResource(R.drawable.background_for_snake_change);
            relativeLayout.startAnimation(fadeIn);
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                    relativeLayout.setBackgroundResource(R.drawable.background_for_snake);
                    relativeLayout.startAnimation(fadeOut);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
    }
}
