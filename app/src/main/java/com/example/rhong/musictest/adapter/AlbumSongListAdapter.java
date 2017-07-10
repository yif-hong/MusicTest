package com.example.rhong.musictest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhong.musictest.R;
import com.example.rhong.musictest.entity.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xr on 2017/7/9.
 */

public class AlbumSongListAdapter extends BaseAdapter {


    public static Map<Integer, Boolean> subChecked = new HashMap<Integer, Boolean>();
    private Context context;
    private ArrayList<Song> data;
    private LayoutInflater inflater;


    public AlbumSongListAdapter(Context context, ArrayList<Song> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(this.context);
        for (int i = 0; i < data.size(); i++) {
            subChecked.put(i, false);
        }
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Song getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_fav, null);
            viewHolder = new ViewHolder();
            viewHolder.songNameTV = view.findViewById(R.id.item_list_fav_song_name);
            viewHolder.songTagIV = view.findViewById(R.id.item_list_fav_tag);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.songNameTV.setText(getItem(i).getName());
        viewHolder.songTagIV.setSelected(subChecked.get(i));
        viewHolder.songNameTV.setSelected(subChecked.get(i));

        return view;
    }

    private class ViewHolder {
        TextView songNameTV;
        ImageView songTagIV;
    }

}
