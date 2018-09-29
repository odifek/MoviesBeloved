package com.techbeloved.moviesbeloved.moviedetails.videos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.techbeloved.moviesbeloved.BuildConfig;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Video;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import com.techbeloved.moviesbeloved.utils.Constants;
import timber.log.Timber;

import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private final VideoClickCallback mVideoClickCallback;

    private List<VideoEntity> mVideoList;

    public VideoAdapter(VideoClickCallback callback) {
        mVideoClickCallback = callback;
    }

    public void setVideoList(final List<VideoEntity> videoList) {
        mVideoList = videoList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

        // No data yet
        if (mVideoList == null) return;

        final Video video = getItem(position);

        holder.videoTitle.setText(video.getName());
        holder.videoType.setText(video.getType());


        holder.videoThumbnail.initialize(Constants.YOUTUBE_DEVELOPER_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(video.getKey());

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

    @Override
    public int getItemCount() {
        return mVideoList != null ? mVideoList.size() : 0;
    }

    private Video getItem(int position) {
        return mVideoList.get(position);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView videoTitle;
        TextView videoType;
        YouTubeThumbnailView videoThumbnail;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.video_title);
            videoType = itemView.findViewById(R.id.video_type_text);
            videoThumbnail = itemView.findViewById(R.id.video_thumbnail);

            // Listen for clicks
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mVideoClickCallback.onClick(getItem(getAdapterPosition()));
        }
    }
}
