package layout;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhong.musictest.MyApplication;
import com.example.rhong.musictest.R;
import com.example.rhong.musictest.entity.Song;
import com.example.rhong.musictest.model.MusicPlayer;
import com.example.rhong.musictest.model.OnDraggingListener;
import com.example.rhong.musictest.model.UpdateListProgress;
import com.example.rhong.musictest.presenter.ISearchPresenter;
import com.example.rhong.musictest.presenter.SearchSongsPresenter;
import com.example.rhong.musictest.util.DateFormatUtil;
import com.example.rhong.musictest.util.MusicUtil;
import com.example.rhong.musictest.util.ToastUtil;
import com.example.rhong.musictest.view.IView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.rhong.musictest.util.ConstantUtil.ACTION_CLEAR;
import static com.example.rhong.musictest.util.ConstantUtil.ACTION_UPDATE;
import static com.example.rhong.musictest.util.ConstantUtil.INTENT_CURRENTTIME;
import static com.example.rhong.musictest.util.ConstantUtil.INTENT_DURATION;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_ALL;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_FOLDER;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_ORDER;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_SINGLE;
import static com.example.rhong.musictest.util.ConstantUtil.REPEAT_MODE_ONE;
import static com.example.rhong.musictest.util.ConstantUtil.REPEAT_MODE_THREE;
import static com.example.rhong.musictest.util.ConstantUtil.REPEAT_MODE_TWO;
import static com.example.rhong.musictest.util.ConstantUtil.REPEAT_MODE_ZERO;


/**
 * Created by rhong on 2017/7/3.
 */

public class Id3Fragment extends Fragment implements View.OnTouchListener, IView, View.OnClickListener {
    private static final String TAG = "Id3Fragment";
    public static boolean isRandom = false;
    public static HashMap<Integer, Boolean> collectMap = new LinkedHashMap<>();
    private static int musicIndex;
    private static ArrayList<Song> allSongList = new ArrayList<>();
    private static UpdateListProgress updateListProgress;
    private int number = 0;
    private boolean isCollected = false;
    private boolean isShuffle = false;
    private View view;
    private ImageView id3Collect, id3PlayOrPause, id3Repeat, id3Prev, id3Next, id3Shuffle, id3SongBitMap;
    private TextView titleTV, artistTV, albumTV, currentTimeTV;
    private MusicPlayer musicPlayer;
    private boolean isTouchingSeekBar;
    private ISearchPresenter searchPresenter;
    private BroadcastReceiver broadcastReceiver;
    private MediaPlayer mediaPlayer;
    private io.feeeei.circleseekbar.CircleSeekBar seekBar;
    private int playMode = PLAY_ORDER;//默认order
    private OnDraggingListener draggingListener;

    public static void registerUpdateListProgress(UpdateListProgress listProgress) {
        updateListProgress = listProgress;
    }

