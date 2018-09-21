package com.techbeloved.moviesbeloved.data.source.local;

import com.techbeloved.moviesbeloved.data.models.Movie;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movies")
    List<Movie> getMovies();

    @Query("SELECT * FROM movies WHERE movie_id = :movieId")
    Movie getMovieById(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Update
    int updateMovie(Movie movie);

    @Query("DELETE FROM movies WHERE movie_id = :movieId")
    int deleteMovieById(int movieId);

    @Query("DELETE FROM movies")
    void deleteMovies();

}
