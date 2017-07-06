package com.example.rhong.musictest.util;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by rhong on 2017/7/5.
 */

public class MusicUtil {
    public static void setImageViewResource(View view, MotionEvent motionEvent, int resId_down, int resID_up) {
        //TODO:设置ImageView的src点击效果
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ((ImageButton) view).setImageResource(resId_down);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            ((ImageButton) view).setImageResource(resID_up);
        }
    }

    public static void selectedImageView(final ImageView imageView, final int resId_down, final int resId_up) {
        //TODO:设置选中的Tab-src点击效果
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setImageResource(resId_down);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setImageResource(resId_up);
                }
                return false;
            }
        });
    }

}
