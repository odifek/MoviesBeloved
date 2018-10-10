package com.techbeloved.moviesbeloved.data.source.local;

import androidx.lifecycle.LiveData;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies")
    Flowable<List<MovieEntity>> getMovies();

    @Query("SELECT * FROM movies WHERE movie_id = :movieId")
    LiveData<MovieEntity> getMovieById(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieEntity movie);

    @Update
    int updateMovie(MovieEntity movie);

    @Query("DELETE FROM movies WHERE movie_id = :movieId")
    int deleteMovieById(int movieId);

    @Query("DELETE FROM movies")
    void deleteMovies();

}
