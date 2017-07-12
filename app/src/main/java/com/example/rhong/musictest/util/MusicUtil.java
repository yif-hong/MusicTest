package com.example.rhong.musictest.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.widget.ImageView;

import com.example.rhong.musictest.R;

import static com.example.rhong.musictest.util.ConstantUtil.PLAY_ALL;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_FOLDER;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_ORDER;
import static com.example.rhong.musictest.util.ConstantUtil.PLAY_SINGLE;
import static com.example.rhong.musictest.util.ConstantUtil.REPEAT_MODE_ONE;
import static com.example.rhong.musictest.util.ConstantUtil.REPEAT_MODE_THREE;
import static com.example.rhong.musictest.util.ConstantUtil.REPEAT_MODE_TWO;
import static com.example.rhong.musictest.util.ConstantUtil.REPEAT_MODE_ZERO;
import static layout.Id3Fragment.playMode;

/**
 * Created by rhong on 2017/7/5.
 */

public class MusicUtil {

    public static void switchImage(int num, ImageView repeatImageView, Activity activity) {
        switch (num) {
            case REPEAT_MODE_ZERO:
                playMode = PLAY_ORDER;
                repeatImageView.setImageResource(R.drawable.id3_icon_repeat_n);
                ToastUtil.showToast(activity, "顺序播放");
                break;
            case REPEAT_MODE_ONE:
                playMode = PLAY_ALL;
                repeatImageView.setImageResource(R.drawable.id3_icon_repeat_all_n);
                ToastUtil.showToast(activity, "全部循环");
                break;
            case REPEAT_MODE_TWO:
                playMode = PLAY_FOLDER;
                repeatImageView.setImageResource(R.drawable.id3_icon_repeat_folder_n);
                ToastUtil.showToast(activity, "列表循环");
                break;
            case REPEAT_MODE_THREE:
                playMode = PLAY_SINGLE;
                repeatImageView.setImageResource(R.drawable.id3_icon_repeat_one_n);
                ToastUtil.showToast(activity, "单曲循环");
                break;
            default:
                break;
        }
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


}
