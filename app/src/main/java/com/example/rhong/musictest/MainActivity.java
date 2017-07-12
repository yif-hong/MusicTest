package com.example.rhong.musictest;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.rhong.musictest.adapter.MFragmentPagerAdapter;
import com.example.rhong.musictest.model.OnDraggingListener;
import com.example.rhong.musictest.model.OnSlideBarListener;
import com.example.rhong.musictest.view.MyViewPager;
import com.yinglan.keyboard.HideUtil;

import java.util.ArrayList;

import layout.EqFragment;
import layout.Id3Fragment;
import layout.ListFragment;
import layout.SetFragment;

public class MainActivity extends FragmentActivity implements OnDraggingListener, OnSlideBarListener {

    private static final String TAG = "MainActivity";
    private Fragment listFragment, id3Fragment;
    private MyViewPager mViewPager;
    private RadioGroup radioGroup;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

//        TODO:!-->动态权限申请
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {

            initFragment();
            initViewPager();
        }
        HideUtil.init(this, mViewPager);


    }

    //        设置只允许竖屏显示
    @Override
    protected void onPostResume() {
        Log.d(TAG, "onPostResume: log in");
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onPostResume();
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.viewPager);

        //初始化适配器
        MFragmentPagerAdapter mFragmentPagerAdapter = new MFragmentPagerAdapter(fragmentManager, fragmentArrayList);
        //设置Adapter
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.button_bar_set_up);
                        break;
                    case 1:
                        radioGroup.check(R.id.button_bar_id3);
                        break;
                    case 2:
                        radioGroup.check(R.id.button_bar_list);
                        break;
                    case 3:
                        radioGroup.check(R.id.button_bar_eq);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragment() {
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new SetFragment());
        id3Fragment = new Id3Fragment();
        fragmentArrayList.add(id3Fragment);
        listFragment = new ListFragment();
        fragmentArrayList.add(listFragment);
        fragmentArrayList.add(new EqFragment());
        fragmentManager = getSupportFragmentManager();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initFragment();
                    initViewPager();
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

        radioGroup = findViewById(R.id.button_bar_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.button_bar_set_up:
                        mViewPager.setCurrentItem(0);
                        id3Fragment.onResume();
                        listFragment.onResume();
                        break;
                    case R.id.button_bar_id3:
                        mViewPager.setCurrentItem(1);
                        id3Fragment.onResume();
                        listFragment.onResume();
                        break;
                    case R.id.button_bar_list:
                        mViewPager.setCurrentItem(2);
                        listFragment.onResume();
                        id3Fragment.onResume();
                        break;
                    case R.id.button_bar_eq:
                        mViewPager.setCurrentItem(3);
                        id3Fragment.onResume();
                        listFragment.onResume();
                        break;
                    default:
                        break;
                }
            }
        });

////        获取屏幕分辨率以及宽度
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int screenWindow = displayMetrics.widthPixels;
    }

    @Override
    public void setTouching(boolean isTouching) {
        mViewPager.setSeekBar(isTouching);
    }


    @Override
    public void isSlideBarOpen(boolean isOpen) {
        mViewPager.judgeIsOpenDrawerLayout(isOpen);
    }
}
