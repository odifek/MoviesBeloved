package com.techbeloved.moviesbeloved;

import android.content.Context;

import com.techbeloved.moviesbeloved.data.source.MoviesRepository;
import com.techbeloved.moviesbeloved.data.source.local.FavoriteDatabase;
import com.techbeloved.moviesbeloved.data.source.local.MoviesLocalDataSource;
import com.techbeloved.moviesbeloved.data.source.remote.MoviesRemoteDataSource;
import com.techbeloved.moviesbeloved.data.source.remote.MoviesRequestQueue;
import com.techbeloved.moviesbeloved.data.source.remote.TMDBMovies;
import com.techbeloved.moviesbeloved.utils.AppExecutors;

import androidx.annotation.NonNull;

import static com.bumptech.glide.util.Preconditions.checkNotNull;

public class Injection {
    public static MoviesRepository provideMoviesRepository(@NonNull Context context) {
        checkNotNull(context);

        FavoriteDatabase database = FavoriteDatabase.getInstance(context);

        TMDBMovies mDBMovies = TMDBMovies.getInstance(MoviesRequestQueue.getInstance(context));
        return MoviesRepository.getInstance(MoviesRemoteDataSource.getInstance(mDBMovies),
                                              MoviesLocalDataSource.getInstance(new AppExecutors(),
                                                      database.moviesDao(),
                                                      database.reviewsDao(),
                                                      database.videosDao()));

    }
}
