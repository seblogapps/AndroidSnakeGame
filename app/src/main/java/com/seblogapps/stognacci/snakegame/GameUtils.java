package com.seblogapps.stognacci.snakegame;

import android.graphics.Rect;
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
}
