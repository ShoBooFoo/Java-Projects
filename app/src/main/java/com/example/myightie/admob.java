package com.example.myightie;

import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.Random;

public class admob extends AppCompatActivity {
    private VideoView vv;
    private String kullanici;
    private int rn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admob);
        Intent intent = getIntent();
        kullanici = intent.getStringExtra("username");
        Random random = new Random();
        rn = random.nextInt(6);
    }

    @Override
    protected void onStart() {
        super.onStart();
        vv = findViewById(R.id.videoView);
        if (rn == 0) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bob;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else if (rn==1) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.rage;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else if (rn==2) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.steel;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else if (rn==3) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.steve;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else if (rn==4) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.vagrants;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.think;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rn == 0) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.bob;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else if (rn==1) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.rage;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else if (rn==2) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.steel;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else if (rn==3) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.steve;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else if (rn==4) {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.vagrants;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
        else {
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.think;
            vv.setVideoURI(Uri.parse(videoPath));
            vv.start();
            vv.setOnCompletionListener(mediaPlayer -> updateEnerji(kullanici));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        vv.stopPlayback();
    }

    private void updateEnerji(String kullanici) {
        int pro = 100;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference docRef = db.collection("users").document(document.getId());
                            runOnUiThread(() -> {
                                docRef.update("enerji", (double) pro)
                                        .addOnSuccessListener(aVoid -> { })
                                        .addOnFailureListener(e -> { });
                                finish();
                            });
                        }
                    }
                });
    }
}