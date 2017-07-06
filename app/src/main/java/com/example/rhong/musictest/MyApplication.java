package com.example.rhong.musictest;

import android.app.Application;
import android.media.MediaPlayer;

import com.example.rhong.musictest.entity.Song;

import java.util.ArrayList;

/**
 * Created by rhong on 2017/7/6.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;
    private static MediaPlayer mediaPlayer;
    private static ArrayList<Song> collectSongList;

    public static MyApplication getMyApplication() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    public static MediaPlayer getMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        return mediaPlayer;
    }

    public static ArrayList<Song> getCollectSongList() {
        return collectSongList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        if (collectSongList == null) {
            collectSongList = new ArrayList<>();
        }
    }
}
