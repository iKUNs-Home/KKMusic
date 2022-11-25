package com.ikunkun.kunmusic.comn;

import android.graphics.drawable.BitmapDrawable;

import java.io.Serializable;

public class MusicInfo implements Serializable {

    @Override
    public String toString() {
        return "MusicInfo{" +
                "MusicId=" + MusicId +
                ", PageImg='" + PageImg + '\'' +
                ", MusicName='" + MusicName + '\'' +
                ", MusicSinger='" + MusicSinger + '\'' +
                ", MusicUrl='" + MusicUrl + '\'' +
                '}';
    }

    public Integer getMusicId() {
        return MusicId;
    }

    public void setMusicId(Integer musicId) {
        MusicId = musicId;
    }

    public String getPageImg() {
        return PageImg;
    }

    public void setPageImg(String pageImg) {
        PageImg = pageImg;
    }

    public String getMusicName() {
        return MusicName;
    }

    public void setMusicName(String musicName) {
        MusicName = musicName;
    }

    public String getMusicSinger() {
        return MusicSinger;
    }

    public void setMusicSinger(String musicSinger) {
        MusicSinger = musicSinger;
    }

    public String getMusicUrl() {
        return MusicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        MusicUrl = musicUrl;
    }

    public BitmapDrawable getBmpDraw() {
        return bmpDraw;
    }

    public void setBmpDraw(BitmapDrawable bmpDraw) {
        this.bmpDraw = bmpDraw;
    }

    /**
     * MusicId 音乐id
     * PageImg 图片url
     * MusicName 音乐名
     * MusicSinger 歌手
     * MusicUrl 下载链接
     * bmpDraw 图片bitmap
     * MusicPath 路径
     * MusicSize 大小
     */
    private BitmapDrawable bmpDraw;
    private Integer MusicId;
    private String PageImg;
    private String MusicName;
    private String MusicSinger;
    private String MusicUrl;
    private String MusicPath;
    private long MusicSize;
    public long getMusicSize() {
        return MusicSize;
    }

    public void setMusicSize(long musicSize) {
        MusicSize = musicSize;
    }


    public String getMusicPath() {
        return MusicPath;
    }

    public void setMusicPath(String musicPath) {
        MusicPath = musicPath;
    }


    public MusicInfo() {
    }


}
