package com.example.smartvid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;
    private RecyclerView timelineRecyclerView;
    private VideoTimelineAdapter timelineAdapter;
    private final ArrayList<Uri> videoUris = new ArrayList<>();

    private final ActivityResultLauncher<Intent> pickVideoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    videoUris.clear(); // clear previous ones

                    // Handle multiple video selection
                    if (result.getData().getClipData() != null) {
                        int count = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                            takeUriPermission(uri);
                            videoUris.add(uri);
                        }
                    }
                    // Handle single video
                    else if (result.getData().getData() != null) {
                        Uri uri = result.getData().getData();
                        takeUriPermission(uri);
                        videoUris.add(uri);
                    }

                    // Update player and timeline
                    if (!videoUris.isEmpty()) {
                        playVideo(videoUris.get(0));
                        timelineAdapter.setVideoUris(videoUris);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(0, insets.getInsets(WindowInsetsCompat.Type.systemBars()).top, 0, 0);
            return insets;
        });

        playerView = findViewById(R.id.playerView);
        timelineRecyclerView = findViewById(R.id.frameTimeline);

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // Set up adapter
        timelineAdapter = new VideoTimelineAdapter(this, videoUris, uri -> playVideo(uri));
        timelineRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        timelineRecyclerView.setAdapter(timelineAdapter);

        // Launch file picker
        pickMultipleVideos();
    }

    private void playVideo(Uri uri) {
        Log.d("VideoActivity", "Playing: " + uri.toString());

        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    private void pickMultipleVideos() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION); // ✅ important
        pickVideoLauncher.launch(intent);
    }

    private void takeUriPermission(Uri uri) {
        try {
            getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );
        } catch (Exception e) {
            Log.e("VideoActivity", "Failed to take persistable permission for URI: " + uri, e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.pause(); // ✅ Don’t release on stop
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release(); // ✅ Release here instead
            player = null;
        }
    }
}