    public static void saveMusic() {
        ArrayList<Song> collectList = new ArrayList<>();
        for (int i : collectMap.keySet()) {
            boolean isLike = collectMap.get(i);
            if (isLike) {
                collectList.add(allSongList.get(i));
            }
        }
        for (Song s : collectList) {
            Log.d(TAG, "saveMusic: " + s.toString());
        }
        File dir = Environment.getExternalStorageDirectory();
        try {
            File file = new File(dir, "collect.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (int i = 0; i < collectList.size(); i++) {
                oos.writeObject(collectList.get(i));
                oos.flush();
            }
            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_id3_constrain, container, false);
        musicPlayer = MusicPlayer.getMusicPlayer(getActivity().getApplicationContext());
        searchPresenter = new SearchSongsPresenter(this);
        searchPresenter.getSongs(getActivity().getApplicationContext());
        musicPlayer.setPlayMode(playMode);

        initView();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ACTION_UPDATE)) {
                    if (!isTouchingSeekBar) {
                        int currentTime = intent.getIntExtra(INTENT_CURRENTTIME, 0);
                        int duration = intent.getIntExtra(INTENT_DURATION, 0);
                        int progress = currentTime * 1000 / duration;
                        seekBar.setCurProcess(progress);
                        currentTimeTV.setText(DateFormatUtil.getDate(currentTime));
                        updateTitle();
                    }

                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE);
        filter.addAction(ACTION_CLEAR);
        getActivity().registerReceiver(broadcastReceiver, filter);
        mediaPlayer = MyApplication.getMediaPlayer();

        return view;
    }

    private void initView() {

        id3Collect = view.findViewById(R.id.id3_collect);
        id3SongBitMap = view.findViewById(R.id.iv_background_image);
        id3PlayOrPause = view.findViewById(R.id.id3_play_or_pause);
        id3Next = view.findViewById(R.id.id3_button_next);
        id3Prev = view.findViewById(R.id.id3_button_prev);
        id3Repeat = view.findViewById(R.id.id3_button_repeat);
        id3Shuffle = view.findViewById(R.id.id3_button_shuffle);
        titleTV = view.findViewById(R.id.id3_music_title);
        artistTV = view.findViewById(R.id.id3_music_artist);
        albumTV = view.findViewById(R.id.id3_music_album);
        currentTimeTV = view.findViewById(R.id.tv_duration);


        seekBar = view.findViewById(R.id.circle_seekbar);


        id3Collect.setOnClickListener(this);
        id3PlayOrPause.setOnClickListener(this);
        id3Next.setOnClickListener(this);
        id3Prev.setOnClickListener(this);
        id3Repeat.setOnClickListener(this);
        id3Shuffle.setOnClickListener(this);

        seekBar.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        seekBar.onTouchEvent(motionEvent);
//        float eventX = motionEvent.getX();
//        float eventY = motionEvent.getY();
        boolean isUp = false;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                seekBar.seekTo(eventX, eventY, isUp);
                int progressActionDown = seekBar.getCurProcess();
                long mills = progressActionDown * mediaPlayer.getCurrentPosition() / 1000;
                currentTimeTV.setText(DateFormatUtil.getDate(mills));
                isTouchingSeekBar = true;
                updateListProgress.mUpdate(progressActionDown, isTouchingSeekBar);
                break;

            case MotionEvent.ACTION_UP:
                isUp = true;
//                seekBar.seekTo(eventX, eventY, isUp);
                int progressActionUp = seekBar.getCurProcess();
                musicPlayer.callCurrentProgress(progressActionUp);
                isTouchingSeekBar = false;
                updateListProgress.mUpdate(progressActionUp, isTouchingSeekBar);
                break;

            case MotionEvent.ACTION_MOVE:
//                seekBar.seekTo(eventX, eventY, isUp);
                int progressActionMove = seekBar.getCurProcess();
                long mills2 = progressActionMove * mediaPlayer.getCurrentPosition() / 1000;
                currentTimeTV.setText(DateFormatUtil.getDate(mills2));
                isTouchingSeekBar = true;
                updateListProgress.mUpdate(progressActionMove, isTouchingSeekBar);
                break;

        }
        draggingListener.setTouching(isTouchingSeekBar);

        return true;//TODO: ------->onTouch和onClick同时响应需要返回false，单独响应onTouch仅需要返回true
    }



    @Override
    public void showData(ArrayList<Song> songs) {
        allSongList.clear();
        allSongList.addAll(songs);
        for (int i = 0; i < allSongList.size(); i++) {
            collectMap.put(i, false);
        }
        musicPlayer.setData(songs);
    }

    @Override
    public void onFalse() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isPlaying = musicPlayer.callIsPlaying();
        if (isPlaying) {
            id3PlayOrPause.setImageResource(R.drawable.id3_button_pause_n);
        } else {
            id3PlayOrPause.setImageResource(R.drawable.id3_button_play_n);
        }
        updateTitle();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.id3_collect:
                Log.d(TAG, "onClick: collect");
                isCollected = !isCollected;
                if (isCollected) {
                    collectMap.put(musicIndex, true);
                } else {
                    collectMap.put(musicIndex, false);
                }
                id3Collect.setActivated(isCollected);
                saveMusic();
                break;
            case R.id.id3_play_or_pause:
                boolean isPlaying = musicPlayer.callIsPlaying();
                if (isPlaying) {
                    musicPlayer.callPause();
                    id3PlayOrPause.setImageResource(R.drawable.id3_button_play_n);
                } else {
                    musicPlayer.callPlay();
                    id3PlayOrPause.setImageResource(R.drawable.id3_button_pause_n);
                    updateTitle();
                }
                break;
            case R.id.id3_button_prev:
                musicPlayer.callPrevious();
                updateTitle();
                id3PlayOrPause.setImageResource(R.drawable.id3_button_pause_n);
                break;
            case R.id.id3_button_next:
                Log.d(TAG, "onClick: next song");
                musicPlayer.callNext();
                updateTitle();
                id3PlayOrPause.setImageResource(R.drawable.id3_button_pause_n);
                break;
            case R.id.id3_button_shuffle:
                isShuffle = !isShuffle;
                id3Shuffle.setSelected(isShuffle);
                if (isShuffle) {
                    isRandom = true;
                } else {
                    isRandom = false;
                }
                break;
            case R.id.id3_button_repeat:
                number++;
                int num = number % 4;
                switchImage(num);
                musicPlayer.setPlayMode(playMode);
                break;
        }
    }

    public void updateTitle() {
        this.musicIndex = musicPlayer.callMusicIndex();
        artistTV.setText(allSongList.get(musicIndex).getArtist());
        titleTV.setText(allSongList.get(musicIndex).getName());
        albumTV.setText(allSongList.get(musicIndex).getAlbum());

        //获取专辑封面
        Bitmap albumBitmap = MusicUtil.createAlbumArt(allSongList.get(musicIndex).getPath());
        id3SongBitMap.setImageBitmap(albumBitmap);

        if (collectMap.get(musicIndex)) {
            id3Collect.setActivated(true);
        } else {
            id3Collect.setActivated(false);
        }
    }

    public void switchImage(int num) {
        switch (num) {
            case REPEAT_MODE_ZERO:
                playMode = PLAY_ORDER;
                id3Repeat.setImageResource(R.drawable.id3_icon_repeat_n);
                ToastUtil.showToast(getActivity(), "顺序播放");
                break;
            case REPEAT_MODE_ONE:
                playMode = PLAY_ALL;
                id3Repeat.setImageResource(R.drawable.id3_icon_repeat_all_n);
                ToastUtil.showToast(getActivity(), "全部循环");
                break;
            case REPEAT_MODE_TWO:
                playMode = PLAY_FOLDER;
                id3Repeat.setImageResource(R.drawable.id3_icon_repeat_folder_n);
                ToastUtil.showToast(getActivity(), "列表循环");
                break;
            case REPEAT_MODE_THREE:
                playMode = PLAY_SINGLE;
                id3Repeat.setImageResource(R.drawable.id3_icon_repeat_one_n);
                ToastUtil.showToast(getActivity(), "单曲循环");
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof OnDraggingListener) {
            draggingListener = (OnDraggingListener) activity;
        }
        super.onAttach(activity);
    }

}

