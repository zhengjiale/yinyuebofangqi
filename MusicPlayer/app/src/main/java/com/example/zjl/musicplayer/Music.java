package com.example.zjl.musicplayer;

import java.io.Serializable;

/**
 * Created by ZJL on 2018/4/6.
 */

public class Music implements Serializable {
    private static final long serialVersionUID=1;
    private String title;
    private String singer;
    private String album;
    private int album_id;
    private String url;
    private long size;
    private int time;
    private String name ;
    public int getAlbum_id(){
        return album_id;
    }
    public void setAlbum_id(int album_id){
        this.album_id=album_id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title) {
        this.title=title;
    }
    public String getSinger(){
        return singer;
    }
    public void setSinger(String singer){
        this.singer=singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Music [title="+title+",singer="+singer+"]";
    }
}
