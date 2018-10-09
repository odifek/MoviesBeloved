package com.techbeloved.moviesbeloved.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.TimeUtils;

import com.techbeloved.moviesbeloved.data.models.*;

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
            int runtime = jsonMovie.optInt("runtime");

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
            movieInfo.setRuntime(runtime);

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

    public static String getGenres(List<String> genres) {
        if (genres != null && !genres.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0, j = genres.size(); i < j; i++) {
                builder.append(genres.get(i));
                if (i != j - 1) builder.append(" | ");
            }
            return builder.toString();
        }
        return "";
    }

    public static ReviewEntity createReviewModel(int movieId, JSONObject jsonReview) {
        try {
            String reviewId = jsonReview.getString("id");
            String author = jsonReview.optString("author");
            String content = jsonReview.optString("content");
            String url = jsonReview.optString("url");

            Review review = new ReviewEntity(movieId, reviewId);
            if (!TextUtils.isEmpty(author)) review.setAuthor(author);
            if (!TextUtils.isEmpty(content)) review.setContent(content);
            if (!TextUtils.isEmpty(url)) review.setUrl(url);

            return (ReviewEntity) review;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static VideoEntity createVideoModel(int movieId, JSONObject jsonVideo) {
        try {
            String videoId = jsonVideo.getString("id");
            String key = jsonVideo.optString("key");
            String name = jsonVideo.optString("name");
            String site = jsonVideo.optString("site");
            String type = jsonVideo.optString("type");

            Video video = new VideoEntity(movieId, videoId);
            if (!TextUtils.isEmpty(key)) video.setKey(key);
            if (!TextUtils.isEmpty(name)) video.setName(name);
            if (!TextUtils.isEmpty(site)) video.setSite(site);
            if (!TextUtils.isEmpty(type)) video.setType(type);

            return (VideoEntity) video;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
