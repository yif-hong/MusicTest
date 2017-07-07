package com.example.rhong.musictest.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.rhong.musictest.callback.MySearchCallBack;
import com.example.rhong.musictest.entity.Song;

import java.util.ArrayList;

/**
 * Created by rhong on 2017/7/6.
 */

public class SearchAllSongs implements ISearchMusic {

    private static final String TAG = "SearchAllSongs";
    private ArrayList<Song> songArrayList;

    @Override
    public void getSongs(Context context, MySearchCallBack searchCallback) {
        Log.d(TAG, "getSongs: launching...");

        int index = 0;
        songArrayList = new ArrayList<>();

        //TODO:BUG here : requires android.permission.READ_EXTERNAL_STORAGE or grantUriPermission()
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null
        );

        if (cursor != null) {
            Log.d(TAG, "getSongs: cursor: " + cursor.toString());
            while (cursor.moveToNext()) {
                Log.d(TAG, "getSongs: cursor move enter");
                Song song = new Song();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Log.d(TAG, "getSongs: path: " + path);
                if (path.toLowerCase().endsWith(".mp3")) {
                    song.setAlbum(album);
                    song.setArtist(artist);
                    song.setId(id);
                    song.setName(name);
                    song.setPath(path);
                    song.setIndex(index);

                    songArrayList.add(song);
                    index++;
                    Log.d(TAG, "getSongs: song id :" + song.getId());
                }
            }
            if (songArrayList.size() > 0) {
                searchCallback.onSuccess(songArrayList);
                Log.d(TAG, "getSongs: searchCallBack --> SongList size:" + songArrayList.size());
            } else {
                searchCallback.onFalse("未搜索到相应的MP3音乐文件!");
            }
            cursor.close();


        }
    }
}
