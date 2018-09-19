package com.techbeloved.moviesbeloved.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.TimeUtils;

import com.techbeloved.moviesbeloved.data.models.Movie;
import com.techbeloved.moviesbeloved.data.models.MovieEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.techbeloved.moviesbeloved.utils.Constants.*;

public class MovieUtils {

    /**
     * Create a {@link Movie} model using JSON object received from volley
     * @param jsonMovie is the json object received from volley
     * @return a {@link Movie} object
     */
    public static MovieEntity createMovieModel(JSONObject jsonMovie) {

        try {
            String title = jsonMovie.getString("title");
            int id = jsonMovie.getInt("id");
            float rating = (float) jsonMovie.getDouble("vote_average");
            String posterPath = jsonMovie.getString("poster_path");
            String synopsis = jsonMovie.getString("overview");
            String releaseDateString = jsonMovie.getString("release_date");

            String backdropPath = jsonMovie.optString("backdrop_path");
            String originalTitle = jsonMovie.optString("original_title");
            JSONArray genresJsonArray = jsonMovie.optJSONArray("genres");

            List<String> genreList = new ArrayList<>();
            if (genresJsonArray != null) {
                for (int i = 0; i < genresJsonArray.length(); i++) {
                    genreList.add(genresJsonArray.getJSONObject(i).optString("name"));
                }
            }

            Date releaseDate = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault()).parse(releaseDateString);
            Movie movieInfo = new MovieEntity(id, title);
            movieInfo.setPosterUrl(buildImageUrl(posterPath, DEFAULT_POSTER_SIZE));
            movieInfo.setSynopsis(synopsis);
            movieInfo.setReleaseDate(releaseDate);
            movieInfo.setUserRating(rating);

            if (!TextUtils.isEmpty(backdropPath)) {
                movieInfo.setBackdropUrl(buildImageUrl(backdropPath, DEFAULT_BACKDROP_SIZE));
            }
            if (!genreList.isEmpty()) {
                movieInfo.setGenres(genreList);
            }

            return (MovieEntity) movieInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String buildImageUrl(String imagePath, String imageSize) {
        Uri.Builder uriBuilder = Uri.parse(TMDB_IMAGE_BASE_URL).buildUpon();
        uriBuilder.appendPath(imageSize)
                .appendEncodedPath(imagePath);
        return uriBuilder.build().toString();
    }

    public static String getYearFromDate(Date date) throws IllegalArgumentException {
        String year = "";
        if (date == null) {
            throw new IllegalArgumentException("Null date!");
        }

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        year = yearFormat.format(date);
        return year;
    }
}
