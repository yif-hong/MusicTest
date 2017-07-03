package com.example.rhong.musictest;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import layout.Id3Fragment;

import static com.example.rhong.musictest.R.id.viewPager;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ImageButton imageButtonSet, imageButtonId3, imageButtonList, imageButtonEq;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;
    private MFragmentPagerAdapter mFragmentPagerAdapter;

    private Context context;
    //当前页面卡号
    private int currentIndex = 0;

    //动画图片
    private ImageView cursor;

    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;

    //动画图片宽度
    private int bmpW;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //初始化view pages
        initView();
        initFragment();
        initViewPager();


        //设置监听

    }

    @Override
    protected void onPostResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onPostResume();
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(viewPager);

        //初始化适配器
        mFragmentPagerAdapter = new MFragmentPagerAdapter(fragmentManager, fragmentArrayList);
        //设置Adapter
        mViewPager.setAdapter(mFragmentPagerAdapter);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(1);
        resumeTab();

        mViewPager.setOnPageChangeListener();
    }

    private void initFragment() {
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new Id3Fragment());
        fragmentArrayList.add(new ListFragment());
        fragmentManager = getSupportFragmentManager();

    }

    private void initView() {

        imageButtonSet = (ImageButton) findViewById(R.id.button_bar_set);
        imageButtonId3 = (ImageButton) findViewById(R.id.button_bar_id3);
        imageButtonList = (ImageButton) findViewById(R.id.button_bar_list);
        imageButtonEq = (ImageButton) findViewById(R.id.button_bar_eq);

        imageButtonSet.setOnClickListener(new MyOnClickListener(0));
        imageButtonId3.setOnClickListener(new MyOnClickListener(1));
        imageButtonList.setOnClickListener(new MyOnClickListener(2));
        imageButtonEq.setOnClickListener(new MyOnClickListener(3));

    }

    private void resumeTab() {
        //TODO:将底部tab全部变初始灰色
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            mViewPager.setCurrentItem(index);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Animation animation = null;

            switch (position) {
                case 0:
                    //TODO:切换界面
                    if (currentIndex == 1) {
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        resumeTab();
                    } else if (currentIndex == 2) {

                    }
                    break;
                case 1:
                    //TODO:切换界面
                    break;
                case 2:
                    //TODO:切换界面
                    break;
                case 3:
                    //TODO:切换界面
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
