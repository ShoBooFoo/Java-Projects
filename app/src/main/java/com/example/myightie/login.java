package com.example.myightie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class login extends AppCompatActivity {
    private EditText username2, parola2;
    private VideoView vv;
    private Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username2 = findViewById(R.id.editTextText);
        parola2 = findViewById(R.id.editTextTextPassword3);
        bt1 = findViewById(R.id.button10);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart() {
        super.onStart();
        vv = findViewById(R.id.videoView4);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.wallking;
        vv.setVideoURI(Uri.parse(videoPath));
        vv.start();
        vv.setOnCompletionListener(mediaPlayer -> vv.start());

        bt1.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                String username3 = username2.getText().toString().trim();
                String parola3 = parola2.getText().toString().trim();

                if (username3.isEmpty() || parola3.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                    builder.setTitle("EMPTY INSTANCES!");
                    builder.setMessage("Username or password is empty!");
                    builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference usersRef = db.collection("users");
                    usersRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            boolean found = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String username = document.getString("username");
                                String parola = document.getString("parola");
                                assert username != null;
                                assert parola != null;
                                if (username.equals(username3) && parola.equals(parola3)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                Intent myI = new Intent(login.this, splash2.class);
                                myI.putExtra("username", username3);
                                startActivity(myI);
                                finish();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                                builder.setTitle("INCORRECT!");
                                builder.setMessage("Username or password is wrong!");
                                builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
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
}
