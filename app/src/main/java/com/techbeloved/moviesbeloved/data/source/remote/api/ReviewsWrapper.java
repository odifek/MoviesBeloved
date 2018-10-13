package com.techbeloved.moviesbeloved.data.source.remote.api;

import com.google.gson.annotations.SerializedName;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;

import java.util.List;

public class ReviewsWrapper {
    @SerializedName("results")
    private List<ReviewEntity> reviewList;

    public List<ReviewEntity> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<ReviewEntity> reviewList) {
        this.reviewList = reviewList;
    }
}
