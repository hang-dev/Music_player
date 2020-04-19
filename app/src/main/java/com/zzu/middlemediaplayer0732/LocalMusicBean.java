package com.zzu.middlemediaplayer0732;

public class LocalMusicBean {
    private String id;
    private String song;
    private String singer;
    private String album;//专辑
    private String duration;//时长
    private String path;//歌曲路径
    private int num_duration;


    public int getNum_duration() {
        return num_duration;
    }

    public void setNum_duration(int num_duration) {
        this.num_duration = num_duration;
    }

    public LocalMusicBean() {

    }
    public LocalMusicBean(String id, String song, String singer, String album, String duration, String path,int num_duration) {
        this.id = id;
        this.song = song;
        this.singer=singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.num_duration=num_duration;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
