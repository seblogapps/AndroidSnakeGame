package com.seblogapps.stognacci.snakegame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.widget.ImageView;

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

    public static float pixelsToDp(final Context context, float px){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float dpToPixel(final Context context, float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
