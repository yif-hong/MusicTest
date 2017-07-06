package com.example.rhong.musictest.model;

import android.os.Message;

import com.example.rhong.musictest.entity.Song;

import java.util.ArrayList;

/**
 * Created by rhong on 2017/7/6.
 */

public interface IPlayer {
    void callPlay();

    void callPause();

    void callPrevious();

    void callNext();

    void callPlay(int position);

    void callCurrentProgress(int progress);

    boolean callIsPlaying();

    int callMusicIndex();

    int callGetDuration();

    int callGetCurrentTime();

    void setData(ArrayList<Song> dataList);

    boolean callIsCollect();

    Message callMusicSub();

    void setPlayMode(int playMode);

    void callPlaySong(ArrayList<Song> subData, int subPosition);
}
