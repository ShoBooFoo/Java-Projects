package com.example.myightie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class splash2 extends AppCompatActivity {
    private static final long SPLASH_DELAY = 10000;
    private final String hint1 = "Touch anywhere on the map to move there.";
    private final String hint2 = "Explore new places and plunder the treasures.";
    private final String hint3 = "Exchange your items with gold in the marketplace.";
    private final String hint4 = "Buy special additions with gold in the marketplace.";
    private String kullanici;
    private VideoView vv;
    private TextView tv7;
    private int rn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash2);
        Intent intent = getIntent();
        kullanici = intent.getStringExtra("username");

        Random random = new Random();
        rn = random.nextInt(4);
        tv7 = findViewById(R.id.textView7);
        tv7.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (rn == 0) { tv7.setText(hint1); }
        else if (rn == 1) { tv7.setText(hint2); }
        else if (rn == 2) { tv7.setText(hint3); }
        else { tv7.setText(hint4); }

        vv = findViewById(R.id.videoView2);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.phone;
        vv.setVideoURI(Uri.parse(videoPath));
        vv.start();
        vv.setOnCompletionListener(mediaPlayer -> vv.start());

        new Handler().postDelayed(() -> {
            Intent myI = new Intent(splash2.this, anasayfa.class);
            myI.putExtra("username", kullanici);
            startActivity(myI);
            finish();
        }, SPLASH_DELAY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rn == 0) { tv7.setText(hint1); }
        else if (rn == 1) { tv7.setText(hint2); }
        else if (rn == 2) { tv7.setText(hint3); }
        else { tv7.setText(hint4); }
        vv.start();
        vv.setOnCompletionListener(mediaPlayer -> vv.start());
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv.stopPlayback();
    }
}
