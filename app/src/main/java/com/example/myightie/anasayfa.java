package com.example.myightie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.os.Vibrator;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentReference;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import java.util.ArrayList;
import java.util.Random;

public class anasayfa extends AppCompatActivity {
    private final double tolerance = 20.0;
    private int progress, goldie, change, movement;
    private String kullanici;
    private MapView mv;
    private TextView tv5, tv6, tv7;
    private MediaPlayer mp3 = new MediaPlayer();
    private Button bt4, bt5;
    private ProgressBar pb;
    private GeoPoint geoPoint;
    private Marker mark, mark2;
    private final ArrayList<Marker> itemMarkers = new ArrayList<>();
    private final ArrayList<Marker> goldMarkers = new ArrayList<>();
    private ArrayList<Integer> inventory = new ArrayList<>();
    private ArrayList<Integer> inventory2 = new ArrayList<>();
    private Drawable people, gold, question, target;

    @SuppressLint({"UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anasayfa);

        Intent intent = getIntent();
        kullanici = intent.getStringExtra("username");
        movement = 0;

        mv = findViewById(R.id.mapView);
        tv5 = findViewById(R.id.textView5);
        tv6 = findViewById(R.id.textView6);
        tv7 = findViewById(R.id.textView7);
        pb = findViewById(R.id.progressBar2);

        bt4 = findViewById(R.id.button4);
        bt5 = findViewById(R.id.button5);

        people = ContextCompat.getDrawable(anasayfa.this, R.drawable.people);
        gold = ContextCompat.getDrawable(anasayfa.this, R.drawable.gold);
        question = ContextCompat.getDrawable(anasayfa.this, R.drawable.question);
        target = ContextCompat.getDrawable(anasayfa.this, R.drawable.target);

        tv5.setVisibility(TextView.INVISIBLE);
        tv6.setVisibility(TextView.VISIBLE);
        tv7.setVisibility(TextView.VISIBLE);
        bt4.setVisibility(View.VISIBLE);
        bt5.setVisibility(View.VISIBLE);

        bt4.setClickable(true);
        bt5.setClickable(true);
        getGeoPoint(kullanici, point -> {
            geoPoint = point;
            spawning();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart() {
        super.onStart();
        getEnerji(kullanici);
        getGold(kullanici);
        getInventory(kullanici);
        getInventory2(kullanici);

        mv.setClickable(true);
        mv.setMultiTouchControls(false);
        mv.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);
        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("osmdroid", MODE_PRIVATE));
        mv.setTileSource(TileSourceFactory.MAPNIK);

        getGeoPoint(kullanici, point -> {
            geoPoint = point;
            setupMap();
            spawning();
        });

        /*
        for (int i = 0; i < inventory2.size(); i++) {
            int element = inventory2.get(i);
            if (element == 1) {
                if (inventory2.get(i) == 0) {
                    mp3 = MediaPlayer.create(anasayfa.this, R.raw.halo);
                    if (mp3 != null && !mp3.isPlaying()) { runOnUiThread(() -> mp3.start()); }
                }
            }
        }
        */

        bt4.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                getEnerji(kullanici);
                if (progress >= 100) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(anasayfa.this);
                    builder.setTitle("WARNING!");
                    builder.setMessage("You can't gain more energy than 100!");
                    builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(anasayfa.this);
                    builder.setTitle("WARNING!");
                    builder.setMessage("Do you want the watch advertisement for 100 energy?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        Intent myI = new Intent(anasayfa.this, admob.class);
                        myI.putExtra("username", kullanici);
                        startActivity(myI);
                    });
                    builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
            return true;
        });

