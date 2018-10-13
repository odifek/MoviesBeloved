package com.techbeloved.moviesbeloved.data.source.local;

import androidx.room.*;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies")
    Flowable<List<MovieEntity>> getMovies();

    @Query("SELECT * FROM movies WHERE movie_id = :movieId")
    Single<MovieEntity> getMovieById(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieEntity movie);

    @Update
    int updateMovie(MovieEntity movie);

    @Query("DELETE FROM movies WHERE movie_id = :movieId")
    int deleteMovieById(int movieId);

    @Query("DELETE FROM movies")
    void deleteMovies();

}
