package com.techbeloved.moviesbeloved.moviedetails;

import androidx.databinding.BindingAdapter;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.techbeloved.moviesbeloved.utils.Constants;
import timber.log.Timber;

public class BindingAdapters {
    @BindingAdapter("youtubeId")
    public static void loadYoutubeThumbnail(YouTubeThumbnailView thumbnailView, String youtubeId) {
        thumbnailView.initialize(Constants.YOUTUBE_DEVELOPER_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(youtubeId);

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        Timber.e("Youtube Thumbnail Error!");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Timber.e("Youtube Initialization failure");
            }
        });
    }
}
