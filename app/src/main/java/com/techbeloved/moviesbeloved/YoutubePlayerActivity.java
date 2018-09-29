package com.techbeloved.moviesbeloved;

import android.os.Bundle;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.techbeloved.moviesbeloved.utils.Constants;
import timber.log.Timber;

public class YoutubePlayerActivity extends YouTubeBaseActivity {

    private YouTubePlayerView mYoutubeView;
    private String mVideoId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_player);

        mVideoId = getIntent().getStringExtra(Constants.YOUTUBE_VIDEO_ID);
        mYoutubeView = findViewById(R.id.youtube_player_view);
        initializeYoutubePlayer();
    }

    private void initializeYoutubePlayer() {
        mYoutubeView.initialize(Constants.YOUTUBE_DEVELOPER_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    youTubePlayer.cueVideo(mVideoId);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Timber.e("Youtube player initialization failed!");
            }
        });
    }
}
