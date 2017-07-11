package com.example.rhong.musictest.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rhong on 2017/7/5.
 */

public class MusicUtil {
    public static void setImageViewResource(View view, MotionEvent motionEvent, int resId_down, int resID_up) {
        //TODO:设置ImageView的src点击效果
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ((ImageButton) view).setImageResource(resId_down);
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            ((ImageButton) view).setImageResource(resID_up);
        }
    }

    public static void selectedImageView(final ImageView imageView, final int resId_down, final int resId_up) {
        //TODO:设置选中的Tab-src点击效果
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    imageView.setImageResource(resId_down);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageView.setImageResource(resId_up);
                }
                return false;
            }
        });
    }

    public static void releasePlayer(MediaPlayer... mediaPlayers) {
        for (MediaPlayer mediaPlayer : mediaPlayers) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }



    /**
     * @param filePath 文件路径，like XXX/XXX/XX.mp3
     * @return 专辑封面bitmap
     * @Description 获取专辑封面
     */
    public static Bitmap createAlbumArt(final String filePath) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath); //设置数据源
            byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void setPlayCurrentTime(TextView timeView, int progress) {
        timeView.setText(DateUtils.formatElapsedTime(progress / 1000));
    }
}
