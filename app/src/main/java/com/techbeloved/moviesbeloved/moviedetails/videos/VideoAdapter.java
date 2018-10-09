package com.techbeloved.moviesbeloved.moviedetails.videos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.techbeloved.moviesbeloved.BuildConfig;
import com.techbeloved.moviesbeloved.R;
import com.techbeloved.moviesbeloved.data.models.Video;
import com.techbeloved.moviesbeloved.data.models.VideoEntity;
import com.techbeloved.moviesbeloved.databinding.VideoItemBinding;
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
        VideoItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.video_item, parent, false);
        binding.setCallback(mVideoClickCallback);
        return new VideoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

        // No data yet
        if (mVideoList == null) return;

        final Video video = getItem(position);

        holder.binding.setVideo(video);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mVideoList != null ? mVideoList.size() : 0;
    }

    private Video getItem(int position) {
        return mVideoList.get(position);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        final VideoItemBinding binding;

        VideoViewHolder(@NonNull VideoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
