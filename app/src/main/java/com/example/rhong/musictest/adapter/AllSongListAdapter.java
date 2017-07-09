package com.example.rhong.musictest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.rhong.musictest.R;
import com.example.rhong.musictest.entity.Song;
import com.example.rhong.musictest.model.UpdateListProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import layout.Id3Fragment;

public class AllSongListAdapter extends BaseAdapter implements UpdateListProgress {

    public static Map<Integer, Boolean> checked = new HashMap<>();
    private ArrayList<Song> songList;
    private LayoutInflater inflater;
    private Context context;
    private boolean isCollected = false;
    private boolean isTouchingSeekBar;
    private int mProgress;

    public AllSongListAdapter(Context context, ArrayList<Song> dataList) {
        songList = dataList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        for (int i = 0; i < songList.size(); i++) {
            checked.put(i, false);
        }
        Id3Fragment.resgisterUpdateListProgress(this);

    }

    @Override
    public void mUpdate(int progress, boolean isTouching) {
        isTouchingSeekBar = isTouching;
        mProgress = progress;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int i) {
        return songList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        Song song = (Song) getItem(i);
        if (view == null) {
            view = inflater.inflate(R.layout.item_list, null);
            viewHolder = new ViewHolder();
            viewHolder.ivTag.setSelected(checked.get(i));
            viewHolder.tvName.setText(song.getName());
            viewHolder.ivCollect.setActivated(Id3Fragment.collectMap.get(i));
            viewHolder.tvName.setSelected(checked.get(i));
            viewHolder.ivCollect.setOnClickListener(new MyOnClickListener(i));

            viewHolder.listSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            if (checked.get(i)) {
                viewHolder.listSeekBar.setProgress(mProgress);
                viewHolder.listSeekBar.setVisibility(View.VISIBLE);
            } else {
                viewHolder.listSeekBar.setProgress(0);
                viewHolder.listSeekBar.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    public void setListProgress(int progress) {
        if (!isTouchingSeekBar) {
            this.mProgress = progress;
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        ImageView ivCollect;
        ImageView ivTag;
        TextView tvName;
        SeekBar listSeekBar;


    }

    class MyOnClickListener implements View.OnClickListener {
        private int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            isCollected = !isCollected;
            if (isCollected) {
                Id3Fragment.collectMap.put(mProgress, true);
            } else {
                Id3Fragment.collectMap.put(mProgress, false);
            }
            Id3Fragment.saveMusic();
            AllSongListAdapter.this.notifyDataSetChanged();
        }
    }

}