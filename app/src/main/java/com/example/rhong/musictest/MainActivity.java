package com.example.rhong.musictest;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rhong.musictest.adapter.MFragmentPagerAdapter;
import com.example.rhong.musictest.view.MyViewPager;

import java.util.ArrayList;

import layout.EqFragment;
import layout.Id3Fragment;
import layout.SetFragment;

import static com.example.rhong.musictest.util.MusicUtil.selectedImageView;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private MyViewPager mViewPager;
    private ImageView imageViewSet, imageViewId3, imageViewList, imageViewEq;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;
    private MFragmentPagerAdapter mFragmentPagerAdapter;
//    private InputMethodManager manager;

    //当前页面卡号
    private int currentIndex = 1;


    //动画图片偏移量
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: get in");
        //初始化view pages

//        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        } else {
//            initView();
//            initFragment();
//        }
        initView();
        initFragment();
        initViewPager();

        checkClickableAndSet();

//        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
//                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        }
//        return super.onTouchEvent(event);
//    }

    @Override
    protected void onPostResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onPostResume();
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.viewPager);

        //初始化适配器
        mFragmentPagerAdapter = new MFragmentPagerAdapter(fragmentManager, fragmentArrayList);
        //设置Adapter
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(1);

        resumeTab();
        imageViewId3.setImageResource(R.drawable.buttonbar_icon_id3_f);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initFragment();
                } else {
                    Toast.makeText(this, "权限拒绝将无法使用", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void initView() {

        imageViewSet = findViewById(R.id.button_bar_set);
        imageViewId3 = findViewById(R.id.button_bar_id3);
        imageViewList = findViewById(R.id.button_bar_list);
        imageViewEq = findViewById(R.id.button_bar_eq);


        imageViewSet.setClickable(false);


        imageViewSet.setOnClickListener(new MyOnClickListener(0));
        imageViewId3.setOnClickListener(new MyOnClickListener(1));
        imageViewList.setOnClickListener(new MyOnClickListener(2));
        imageViewEq.setOnClickListener(new MyOnClickListener(3));


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWindow = displayMetrics.widthPixels;
        offset = 0;
        position_one = (int) (screenWindow / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;

    }

    private void checkClickableAndSet() {
        //检查ImageView是否能够点击并设置src，其中background由selector设置
        if (!imageViewSet.isClickable()) {
            imageViewSet.setImageResource(R.drawable.buttonbar_icon_setup_d);
        }
        if (!imageViewId3.isClickable()) {
            imageViewId3.setImageResource(R.drawable.buttonbar_icon_setup_d);
        }
        if (!imageViewList.isClickable()) {
            imageViewList.setImageResource(R.drawable.buttonbar_icon_setup_d);
        }
        if (!imageViewEq.isClickable()) {
            imageViewEq.setImageResource(R.drawable.buttonbar_icon_setup_d);
        }
    }


    private void resumeTab() {
        //TODO:将底部tab全部变初始灰色
        imageViewSet.setImageResource(R.drawable.buttonbar_icon_setup_n);
        imageViewId3.setImageResource(R.drawable.buttonbar_icon_id3_n);
        imageViewList.setImageResource(R.drawable.buttonbar_icon_list_n);
        imageViewEq.setImageResource(R.drawable.buttonbar_icon_eq_n);
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
            resumeTab();
            switch (index) {
                case 0:
                    selectedImageView(imageViewSet, R.drawable.buttonbar_icon_setup_n, R.drawable.buttonbar_icon_setup_f);
                    break;
                case 1:
                    selectedImageView(imageViewId3, R.drawable.buttonbar_icon_id3_n, R.drawable.buttonbar_icon_id3_f);
                    break;
                case 2:
                    selectedImageView(imageViewList, R.drawable.buttonbar_icon_list_n, R.drawable.buttonbar_icon_list_f);
                    break;
                case 3:
                    selectedImageView(imageViewEq, R.drawable.buttonbar_icon_eq_n, R.drawable.buttonbar_icon_eq_f);
                    break;
                default:
                    break;
            }
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
                //实现了1234页面切换,以及动画
                case 0:
                    //TODO:切换界面
                    if (currentIndex == 1) {
                        animation = new TranslateAnimation(position_one, offset, 0, 0);
                    } else if (currentIndex == 2) {
                        animation = new TranslateAnimation(position_two, offset, 0, 0);
                    } else if (currentIndex == 3) {
                        animation = new TranslateAnimation(position_three, offset, 0, 0);
                    }
                    resumeTab();
                    selectedImageView(imageViewSet, R.drawable.buttonbar_icon_setup_n, R.drawable.buttonbar_icon_setup_f);
                    break;
                case 1:
                    //TODO:切换界面
                    if (currentIndex == 0) {
                        animation = new TranslateAnimation(offset, position_one, 0, 0);
                    } else if (currentIndex == 2) {
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                    } else if (currentIndex == 3) {
                        animation = new TranslateAnimation(position_three, position_one, 0, 0);
                    }
                    resumeTab();
                    selectedImageView(imageViewId3, R.drawable.buttonbar_icon_id3_n, R.drawable.buttonbar_icon_id3_f);
                    break;
                case 2:
                    //TODO:切换界面
                    Log.d(TAG, "onPageSelected: 2");
                    if (currentIndex == 0) {
                        animation = new TranslateAnimation(offset, position_two, 0, 0);
                    } else if (currentIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                    } else if (currentIndex == 3) {
                        animation = new TranslateAnimation(position_three, position_two, 0, 0);
                    }
                    resumeTab();
                    selectedImageView(imageViewList, R.drawable.buttonbar_icon_list_n, R.drawable.buttonbar_icon_list_f);
                    break;
                case 3:
                    //TODO:切换界面
                    if (currentIndex == 0) {
                        animation = new TranslateAnimation(offset, position_three, 0, 0);
                    } else if (currentIndex == 1) {
                        animation = new TranslateAnimation(position_one, position_three, 0, 0);
                    } else if (currentIndex == 2) {
                        animation = new TranslateAnimation(position_two, position_three, 0, 0);
                    }
                    resumeTab();
                    selectedImageView(imageViewEq, R.drawable.buttonbar_icon_eq_n, R.drawable.buttonbar_icon_eq_f);
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
