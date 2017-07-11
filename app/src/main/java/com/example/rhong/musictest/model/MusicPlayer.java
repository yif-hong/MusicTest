package com.example.rhong.musictest.model;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Message;
import android.util.Log;

import com.example.rhong.musictest.MyApplication;
import com.example.rhong.musictest.entity.Song;
import com.example.rhong.musictest.util.ConstantUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import layout.Id3Fragment;

/**
 * Created by rhong on 2017/7/6.
 */

public class MusicPlayer implements IPlayer {

    private static final String TAG = "MusicPlayer";
    private static MusicPlayer musicPlayer;
    private static Context context;
    private static int musicIndex;
    Intent intent = new Intent();
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songArrayList;
    private UpdateThread updateThread = null;
    private int pausePosition;
    private boolean isRandom;
    private Random random = new Random();
    private int playMode;
    private int subIndex;
    private ArrayList<Song> subList;

    public MusicPlayer() {
        super();
        mediaPlayer = MyApplication.getMediaPlayer();
    }


    public static synchronized MusicPlayer getMusicPlayer(Context context) {
        MusicPlayer.context = context;

        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }
        return musicPlayer;
    }

    private void startUpdateThread() {
        stopUpdateThread();
        if (updateThread == null) {
            updateThread = new UpdateThread();
            updateThread.setRunning(true);
            updateThread.start();
        } else {
            updateThread.setRunning(true);
            updateThread.start();
        }
    }

    private void stopUpdateThread() {
        if (updateThread != null) {
            updateThread.setRunning(false);
            updateThread = null;
        }
    }

    private void play() {

        Log.d(TAG, "play: music index-------->" + musicIndex);
        mediaPlayer.reset();
        String path = songArrayList.get(musicIndex).getPath();

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.seekTo(pausePosition);
        mediaPlayer.setOnPreparedListener(new MyPreparedListener());
        mediaPlayer.setOnCompletionListener(new MyCompletionListener());
        startUpdateThread();
    }

    private void matchSongListAndSubList() {
        for (int i = 0; i < songArrayList.size(); i++) {
            if (songArrayList.get(i).getId() == subList.get(subIndex).getId()) {
                musicIndex = i;
            }
        }
    }

    private void pause() {
        pausePosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.stop();
        stopUpdateThread();
    }

    private void pauseAndClear() {
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
        intent.setAction(ConstantUtil.ACTION_CLEAR);
        context.sendBroadcast(intent);
    }

    @Override
    public void callPlay() {
        play();
    }

    @Override
    public void callPause() {
        pause();
    }

    @Override
    public void callPrevious() {
        isRandom = Id3Fragment.isRandom;
        if (isRandom) {
            switch (playMode) {
                case ConstantUtil.PLAY_ALL:
                    musicIndex = random.nextInt(songArrayList.size());
                    break;
                case ConstantUtil.PLAY_ORDER:
                    musicIndex = random.nextInt(songArrayList.size());
                    break;
                case ConstantUtil.PLAY_FOLDER:
                    subIndex = random.nextInt(subList.size());
                    matchSongListAndSubList();
                    break;
                case ConstantUtil.PLAY_SINGLE:
                    break;
                default:
                    break;
            }
        } else {
            switch (playMode) {
                case ConstantUtil.PLAY_ALL:
                    musicIndex--;
                    if (musicIndex < 0) {
                        musicIndex = songArrayList.size() - 1;
                    }
                    break;
                case ConstantUtil.PLAY_ORDER:
                    musicIndex--;
                    if (musicIndex < 0) {
                        musicIndex = 0;
                        pauseAndClear();
                    }
                    break;
                case ConstantUtil.PLAY_FOLDER:
                    if (subIndex == 0) {
                        musicIndex--;
                        if (musicIndex < 0) {
                            musicIndex = songArrayList.size() - 1;
                        }
                    } else {
                        subIndex--;
                        if (subIndex < 0) {
                            subIndex = subList.size() - 1;
                        }
                        matchSongListAndSubList();
                    }
                    break;
                case ConstantUtil.PLAY_SINGLE:
                    break;
                default:
                    break;
            }
        }
        pausePosition = 0;
        play();

    }

    @Override
    public void callNext() {
        isRandom = Id3Fragment.isRandom;
        Log.d(TAG, "callNext: call next");
        if (isRandom) {
            switch (playMode) {
                case ConstantUtil.PLAY_ALL:
                    musicIndex = random.nextInt(songArrayList.size());
                    break;
                case ConstantUtil.PLAY_ORDER:
                    musicIndex = random.nextInt(songArrayList.size());
                    break;
                case ConstantUtil.PLAY_FOLDER:
                    subIndex = random.nextInt(subList.size());
                    matchSongListAndSubList();
                    break;
                case ConstantUtil.PLAY_SINGLE:
                    break;
                default:
                    break;
            }
        } else {
            switch (playMode) {
                case ConstantUtil.PLAY_ALL:
                    musicIndex++;
                    if (musicIndex == songArrayList.size()) {
                        musicIndex = 0;
                    }
                    break;
                case ConstantUtil.PLAY_ORDER:
                    musicIndex++;
                    if (musicIndex == songArrayList.size() - 1) {
                        musicIndex = 0;
                        pauseAndClear();
                    }
                    break;
                case ConstantUtil.PLAY_FOLDER:
                    if (subIndex == 0) {
                        musicIndex++;
                        if (musicIndex == songArrayList.size()) {
                            musicIndex = 0;
                        }
                    } else {
                        subIndex++;
                        if (subIndex == subList.size()) {
                            subIndex = 0;
                        }
                        matchSongListAndSubList();
                    }
                    break;
                case ConstantUtil.PLAY_SINGLE:
                    break;
                default:
                    break;
            }
        }
        pausePosition = 0;
        play();
    }

    @Override
    public void callPlay(int index) {
        musicIndex = index;
        pausePosition = 0;
        play();
    }

    @Override
    public void callCurrentProgress(int progress) {
        pausePosition = callGetDuration() * progress / 1000;
        if (mediaPlayer.isPlaying()) {
            play();
        }
    }

    @Override
    public boolean callIsPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int callMusicIndex() {
        return musicIndex;
    }

    @Override
    public int callGetDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int callGetCurrentTime() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void setData(ArrayList<Song> dataList) {
        songArrayList = dataList;
    }

    @Override
    public boolean callIsCollect() {
        return false;
    }

    @Override
    public Message callMusicSub() {//TODO:>>
        Message message = Message.obtain();
        message.arg1 = subIndex;
        message.obj = subList;

        return message;
    }

    @Override
    public void setPlayMode(int playMode) {
        this.playMode = playMode;
    }

    @Override
    public void callPlaySong(ArrayList<Song> subData, int subPosition) {
        subList = subData;
        subIndex = subPosition;
        for (int i = 0; i < songArrayList.size(); i++) {
            if (songArrayList.get(i).getId() == subData.get(subPosition).getId()) {
                musicIndex = i;
                pausePosition = 0;
                play();
            }
        }

    }

    class UpdateThread extends Thread {
        public boolean isRunning;

        public void setRunning(boolean running) {
            isRunning = running;
        }

        @Override
        public void run() {
            while (isRunning) {
                if (mediaPlayer.isPlaying()) {
                    intent.setAction(ConstantUtil.ACTION_UPDATE);
                    intent.putExtra(ConstantUtil.INTENT_DURATION, mediaPlayer.getDuration());
                    intent.putExtra(ConstantUtil.INTENT_CURRENTTIME, mediaPlayer.getCurrentPosition());
                    context.sendBroadcast(intent);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
        }
    }

    class MyCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (isRandom) {
                switch (playMode) {
                    case ConstantUtil.PLAY_ALL:
                        musicIndex = random.nextInt(songArrayList.size());
                        break;
                    case ConstantUtil.PLAY_FOLDER:
                        subIndex = random.nextInt(subList.size());
                        matchSongListAndSubList();
                        break;
                    case ConstantUtil.PLAY_ORDER:
                        musicIndex = random.nextInt(songArrayList.size());
                        break;
                    case ConstantUtil.PLAY_SINGLE:
                        break;
                    default:
                        break;
                }
            } else {
                switch (playMode) {
                    case ConstantUtil.PLAY_ALL:
                        musicIndex++;
                        if (musicIndex == songArrayList.size()) {
                            musicIndex = 0;
                        }
                        break;
                    case ConstantUtil.PLAY_FOLDER:
                        subIndex++;
                        if (subIndex == subList.size()) {
                            subIndex = 0;
                        }
                        matchSongListAndSubList();
                        break;
                    case ConstantUtil.PLAY_ORDER:
                        musicIndex++;
                        if (musicIndex == songArrayList.size() - 1) {
                            mediaPlayer.pause();
                            return;
                        }
                        break;
                    case ConstantUtil.PLAY_SINGLE:
                        break;
                    default:
                        break;
                }
            }
            pausePosition = 0;
            play();
        }
    }
}
