package com.example.rhong.musictest;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import layout.EqFragment;
import layout.Id3Fragment;
import layout.SetFragment;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private ImageButton imageButtonSet, imageButtonId3, imageButtonList, imageButtonEq;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;
    private MFragmentPagerAdapter mFragmentPagerAdapter;
    private Context context;
    //当前页面卡号
    private int currentIndex = 1;

    //动画图片
    private ImageView cursor;

    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;

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

    }

    @Override
    protected void onPostResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onPostResume();
    }

    private void initViewPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        //初始化适配器
        mFragmentPagerAdapter = new MFragmentPagerAdapter(fragmentManager, fragmentArrayList);
        //设置Adapter
        mViewPager.setAdapter(mFragmentPagerAdapter);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(1);
        resumeTab();

        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initFragment() {
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new SetFragment());
        fragmentArrayList.add(new Id3Fragment());
        fragmentArrayList.add(new layout.ListFragment());
        fragmentArrayList.add(new EqFragment());
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

        //光标动画
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWindow = displayMetrics.widthPixels;
        bmpW = (screenWindow / 4);
        setBmpW(cursor, bmpW);
        offset = 0;
        position_one = (int) (screenWindow / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;


    }

    private void setBmpW(ImageView imageView, int mWidth) {
        ViewGroup.LayoutParams params;
        params = imageView.getLayoutParams();
        params.width = mWidth;
        imageView.setLayoutParams(params);
    }

    private void resumeTab() {
        //TODO:将底部tab全部变初始灰色
    }

    private void selectedTab(ImageButton imageButton) {
        //TODO:设置选中的tab颜色变化
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: current item: " + index);
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
                //实现了1234页面切换
                case 0:
                    //TODO:切换界面
                    if (currentIndex == 1) {
                        animation = new TranslateAnimation(position_one, offset, 0, 0);
                        Toast.makeText(context, "id3 to set,1-0", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonSet);
                    } else if (currentIndex == 2) {
                        animation = new TranslateAnimation(position_two, offset, 0, 0);
                        Toast.makeText(context, "list to set,2-0", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonSet);
                    } else if (currentIndex == 3) {
                        animation = new TranslateAnimation(position_three, offset, 0, 0);
                        Toast.makeText(context, "eq to set,3-0", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonSet);
                    }
                    break;
                case 1:
                    //TODO:切换界面
                    if (currentIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                        Toast.makeText(context, "set to id3,0-1", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonId3);
                    } else if (currentIndex == 2) {
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        Toast.makeText(context, "list to id3,2-1", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonId3);
                    } else if (currentIndex == 3) {
                        animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        Toast.makeText(context, "eq to id3,3-1", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonId3);
                    }
                    break;
                case 2:
                    //TODO:切换界面
                    Log.d(TAG, "onPageSelected: 2");
                    if (currentIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                        Toast.makeText(context, "set to list,0-2", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonList);
                    } else if (currentIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        Toast.makeText(context, "id3 to list,1-2", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonList);
                    } else if (currentIndex == 3) {
                        animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        Toast.makeText(context, "eq to list,1-2", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonList);
                    }
                    break;
                case 3:
                    //TODO:切换界面
                    if (currentIndex == 0) {
                        animation = new TranslateAnimation(offset, position_three, 0, 0);
                        Toast.makeText(context, "set to eq,0-3", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonEq);
                    } else if (currentIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        Toast.makeText(context, "id3 to eq,1-3", Toast.LENGTH_SHORT).show();
                        resumeTab();
                        selectedTab(imageButtonEq);
                    } else if (currentIndex == 2) {
                        animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        Toast.makeText(context, "list to eq,2-3", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
            currentIndex = position;
            if (animation != null) {
                animation.setFillAfter(true);
            }
            if (animation != null) {
                animation.setDuration(300);
            }
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
