package com.example.rhong.musictest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rhong.musictest.R;

import java.util.ArrayList;

/**
 * Created by rhong on 2017/7/10.
 */

public class AlbumAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> data;
    private LayoutInflater inflater;

    public AlbumAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int i) {
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
            view = inflater.inflate(R.layout.item_list_singer, null);
            viewHolder = new ViewHolder();
            viewHolder.tvAlbum = view.findViewById(R.id.singerlist_item_show_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tvAlbum.setText(data.get(i));

        return view;
    }

    class ViewHolder {
        TextView tvAlbum;
    }

}
