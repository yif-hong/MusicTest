package com.example.rhong.musictest.presenter;

import android.content.Context;
import android.util.Log;

import com.example.rhong.musictest.callback.MySearchCallBack;
import com.example.rhong.musictest.entity.Song;
import com.example.rhong.musictest.model.ISearchMusic;
import com.example.rhong.musictest.model.SearchAllSongs;
import com.example.rhong.musictest.view.IView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by rhong on 2017/7/6.
 */

public class SearchSongsPresenter implements ISearchPresenter {
    private IView iView;
    private ISearchMusic searchMusic;

    public SearchSongsPresenter(IView iView) {
        this.iView = iView;
        searchMusic = new SearchAllSongs();
    }

    @Override
    public void getSongs(Context context) {
        searchMusic.getSongs(context, new MySearchCallBack() {
            @Override
            public void onSuccess(ArrayList<Song> songs) {
                iView.showData(songs);
                Log.d(TAG, "onSuccess: " + songs.toString());
            }

            @Override
            public void onFalse(String string) {
                iView.onFalse();
                Log.d(TAG, "onFalse:  " + string);
            }
        });

    }
}
