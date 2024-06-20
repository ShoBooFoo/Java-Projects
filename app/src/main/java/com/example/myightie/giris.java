package com.example.myightie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class giris extends AppCompatActivity {
    private VideoView vv;
    private Button bt2, bt3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giris);
        bt2 = findViewById(R.id.button2);
        bt3 = findViewById(R.id.button3);
    }

    @Override
    protected void onStart() {
        super.onStart();
        vv = findViewById(R.id.videoView5);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.wallking;
        vv.setVideoURI(Uri.parse(videoPath));
        vv.start();
        vv.setOnCompletionListener(mediaPlayer -> vv.start());

        bt2.setOnClickListener(view -> {
            Intent myI1 = new Intent(giris.this, login.class);
            startActivity(myI1);
        });

        bt3.setOnClickListener(view -> {
            Intent myI2 = new Intent(giris.this, register.class);
            startActivity(myI2);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        vv.start();
        vv.setOnCompletionListener(mediaPlayer -> vv.start());
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv.stopPlayback();
    }
}