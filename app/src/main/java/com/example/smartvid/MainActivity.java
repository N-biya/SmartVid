package com.example.smartvid;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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

    }
}
