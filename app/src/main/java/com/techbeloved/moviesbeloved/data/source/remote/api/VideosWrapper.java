package com.techbeloved.moviesbeloved.data.source.remote.api;

import com.google.gson.annotations.SerializedName;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;

import java.util.List;

public class VideosWrapper {
    @SerializedName("results")
    private List<VideoEntity> videoList;

    public List<VideoEntity> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoEntity> videoList) {
        this.videoList = this.videoList;
    }
}
