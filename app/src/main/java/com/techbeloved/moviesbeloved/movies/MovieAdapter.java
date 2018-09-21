package com.techbeloved.moviesbeloved.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<MovieEntity> mMovieList;
    final MovieClickCallback mMovieClickCallback;

    public MovieAdapter(MovieClickCallback clickCallback) {
        this.mMovieClickCallback = clickCallback;
    }

    public void setMovieList(final List<MovieEntity> movieList) {
        if (mMovieList == null) {
            mMovieList = movieList;
            notifyItemRangeInserted(0, movieList.size());
        } else {
            mMovieList.addAll(movieList);
            notifyItemRangeInserted(mMovieList.size(), movieList.size());
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        // No data yet
        if (mMovieList == null) return;

        Movie movie = getItem(position);

        holder.movieTitleText.setText(movie.getTitle());

        String rating = String.valueOf(movie.getUserRating());
        holder.movieRatingText.setText(rating);

        String posterUrl = movie.getPosterUrl();
        if (posterUrl != null) {
            Glide.with(holder.moviePosterImage.getContext())
                    .load(posterUrl)
                    .into(holder.moviePosterImage);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList != null ? mMovieList.size() : 0;
    }

    private Movie getItem(int position) {
        return mMovieList.get(position);
    }

    public void clear() {
        mMovieList.clear();
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView moviePosterImage;
        TextView movieTitleText;
        TextView movieRatingText;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePosterImage = itemView.findViewById(R.id.iv_movie_poster);
            movieTitleText = itemView.findViewById(R.id.tv_title_movie);
            movieRatingText = itemView.findViewById(R.id.tv_rating_value);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mMovieClickCallback.onClick(getItem(getAdapterPosition()));
        }
    }

}
