package com.example.myightie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    private VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        vv = findViewById(R.id.videoView);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.explosion;
        vv.setVideoURI(Uri.parse(videoPath));
        vv.start();
        vv.setOnCompletionListener(mediaPlayer -> {
            Intent intent = new Intent(splash.this, giris.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        vv.start();
        vv.setOnCompletionListener(mediaPlayer -> {
            Intent intent = new Intent(splash.this, giris.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv.stopPlayback();
    }
}