        bt5.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                AlertDialog.Builder builder = new AlertDialog.Builder(anasayfa.this);
                builder.setTitle("WARNING!");
                builder.setMessage("Do you want to visit marketplace?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    Intent myI = new Intent(anasayfa.this, marketplace.class);
                    myI.putExtra("username", kullanici);
                    startActivity(myI);
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            return true;
        });

        mv.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                getEnerji(kullanici);
                if (progress > 0) {
                    if (mv.isClickable()) {
                        mv.setClickable(false);
                        tv5.setVisibility(TextView.VISIBLE);

                        double latitude = mv.getProjection().fromPixels
                                ((int) event.getX(), (int) event.getY()).getLatitude();
                        double longitude = mv.getProjection().fromPixels
                                ((int) event.getX(), (int) event.getY()).getLongitude();

                        if (movement == 20) {
                            movement = 0;
                            itemMarkers.clear();
                            goldMarkers.clear();
                            spawning();
                        }
                        else { movement++; }

                        geoPoint = new GeoPoint(latitude, longitude);
                        updateGeoPoint(kullanici, latitude, longitude);

                        mark2 = new Marker(mv);
                        mark2.setPosition(geoPoint);
                        mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                        mark2.setIcon(target);
                        mv.getOverlays().add(mark2);
                        mv.invalidate();

                        updateEnerji(kullanici);
                        getEnerji(kullanici);
                        getInventory2(kullanici);
                        getInventory(kullanici);
                        /*
                        for (int i = 0; i < 5; i++) {
                            int element = inventory2.get(i);
                            if (element == 1) {
                                switch (i) {
                                    case 1:
                                        mp3 = MediaPlayer.create(anasayfa.this, R.raw.mario);
                                        if (mp3 != null && !mp3.isPlaying()) { runOnUiThread(() -> mp3.start()); }

                                    case 2:
                                        mp3 = MediaPlayer.create(anasayfa.this, R.raw.wah);
                                        if (mp3 != null && !mp3.isPlaying()) { runOnUiThread(() -> mp3.start()); }

                                    case 3:
                                        mp3 = MediaPlayer.create(anasayfa.this, R.raw.yummy);
                                        if (mp3 != null && !mp3.isPlaying()) { runOnUiThread(() -> mp3.start()); }

                                    case 4:
                                        mp3 = MediaPlayer.create(anasayfa.this, R.raw.mario);
                                        if (mp3 != null && !mp3.isPlaying()) { runOnUiThread(() -> mp3.start()); }
                                }
                            }
                        }
                        */
                        startCountDownTimer();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(anasayfa.this);
                        builder.setTitle("WARNING!");
                        builder.setMessage("You are already in movement!");
                        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(anasayfa.this);
                    builder.setTitle("WARNING!");
                    builder.setMessage("There isn't enough energy for the movement!");
                    builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv.onResume();
        getEnerji(kullanici);
        getGold(kullanici);
        getInventory(kullanici);
        getInventory2(kullanici);
        mp3.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv.onPause();
        mp3.pause();
    }

    private void startCountDownTimer() {
        new CountDownTimer(6000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) { tv5.setText("Time Left: " + millisUntilFinished / 1000); }

            @Override
            public void onFinish() {
                TitremeUtil.telefonuTitret(getApplicationContext());
                getGeoPoint(kullanici, point -> {
                    geoPoint = point;
                    setupMap();
                });
            }
        }.start();
    }

    public static class TitremeUtil {
        private static final long TITREME_SURESI = 250;
        public static void telefonuTitret(Context context) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) { vibrator.vibrate(TITREME_SURESI); }
        }
    }

    private void setupMap() {
        getGeoPoint(kullanici, point -> geoPoint = point);
        mv.getController().setZoom(17.5);
        mv.getController().setCenter(geoPoint);
        mv.getController().animateTo(geoPoint);
        mv.invalidate();

        if (!mv.getOverlays().isEmpty()) { mv.getOverlays().clear(); }

        mark = new Marker(mv);
        mark.setPosition(geoPoint);
        mark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        mark.setIcon(people);
        mv.getOverlays().add(mark);
        mv.invalidate();

        tv5.setVisibility(TextView.INVISIBLE);
        mv.setClickable(true);
    }

    private void updateInventory(String kullanici) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference docRef = db.collection("users").document(document.getId());
                            runOnUiThread(() -> docRef.update("inventory", inventory)
                                    .addOnSuccessListener(aVoid -> { })
                                    .addOnFailureListener(e -> { }));
                        }
                    }
                });
    }

    private void updateGold(String kullanici) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference docRef = db.collection("users").document(document.getId());
                            runOnUiThread(() -> {
                                goldie = goldie + change;
                                docRef.update("gold", (double) goldie)
                                        .addOnSuccessListener(aVoid -> { })
                                        .addOnFailureListener(e -> { });
                            });
                        }
                    }
                });
    }

    private void updateEnerji(String kullanici) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference docRef = db.collection("users").document(document.getId());
                            runOnUiThread(() -> {
                                progress = progress - 20;
                                docRef.update("enerji", (double) progress)
                                        .addOnSuccessListener(aVoid -> { })
                                        .addOnFailureListener(e -> { });
                            });
                        }
                    }
                });
    }

    private void updateGeoPoint(String kullanici, double newLatitude, double newLongitude) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference docRef = db.collection("users").document(document.getId());
                            runOnUiThread(() -> docRef.update("point", new com.google.firebase.firestore.GeoPoint
                                            (newLatitude, newLongitude)).addOnSuccessListener(aVoid -> { })
                                    .addOnFailureListener(e -> { }));
                        }
                    }
                });
    }

    private void getInventory2(String kullanici) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Object arr2 = document.get("inventory2");
                            if (arr2 != null) {
                                runOnUiThread(() -> inventory2 = (ArrayList<Integer>) arr2);
                                return;
                            }
                        }
                    }
                });
    }

    private void getInventory(String kullanici) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Object arr = document.get("inventory");
                            if (arr != null) {
                                runOnUiThread(() -> inventory = (ArrayList<Integer>) arr);
                                return;
                            }
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getGold(String kullanici) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Number gold = document.getDouble("gold");
                            if (gold != null) {
                                goldie = gold.intValue();
                                runOnUiThread(() -> tv7.setText("Gold: " + goldie + " G"));
                                break;
                            }
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getEnerji(String kullanici) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Number enerji = document.getDouble("enerji");
                            if (enerji != null) {
                                progress = enerji.intValue();
                                runOnUiThread(() -> {
                                    tv6.setText("Energy: " + progress);
                                    pb.setProgress(progress);
                                });
                                break;
                            }
                        }
                    }
                });
    }

    private void getGeoPoint(String kullanici, OnGeoPointFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            com.google.firebase.firestore.GeoPoint point = document.getGeoPoint("point");
                            if (point != null) {
                                runOnUiThread(() -> {
                                    geoPoint = new GeoPoint(point.getLatitude(), point.getLongitude());
                                    listener.onGeoPointFetched(geoPoint);
                                });
                                return;
                            }
                        }
                    }
                });
    }

    interface OnGeoPointFetchedListener { void onGeoPointFetched(GeoPoint geoPoint); }

    private void spawning() {
        double minLatitude = geoPoint.getLatitude() - 0.2;
        double maxLatitude = geoPoint.getLatitude() + 0.2;
        double minLongitude = geoPoint.getLongitude() - 0.2;
        double maxLongitude = geoPoint.getLongitude() + 0.2;
        Random random = new Random();

        for (int i = 0; i < 2000; i++) {
            double ranLatitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
            double ranLongitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();
            GeoPoint goldGeopoint = new GeoPoint(ranLatitude, ranLongitude);

            Marker goldMarker = new Marker(mv);
            goldMarker.setPosition(goldGeopoint);
            goldMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            goldMarker.setIcon(gold);

            goldMarker.setOnMarkerClickListener((marker, mv) -> {
                GeoPoint point2 = marker.getPosition();
                change = 30;
                updateGold(kullanici);
                getGold(kullanici);
                removeGoldMarker(point2, tolerance);
                return true;
            });

            goldMarkers.add(goldMarker);
            mv.getOverlays().add(goldMarker);
            mv.invalidate();
        }

        for (int i = 0; i < 1000; i++) {
            double ranLatitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
            double ranLongitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();
            GeoPoint itemGeopoint = new GeoPoint(ranLatitude, ranLongitude);

            Marker itemMarker = new Marker(mv);
            itemMarker.setPosition(itemGeopoint);
            itemMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
            itemMarker.setIcon(question);

            itemMarker.setOnMarkerClickListener((marker, mv) -> {
                itemMarkerListeners(itemMarker);
                return true;
            });

            itemMarkers.add(itemMarker);
            mv.getOverlays().add(itemMarker);
            mv.invalidate();
        }
    }

    public void removeGoldMarker(GeoPoint targetPoint, double tolerance) {
        for (Marker marker : goldMarkers) {
            GeoPoint markerPoint = marker.getPosition();
            double distance = markerPoint.distanceToAsDouble(targetPoint);
            if (distance <= tolerance) {
                mv.getOverlays().remove(marker);
                goldMarkers.remove(marker);
                mv.invalidate();
                break;
            }
        }
    }

    public void removeItemMarker(GeoPoint targetPoint, double tolerance) {
        for (Marker marker : itemMarkers) {
            GeoPoint markerPoint = marker.getPosition();
            double distance = markerPoint.distanceToAsDouble(targetPoint);
            if (distance <= tolerance) {
                mv.getOverlays().remove(marker);
                itemMarkers.remove(marker);
                mv.invalidate();
                break;
            }
        }
    }

    public void itemMarkerListeners(Marker marker) {
        GeoPoint point = marker.getPosition();
        Random random = new Random();
        int rn = random.nextInt(5);
        if (rn == 0) {
            int numb = inventory.get(0);
            numb++;
            inventory.set(0, numb);
        }
        else if (rn==1) {
            int numb = inventory.get(1);
            numb++;
            inventory.set(1, numb);
        }
        else if (rn==2) {
            int numb = inventory.get(2);
            numb++;
            inventory.set(2, numb);
        }
        else if (rn==3) {
            int numb = inventory.get(3);
            numb++;
            inventory.set(3, numb);
        }
        else {
            int numb = inventory.get(4);
            numb++;
            inventory.set(4, numb);
        }
        updateInventory(kullanici);
        getInventory(kullanici);
        removeItemMarker(point, tolerance);
    }
}