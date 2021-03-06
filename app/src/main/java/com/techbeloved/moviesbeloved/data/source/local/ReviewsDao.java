package com.techbeloved.moviesbeloved.data.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.techbeloved.moviesbeloved.data.models.ReviewEntity;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface ReviewsDao {

    @Query("SELECT * FROM reviews WHERE movie_id = :movieId")
    Flowable<List<ReviewEntity>> getReviews(int movieId);

    @Query("SELECT * FROM reviews WHERE review_id = :reviewId")
    LiveData<ReviewEntity> getReview(String reviewId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReview(ReviewEntity review);

    @Update
    int updateReview(ReviewEntity review);

    @Query("DELETE FROM reviews WHERE review_id = :reviewId")
    int deleteMovieById(String reviewId);

    @Query("DELETE FROM reviews WHERE movie_id = :movieId")
    void deleteReviews(int movieId);

    @Query("DELETE FROM reviews")
    void deleteAllReviews();
}
