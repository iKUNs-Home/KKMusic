package com.ikunkun.kunmusic.comn;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Binder;

import java.io.Serializable;

public class MusicInfo implements Serializable {

    @Override
    public String toString () {
        return "MusicInfo{" +
                "MusicId=" + MusicId +
                ", PageImg='" + PageImg + '\'' +
                ", MusicName='" + MusicName + '\'' +
                ", MusicSinger='" + MusicSinger + '\'' +
                ", MusicUrl='" + MusicUrl + '\'' +
                '}';
    }

    public String getMusicId() {
        return MusicId;
    }

    public void setMusicId(String musicId) {
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




    public byte[] getBmp_pic() {
        return bmp_pic;
    }

    public void setBmp_pic(byte[] bmp_pic) {
        this.bmp_pic = bmp_pic;
    }

    /**
     * MusicId 音乐id
     * PageImg 图片url
     * MusicName 音乐名
     * MusicSinger 歌手
     * MusicUrl 下载链接
     * bmp_pic 图片bitmap的btye流形式
     * MusicPath 路径
     * MusicSize 大小
     */
//    bitmap无法可序化，需转化为byte[]形式
    private byte[] bmp_pic;
    private String MusicId;
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
}
