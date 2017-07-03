package com.example.rhong.musictest;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import layout.MovieFragment;
import layout.MusicFragment;
import layout.PictureFragment;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends FragmentActivity {

    private TextView pictureTextView, movieTextView, musicTextView;
    private ViewPager mViewpager;
    private ImageView cursor;
    private int offset = 0;
    private int position_one;
    private int position_two;

    private int bmpw;
    private int currentIndex = 0;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;
    public Context context;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initTextView();
        initViewPager();
        initImageView();
        initFragment();
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    private void initTextView() {
        pictureTextView = (TextView) findViewById(R.id.picture_text);
        movieTextView = (TextView) findViewById(R.id.movie_text);
        musicTextView = (TextView) findViewById(R.id.music_text);

        pictureTextView.setOnClickListener(new MyClickListener(0));
        movieTextView.setOnClickListener(new MyClickListener(1));
        musicTextView.setOnClickListener(new MyClickListener(2));

    }

    private void initViewPager() {
        mViewpager = (ViewPager) findViewById(R.id.viewPager);
        mViewpager.setAdapter(new MFragmentPagerAdapter(fragmentManager, fragmentArrayList));
        mViewpager.setOffscreenPageLimit(2);
        mViewpager.setCurrentItem(0);
        //将顶部文字恢复默认值
        resetTextViewTextColor();
        pictureTextView.setTextColor(getResources().getColor(R.color.main_top_tab_color_2));
        //设置viewpager页面滑动监听事件
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenW = displayMetrics.widthPixels;

        bmpw = (screenW / 3);
        setBmpw(cursor, bmpw);
        offset=0;

        position_one = (int) (screenW / 3.0);
        position_two = position_one * 2;

    }

    private void initFragment() {
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new PictureFragment());
        fragmentArrayList.add(new MovieFragment());
        fragmentArrayList.add(new MusicFragment());

        fragmentManager = getSupportFragmentManager();
    }


    class MyClickListener implements View.OnClickListener {
        private int index = 0;

        public MyClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View view) {
            mViewpager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


}
