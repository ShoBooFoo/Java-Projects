package com.example.myightie;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.TransformationMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class register extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private EditText kullanici, sifre1, sifre2, date;
    private String parola1, parola2, kullanici2, date2;
    private ArrayList<Integer> inventory;
    private TransformationMethod method;
    private FirebaseFirestore db;
    private VideoView vv;
    private Button bt5;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(register.this);

        kullanici = findViewById(R.id.editTextText2);
        kullanici.setMaxLines(1);

        sifre1 = findViewById(R.id.editTextTextPassword);
        sifre1.setMaxLines(1);
        sifre2 = findViewById(R.id.editTextTextPassword2);
        sifre2.setMaxLines(1);
        method = sifre1.getTransformationMethod();

        date = findViewById(R.id.editTextDate);
        date.setMaxLines(1);
        bt5 = findViewById(R.id.button5);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart() {
        super.onStart();
        vv = findViewById(R.id.videoView3);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.wallking;
        vv.setVideoURI(Uri.parse(videoPath));
        vv.start();
        vv.setOnCompletionListener(mediaPlayer -> vv.start());

        date.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) { showDatePickerDialog(); }
            return false;
        });

        bt5.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                sifre1.setTransformationMethod(null);
                sifre2.setTransformationMethod(null);

                parola1 = sifre1.getText().toString().trim();
                parola2 = sifre2.getText().toString().trim();
                kullanici2 = kullanici.getText().toString().trim();

                sifre1.setTransformationMethod(method);
                sifre2.setTransformationMethod(method);

                CollectionReference usersRef = db.collection("users");
                usersRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean found = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            if (Objects.equals(kullanici2, username)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                            builder.setTitle("WARNING!");
                            builder.setMessage("There is already someone with that username!");
                            builder.setPositiveButton("Ok", (dialog, which) -> {
                                Intent myI = new Intent(register.this, login.class);
                                startActivity(myI);
                                finish();
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                        builder.setTitle("FAILURE!");
                        builder.setMessage("Couldn't get the data from Firestore!");
                        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                if (kullanici2 == null || parola1 == null || parola2 == null || date2 == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                    builder.setTitle("EMPTY INSTANCES!");
                    builder.setMessage("Username, password or date of birth is empty!");
                    builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if (!parola1.equals(parola2)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                    builder.setTitle("INCORRECT!");
                    builder.setMessage("One of the password is wrong!");
                    builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if (age < 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                    builder.setTitle("ILLEGAL!");
                    builder.setMessage("You have to be at least 10 years old to make an account!");
                    builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else { checkPermissions(); }
            }
            return false;
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

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(register.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    date2 = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    date.setText(date2);
                    calculateAge(selectedYear, selectedMonth, selectedDay);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void calculateAge(int year, int month, int day) {
        final Calendar today = Calendar.getInstance();
        int currentYear = today.get(Calendar.YEAR);
        int currentMonth = today.get(Calendar.MONTH);
        int currentDay = today.get(Calendar.DAY_OF_MONTH);

        age = currentYear - year;
        if (currentMonth < month || (currentMonth == month && currentDay < day)) { age--; }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(register.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(register.this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION }, 1);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String username = kullanici.getText().toString().trim();
                String parola = sifre1.getText().toString().trim();

                fusedLocationClient.getLastLocation().addOnCompleteListener(register.this, task -> {
                    if (task.isSuccessful()) {
                        Location point = task.getResult();
                        double latitude = point.getLatitude();
                        double longitude = point.getLongitude();
                        GeoPoint geoPoint = new GeoPoint(latitude, longitude);

                        inventory = new ArrayList<>();
                        inventory.add(0);
                        inventory.add(0);
                        inventory.add(0);
                        inventory.add(0);
                        inventory.add(0);

                        Map<String, Object> userData = new HashMap<>();
                        userData.put("username", username);
                        userData.put("parola", parola);
                        userData.put("enerji", 100.0);
                        userData.put("gold", 100.0);
                        userData.put("date", date2);
                        userData.put("point", geoPoint);
                        userData.put("inventory", inventory);
                        userData.put("inventory2", inventory);
                        userData.put("bidder", "None");
                        userData.put("bidding", 0.0);

                        db.collection("users")
                                .add(userData)
                                .addOnSuccessListener(documentReference -> {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                                    builder.setTitle("SUCCESS!");
                                    builder.setMessage("Your account has been successfully made!");
                                    builder.setPositiveButton("Ok", (dialog, which) -> {
                                        Intent myI = new Intent(register.this, login.class);
                                        startActivity(myI);
                                        finish();
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                })
                                .addOnFailureListener(e -> {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                                    builder.setTitle("FAILURE!");
                                    builder.setMessage("Couldn't transfer data to Firestore!");
                                    builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                });
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                        builder.setTitle("FAILURE!");
                        builder.setMessage("Couldn't get the users location!");
                        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
                builder.setTitle("WARNING!");
                builder.setMessage("Without the users location the game can't be played!");
                builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

}
