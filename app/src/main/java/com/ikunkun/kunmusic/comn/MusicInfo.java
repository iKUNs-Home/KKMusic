package com.ikunkun.kunmusic.comn;

public class MusicInfo {
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

    /**
     * MusicId 音乐id
     * PageImg 图片url
     * MusicName 音乐名
     * MusicSinger 歌手
     * MusicUrl 下载链接
     */
    private Integer MusicId;
    private String PageImg;
    private String MusicName;
    private String MusicSinger;
    private String MusicUrl;

    public MusicInfo() {
    }


}
