package com.example.rhong.musictest.entity;

import java.io.Serializable;

public class Song implements Serializable {

    private static final long serialViersionUID = 1L;

    private int id;
    private String name;
    //	private Boolean like;
    private String path;
    private String album;
    private int index;
    private String artist;

    public Song(int id, String name, String path, String album, int index, String artist) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.album = album;
        this.index = index;
        this.artist = artist;
    }

    public Song() {

    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", album='" + album + '\'' +
                ", index=" + index +
                ", artist='" + artist + '\'' +
                '}';
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
}
