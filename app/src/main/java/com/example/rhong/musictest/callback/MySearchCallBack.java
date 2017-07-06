package com.example.rhong.musictest.callback;

import com.example.rhong.musictest.entity.Song;

import java.util.ArrayList;

/**
 * Created by rhong on 2017/7/6.
 */

public interface MySearchCallBack {
    void onSuccess(ArrayList<Song> songs);

    void onFalse(String string);
}
