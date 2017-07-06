package com.example.rhong.musictest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.rhong.musictest.R;
import com.example.rhong.musictest.entity.Song;

import java.util.List;

public class MyListAdapter extends BaseAdapter {

    private List<Song> songList;
    private LayoutInflater inflater;

    public MyListAdapter() {
    }

    public MyListAdapter(List<Song> songList, Context context) {
        this.songList = songList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return songList == null ? 0 : songList.size();
    }

    @Override
    public Song getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 加载布局为一个视图
        View view = inflater.inflate(R.layout.item_list, null);

        return view;
    }
}