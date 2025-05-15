package com.example.smartvid;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_MEDIA = 1;
    private ImageView imageView;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSelect = findViewById(R.id.btnSelect);
        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);

        btnSelect.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
            startActivityForResult(intent, PICK_MEDIA);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MEDIA && resultCode == RESULT_OK && data != null) {
            Uri selectedUri = data.getData();

            String type = getContentResolver().getType(selectedUri);
            if (type != null) {
                if (type.startsWith("image/")) {
                    // Show image
                    imageView.setVisibility(ImageView.VISIBLE);
                    videoView.setVisibility(VideoView.GONE);
                    imageView.setImageURI(selectedUri);
                } else if (type.startsWith("video/")) {
                    // Play video
                    imageView.setVisibility(ImageView.GONE);
                    videoView.setVisibility(VideoView.VISIBLE);
                    videoView.setVideoURI(selectedUri);
                    MediaController mediaController = new MediaController(this);
                    mediaController.setAnchorView(videoView);
                    videoView.setMediaController(mediaController);
                    videoView.start();
                }
            }
        }
    }
}
