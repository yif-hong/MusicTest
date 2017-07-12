package com.example.rhong.musictest.util;

/**
 * Created by rhong on 2017/7/6.
 */

public class ConstantUtil {
    public static final int REPEAT_MODE_ZERO = 0;
    public static final int REPEAT_MODE_ONE = 1;
    public static final int REPEAT_MODE_TWO = 2;
    public static final int REPEAT_MODE_THREE = 3;


    public static final int PLAY_ORDER = 20;//按顺序播放（不循环，列表播放完即结束）
    public static final int PLAY_ALL = 21;//全部播放（全部循环）
    public static final int PLAY_FOLDER = 22;
    public static final int PLAY_SINGLE = 23;

    public static final int FOLDER_CIRCLE = 30;
    public static final int CIRCLE = 31;

    public static final int SEARCHALL = 50;
    public static final int SEARCHSINGER = 51;
    public static final int SEARCHALBUM = 52;
    public static final int SEARCHFAVOUR = 53;

    public static final int ISSHOW_ALBUM_MUSIC = 60;
    public static final int ISSHOW_SINGER_MUSIC = 61;
    public static final int ENTER_SUBSEARCH_ALBUM_STATE = 70;
    public static final int ENTER_SUBSEARCH_SINGER_STATE = 71;
    public static final int ENTER_SEARCH_ALL = 80;
    public static final int ENTER_SEARCH_ALBUM = 81;
    public static final int ENTER_SEARCH_SINGER = 82;
    public static final int ENTER_SEARCH_FAVOUR = 83;

    public static final int IS_GET_FILE_LIST = 100;


    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_CLEAR = "clear";
    public static final String INTENT_DURATION = "duration";
    public static final String INTENT_CURRENTTIME = "currentTime";

    public static final int LIST_ALL = 101;
    public static final int LIST_ALBUM = 102;
    public static final int LIST_SINGER = 103;
    public static final int LIST_FAVOURITE = 104;


}
