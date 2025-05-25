package com.example.smartvid;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class VideoTimelineAdapter extends RecyclerView.Adapter<VideoTimelineAdapter.VideoViewHolder> {

    private final Context context;
    private final OnVideoClickListener listener;
    private final List<Uri> videoUris;

    public interface OnVideoClickListener {
        void onVideoClick(Uri uri);
    }

    public VideoTimelineAdapter(Context context, List<Uri> videoUris, OnVideoClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.videoUris = new ArrayList<>(videoUris); // copy list initially
    }

    // 👇 Add this to allow updating list after selecting new videos
    public void setVideoUris(List<Uri> newUris) {
        videoUris.clear();
        videoUris.addAll(newUris);
        notifyDataSetChanged(); // refresh the timeline
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_timeline_thumbnail, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Uri uri = videoUris.get(position);
        Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(v -> listener.onVideoClick(uri));
    }

    @Override
    public int getItemCount() {
        return videoUris.size();
    }



}
