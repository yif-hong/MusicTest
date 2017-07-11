package com.example.rhong.musictest.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by rhong on 2017/7/6.
 */

public class MyViewPager extends ViewPager {
    private boolean isTouchingSeekBar;
    private boolean isOpenDrawerLayout;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setSeekBar(boolean isTouchingSeekBar) {
        this.isTouchingSeekBar = isTouchingSeekBar;
    }

    public void judgeIsOpenDrawerLayout(boolean isOpen) {
        isOpenDrawerLayout = isOpen;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (isTouchingSeekBar || isOpenDrawerLayout) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}
