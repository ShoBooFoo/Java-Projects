package com.example.myightie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import android.widget.ArrayAdapter;

public class marketplace extends AppCompatActivity {
    private ArrayList<Integer> inventory = new ArrayList<>();
    private ArrayList<Integer> inventory2 = new ArrayList<>();
    private int Goldie, change, bidAmount, highestBid;
    private long timeLeftInMillis = 2 * 60 * 60 * 1000;
    private int selectedPosition = -1;
    private double currentItem = 0;
    private ArrayAdapter<Integer> adapter;
    private TextView tv3, tv4, tv5, tv6;
    private Button bt7, bt8, bt9, bt10;
    private String kullanici, lastBidder;
    private EditText et;
    private ImageView img;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marketplace);
        Intent intent = getIntent();
        kullanici = intent.getStringExtra("username");

        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        tv5 = findViewById(R.id.textView5);
        tv6 = findViewById(R.id.textView6);
        bt7 = findViewById(R.id.button7);
        bt8 = findViewById(R.id.button8);
        bt9 = findViewById(R.id.button9);
        bt10 = findViewById(R.id.button10);
        img = findViewById(R.id.imageView);
        lv = findViewById(R.id.listView);
        et = findViewById(R.id.editTextText);

        tv3.setVisibility(TextView.VISIBLE);
        tv4.setVisibility(TextView.VISIBLE);
        tv5.setVisibility(TextView.VISIBLE);
        tv6.setVisibility(TextView.VISIBLE);
        bt7.setVisibility(View.VISIBLE);
        bt8.setVisibility(View.VISIBLE);
        bt9.setVisibility(View.VISIBLE);
        bt10.setVisibility(View.VISIBLE);
        img.setVisibility(View.VISIBLE);
        lv.setVisibility(View.INVISIBLE);
        et.setVisibility(View.VISIBLE);

        tv3.setClickable(false);
        tv4.setClickable(false);
        tv5.setClickable(false);
        tv6.setClickable(false);
        bt7.setClickable(true);
        bt8.setClickable(true);
        bt9.setClickable(true);
        bt10.setClickable(true);
        img.setClickable(false);
        lv.setClickable(false);
        et.setClickable(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart() {
        super.onStart();
        getGold(kullanici);
        getBidder();
        getBidding();
        getCurrentItem();

        if (currentItem == 0.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 1.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 2.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 3.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 4.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 5.0) { img.setImageResource(R.drawable.sword); }
        else if (currentItem == 6.0) { img.setImageResource(R.drawable.shield); }
        else if (currentItem == 7.0) { img.setImageResource(R.drawable.map); }
        else if (currentItem == 8.0) { img.setImageResource(R.drawable.cloak); }
        else { img.setImageResource(R.drawable.compass); }

        startTimer();
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTimeMillis = calendar.getTimeInMillis();

        long duration = 2 * 60 * 60 * 1000;
        if (currentTimeMillis > startTimeMillis) {
            timeLeftInMillis = duration - (currentTimeMillis - startTimeMillis) % duration; }
        else { timeLeftInMillis = duration - ((currentTimeMillis + 24 * 60 * 60 * 1000) - startTimeMillis) % duration; }
        updateTimer();

        bt7.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                tv4.setVisibility(TextView.INVISIBLE);
                tv5.setVisibility(TextView.INVISIBLE);
                tv6.setVisibility(TextView.INVISIBLE);
                bt10.setVisibility(View.INVISIBLE);
                img.setVisibility(View.INVISIBLE);
                lv.setVisibility(View.VISIBLE);
                et.setVisibility(View.INVISIBLE);

                getGold(kullanici);
                getInventory2(kullanici);
                et.setClickable(false);
                bt10.setClickable(false);

                adapter = new ArrayAdapter<Integer>(marketplace.this,
                        R.layout.list_item, R.id.item_integer, inventory2) {

                    @SuppressLint("SetTextI18n")
                    @NonNull
                    @Override
                    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        ImageView itemImage = view.findViewById(R.id.item_image);
                        TextView itemText = view.findViewById(R.id.item_text);
                        TextView itemInteger = view.findViewById(R.id.item_integer);
                        Button itemButton = view.findViewById(R.id.item_button);

                        itemImage.setImageResource(R.drawable.voice);
                        switch (position) {
                            case 0:
                                itemText.setText("Voice Packet 1 -> 100 Gold");
                                itemInteger.setText("");
                                if (inventory2.get(position) == 0) { itemButton.setText("Buy"); }
                                else if (inventory2.get(position) == 1) { itemButton.setText("Equip"); }
                                else { itemButton.setText("Unequip"); }
                                break;
                            case 1:
                                itemText.setText("Voice Packet 2 -> 100 Gold");
                                itemInteger.setText("");
                                if (inventory2.get(position) == 0) { itemButton.setText("Buy"); }
                                else if (inventory2.get(position) == 1) { itemButton.setText("Equip"); }
                                else { itemButton.setText("Unequip"); }
                                break;
                            case 2:
                                itemText.setText("Voice Packet 3 -> 100 Gold");
                                itemInteger.setText("");
                                if (inventory2.get(position) == 0) { itemButton.setText("Buy"); }
                                else if (inventory2.get(position) == 1) { itemButton.setText("Equip"); }
                                else { itemButton.setText("Unequip"); }
                                break;
                            case 3:
                                itemText.setText("Voice Packet 4 -> 100 Gold");
                                itemInteger.setText("");
                                if (inventory2.get(position) == 0) { itemButton.setText("Buy"); }
                                else if (inventory2.get(position) == 1) { itemButton.setText("Equip"); }
                                else { itemButton.setText("Unequip"); }
                                break;
                            case 4:
                                itemText.setText("Voice Packet 5 -> 100 Gold");
                                itemInteger.setText("");
                                if (inventory2.get(position) == 0) { itemButton.setText("Buy"); }
                                else if (inventory2.get(position) == 1) { itemButton.setText("Equip"); }
                                else { itemButton.setText("Unequip"); }
                                break;
                        }

                        if (selectedPosition == position) {
                            itemButton.setVisibility(View.VISIBLE);
                            itemButton.setClickable(true);
                        }
                        else {
                            itemButton.setVisibility(View.GONE);
                            itemButton.setClickable(false);
                        }

                        view.setOnClickListener(v -> {
                            selectedPosition = (selectedPosition == position) ? -1 : position;
                            notifyDataSetChanged();
                        });

                        itemButton.setOnClickListener(v -> {
                            if (itemButton.getText().toString().equals("Buy")) {
                                inventory2.set(position, 1);
                                updateInventory2(kullanici);
                                getInventory2(kullanici);

                                if (Goldie >= 100) {
                                    change = -100;
                                    updateGold(kullanici);
                                    getGold(kullanici);
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(marketplace.this);
                                    builder.setTitle("WARNING!");
                                    builder.setMessage("You don't have 100 gold to buy this item!");
                                    builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                            else if (itemButton.getText().toString().equals("Equip")) {
                                inventory2.set(position, 2);
                                updateInventory2(kullanici);
                                getInventory2(kullanici);
                            }
                            else {
                                inventory2.set(position, 1);
                                updateInventory2(kullanici);
                                getInventory2(kullanici);
                            }
                        });
                        return view;
                    }
                };
                lv.setAdapter(adapter);
            }
            return true;
        });

        bt8.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                tv4.setVisibility(TextView.VISIBLE);
                tv5.setVisibility(TextView.VISIBLE);
                tv6.setVisibility(TextView.VISIBLE);
                bt10.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
                lv.setVisibility(View.INVISIBLE);
                et.setVisibility(View.VISIBLE);

                et.setClickable(true);
                bt10.setClickable(true);
                et.setText("");
                getGold(kullanici);
                getBidder();
                getBidding();
            }
            return true;
        });

        bt9.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                tv4.setVisibility(TextView.INVISIBLE);
                tv5.setVisibility(TextView.INVISIBLE);
                tv6.setVisibility(TextView.INVISIBLE);
                bt10.setVisibility(View.INVISIBLE);
                img.setVisibility(View.INVISIBLE);
                lv.setVisibility(View.VISIBLE);
                et.setVisibility(View.INVISIBLE);

                getGold(kullanici);
                getInventory(kullanici);
                et.setClickable(false);
                bt10.setClickable(false);

                adapter = new ArrayAdapter<Integer>(marketplace.this,
                        R.layout.list_item, R.id.item_integer, inventory) {

                    @SuppressLint("SetTextI18n")
                    @NonNull
                    @Override
                    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        ImageView itemImage = view.findViewById(R.id.item_image);
                        TextView itemText = view.findViewById(R.id.item_text);
                        TextView itemInteger = view.findViewById(R.id.item_integer);
                        Button itemButton = view.findViewById(R.id.item_button);

                        switch (position) {
                            case 0:
                                itemImage.setImageResource(R.drawable.sword);
                                itemText.setText("Sword -> 50 Gold");
                                itemInteger.setText(String.valueOf(inventory.get(position)));
                                break;
                            case 1:
                                itemImage.setImageResource(R.drawable.shield);
                                itemText.setText("Shield -> 50 Gold");
                                itemInteger.setText(String.valueOf(inventory.get(position)));
                                break;
                            case 2:
                                itemImage.setImageResource(R.drawable.map);
                                itemText.setText("Map -> 50 Gold");
                                itemInteger.setText(String.valueOf(inventory.get(position)));
                                break;
                            case 3:
                                itemImage.setImageResource(R.drawable.cloak);
                                itemText.setText("Cloak -> 50 Gold");
                                itemInteger.setText(String.valueOf(inventory.get(position)));
                                break;
                            case 4:
                                itemImage.setImageResource(R.drawable.compass);
                                itemText.setText("Compass -> 50 Gold");
                                itemInteger.setText(String.valueOf(inventory.get(position)));
                                break;
                        }
                        itemButton.setText("Sell");

                        if (selectedPosition == position) {
                            itemButton.setVisibility(View.VISIBLE);
                            itemButton.setClickable(true);
                        }
                        else {
                            itemButton.setVisibility(View.GONE);
                            itemButton.setClickable(false);
                        }

                        view.setOnClickListener(v -> {
                            selectedPosition = (selectedPosition == position) ? -1 : position;
                            notifyDataSetChanged();
                        });

                        itemButton.setOnClickListener(v -> {
                            if (inventory.get(position) <= 0) {
                                int num = inventory.get(position);
                                num--;
                                inventory.set(position, num);
                                updateInventory(kullanici);
                                getInventory(kullanici);

                                change = 50;
                                updateGold(kullanici);
                                getGold(kullanici);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(marketplace.this);
                                builder.setTitle("WARNING!");
                                builder.setMessage("You have exactly zero of this item!");
                                builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                        return view;
                    }
                };
                lv.setAdapter(adapter);
            }
            return true;
        });

        bt10.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) { placeBid(); }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGold(kullanici);
        getBidder();
        getBidding();
        getCurrentItem();

        if (currentItem == 0.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 1.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 2.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 3.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 4.0) { img.setImageResource(R.drawable.voice); }
        else if (currentItem == 5.0) { img.setImageResource(R.drawable.sword); }
        else if (currentItem == 6.0) { img.setImageResource(R.drawable.shield); }
        else if (currentItem == 7.0) { img.setImageResource(R.drawable.map); }
        else if (currentItem == 8.0) { img.setImageResource(R.drawable.cloak); }
        else { img.setImageResource(R.drawable.compass); }

        startTimer();
        long currentTimeMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTimeMillis = calendar.getTimeInMillis();

        long duration = 2 * 60 * 60 * 1000;
        if (currentTimeMillis > startTimeMillis) {
            timeLeftInMillis = duration - (currentTimeMillis - startTimeMillis) % duration; }
        else { timeLeftInMillis = duration - ((currentTimeMillis + 24 * 60 * 60 * 1000) - startTimeMillis) % duration; }
        updateTimer();
    }

    @Override
    protected void onPause() { super.onPause(); }

    private void startTimer() {
        new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long currentTimeMillis = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 4);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long startTimeMillis = calendar.getTimeInMillis();

                long duration = 2 * 60 * 60 * 1000;
                if (currentTimeMillis > startTimeMillis) {
                    timeLeftInMillis = duration - (currentTimeMillis - startTimeMillis) % duration; }
                else { timeLeftInMillis = duration - ((currentTimeMillis + 24 * 60 * 60 * 1000)
                        - startTimeMillis) % duration; }
                updateTimer();
            }

            @Override
            public void onFinish() {
                getBidding();
                getBidder();

                if (kullanici.equals(lastBidder)) {
                    change = -bidAmount;
                    updateGold(kullanici);
                    getGold(kullanici);
                    if (currentItem < 5) {
                        int numb = inventory2.get((int) currentItem);
                        numb ++;
                        inventory2.set((int) currentItem, numb);
                        updateInventory2(kullanici);
                    }
                    else {
                        currentItem = currentItem -5;
                        int numb = inventory.get((int) currentItem);
                        numb ++;
                        inventory.set((int) currentItem, numb);
                        updateInventory(kullanici);
                    }
                }
                resetAuction();
                startTimer();
            }
        }.start();
    }

    private void updateTimer() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),
                "%02d:%02d:%02d", hours, minutes, seconds);
        tv4.setText(timeLeftFormatted);
    }

    @SuppressLint("SetTextI18n")
    private void placeBid() {
        getGold(kullanici);
        String bidString = et.getText().toString();
        if (!bidString.isEmpty()) {
            bidAmount = Integer.parseInt(bidString);
            if (bidAmount > highestBid && bidAmount <= Goldie) {
                highestBid = bidAmount;
                lastBidder = kullanici;
                updateBidding();
                updateBidder();

                tv5.setText("Highest Bid: " + highestBid);
                tv6.setText("Last Bidder: " + lastBidder);
                et.setText("");
            }
            else if (bidAmount > Goldie) {
                AlertDialog.Builder builder = new AlertDialog.Builder(marketplace.this);
                builder.setTitle("WARNING!");
                builder.setMessage("You don't have that much gold!");
                builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(marketplace.this);
                builder.setTitle("WARNING!");
                builder.setMessage("Bid must be higher than the current highest bid!");
                builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(marketplace.this);
            builder.setTitle("WARNING!");
            builder.setMessage("Please enter a bid!");
            builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void resetAuction() {
        highestBid = 0;
        lastBidder = "None";
        tv5.setText("Highest Bid: " + highestBid);
        tv6.setText("Last Bidder: " + lastBidder);
        timeLeftInMillis = 7200000;

        Random random = new Random();
        int rn = random.nextInt(100);
        if (rn < 5) {
            currentItem = 0;
            img.setImageResource(R.drawable.voice);
        }
        else if (rn < 10) {
            currentItem = 1;
            img.setImageResource(R.drawable.voice);
        }
        else if (rn < 15) {
            currentItem = 2;
            img.setImageResource(R.drawable.voice);
        }
        else if (rn < 20) {
            currentItem = 3;
            img.setImageResource(R.drawable.voice);
        }
        else if (rn < 25) {
            currentItem = 4;
            img.setImageResource(R.drawable.voice);
        }
        else if (rn < 40) {
            currentItem = 5;
            img.setImageResource(R.drawable.sword);
        }
        else if (rn < 55) {
            currentItem = 6;
            img.setImageResource(R.drawable.shield);
        }
        else if (rn < 70) {
            currentItem = 7;
            img.setImageResource(R.drawable.map);
        }
        else if (rn < 85) {
            currentItem = 8;
            img.setImageResource(R.drawable.cloak);
        }
        else {
            currentItem = 9;
            img.setImageResource(R.drawable.compass);
        }
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

    private void updateInventory2(String kullanici) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", kullanici)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference docRef = db.collection("users").document(document.getId());
                            runOnUiThread(() -> docRef.update("inventory2", inventory2)
                                    .addOnSuccessListener(aVoid -> { })
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
                                Goldie = Goldie + change;
                                docRef.update("gold", (double) Goldie)
                                        .addOnSuccessListener(aVoid -> { })
                                        .addOnFailureListener(e -> { });
                            });
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
                                Goldie = gold.intValue();
                                runOnUiThread(() -> tv3.setText("Gold: " + Goldie + " G"));
                                break;
                            }
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getBidder() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String bidder = document.getString("bidder");
                            if (bidder != null) {
                                runOnUiThread(() -> {
                                    lastBidder = bidder;
                                    tv6.setText("Last Bidder: " + lastBidder);
                                });
                                break;
                            }
                        }
                    }
                });
    }

    private void updateBidder() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference docRef = db.collection("users").document(document.getId());
                            runOnUiThread(() -> docRef.update("bidder", lastBidder)
                                    .addOnSuccessListener(aVoid -> { })
                                    .addOnFailureListener(e -> { }));
                        }
                    }
                });
    }

    private void updateBidding() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference docRef = db.collection("users").document(document.getId());
                            runOnUiThread(() -> docRef.update("bidding", (double) highestBid)
                                    .addOnSuccessListener(aVoid -> { })
                                    .addOnFailureListener(e -> { }));
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getBidding() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Number bidding = document.getDouble("bidding");
                            if (bidding != null) {
                                highestBid = bidding.intValue();
                                runOnUiThread(() -> tv5.setText("Highest Bid: " + highestBid));
                                break;
                            }
                        }
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getCurrentItem() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Number CurrentItem = document.getDouble("currentItem");
                            if (CurrentItem != null) {
                                runOnUiThread(() -> currentItem = CurrentItem.intValue());
                                break;
                            }
                        }
                    }
                });
    }
}