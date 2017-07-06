package com.example.rhong.musictest.view;

import com.example.rhong.musictest.entity.Song;

import java.util.ArrayList;

/**
 * Created by rhong on 2017/7/6.
 */

public interface IView {
    void showData(ArrayList<Song> songs);

    void onFalse();
}
