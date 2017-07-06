package com.example.rhong.musictest.model;

import android.content.Context;

import com.example.rhong.musictest.callback.MySearchCallBack;

/**
 * Created by rhong on 2017/7/6.
 */

public interface ISearchMusic {
    void getSongs(Context context, MySearchCallBack searchCallback);
}
