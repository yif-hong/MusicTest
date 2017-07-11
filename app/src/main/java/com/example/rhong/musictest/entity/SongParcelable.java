package com.example.rhong.musictest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rhong on 2017/7/11.
 * 将Song类实现Parcelable接口，提升使用性能，学习测试用。
 */

public class SongParcelable implements Parcelable {
    public static final Creator<SongParcelable> CREATOR = new Creator<SongParcelable>() {
        @Override
        public SongParcelable createFromParcel(Parcel parcel) {
            return new SongParcelable(parcel);
        }

        @Override
        public SongParcelable[] newArray(int i) {
            return new SongParcelable[i];
        }
    };
    private int id;
    private String name;
    private String path;
    private String album;
    private int index;
    private String artist;

    public SongParcelable(Parcel in) {
        id = in.readInt();
        index = in.readInt();
        name = in.readString();
        path = in.readString();
        artist = in.readString();
        album = in.readString();
    }

    public SongParcelable() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(index);
        parcel.writeString(name);
        parcel.writeString(path);
        parcel.writeString(artist);
        parcel.writeString(album);
    }
}
