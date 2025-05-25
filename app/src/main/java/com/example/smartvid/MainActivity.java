package com.example.smartvid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView createNew = findViewById(R.id.createNewText);
        TextView videoText = findViewById(R.id.videoText);
        TextView photoText = findViewById(R.id.photoText);

        ViewTreeObserver.OnGlobalLayoutListener gradientListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                createNew.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int widthCreate = createNew.getWidth();
                int widthVideo = videoText.getWidth();
                int widthPhoto = photoText.getWidth();

                Shader shaderCreate = new LinearGradient(
                        0, 0, widthCreate, 0,
                        new int[]{Color.parseColor("#4751ea"), Color.parseColor("#00a6f8")},
                        null, Shader.TileMode.CLAMP
                );
                createNew.getPaint().setShader(shaderCreate);
                createNew.invalidate();

                Shader shaderVideo = new LinearGradient(
                        0, 0, widthVideo, 0,
                        new int[]{Color.parseColor("#4751ea"), Color.parseColor("#00a6f8")},
                        null, Shader.TileMode.CLAMP
                );
                videoText.getPaint().setShader(shaderVideo);
                videoText.invalidate();

                Shader shaderPhoto = new LinearGradient(
                        0, 0, widthPhoto, 0,
                        new int[]{Color.parseColor("#4751ea"), Color.parseColor("#00a6f8")},
                        null, Shader.TileMode.CLAMP
                );
                photoText.getPaint().setShader(shaderPhoto);
                photoText.invalidate();
            }
        };

        createNew.getViewTreeObserver().addOnGlobalLayoutListener(gradientListener);

        FrameLayout circle2 = findViewById(R.id.circle2);
        circle2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
            startActivity(intent);
        });

        FrameLayout circle1 = findViewById(R.id.circle1);
        circle1.setOnClickListener(v -> {
            checkPermissionAndOpenVideoEditor();
        });
    }

    private void checkPermissionAndOpenVideoEditor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            openVideoEditor();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openVideoEditor();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
        }
    }

    private void openVideoEditor() {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openVideoEditor();
            } else {
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
