package com.example.citycyclerentalsnew;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RentalActivity extends AppCompatActivity {
    private ImageView bikeIconImageView;
    private TextView bikeInfoTextView, rentalStatusTextView;
    private Button reserveButton, rentButton, endRentalButton;
    private DatabaseHelper db;
    private SharedPreferences prefs;
    private int bikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);

        bikeIconImageView = findViewById(R.id.bikeIconImageView);
        bikeInfoTextView = findViewById(R.id.bikeInfoTextView);
        rentalStatusTextView = findViewById(R.id.rentalStatusTextView);
        reserveButton = findViewById(R.id.reserveButton);
        rentButton = findViewById(R.id.rentButton);
        endRentalButton = findViewById(R.id.endRentalButton);
        db = new DatabaseHelper(this);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        bikeId = getIntent().getIntExtra("bike_id", -1);
        int userId = prefs.getInt("userId", -1);

        loadBikeInfo();
        checkRentalStatus(userId);

        reserveButton.setOnClickListener(v -> {
            String reserveTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String expiryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis() + 15 * 60 * 1000)); // 15 mins
            if (db.reserveBike(userId, bikeId, reserveTime, expiryTime)) {
                Toast.makeText(this, "Bike reserved for 15 minutes", Toast.LENGTH_SHORT).show();
                checkRentalStatus(userId);
            } else {
                Toast.makeText(this, "Reservation failed", Toast.LENGTH_SHORT).show();
            }
        });

        rentButton.setOnClickListener(v -> {
            String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            if (db.startRental(userId, bikeId, startTime)) {
                Toast.makeText(this, "Rental started", Toast.LENGTH_SHORT).show();
                checkRentalStatus(userId);
            } else {
                Toast.makeText(this, "Failed to start rental", Toast.LENGTH_SHORT).show();
            }
        });

        endRentalButton.setOnClickListener(v -> {
            String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Cursor cursor = db.getActiveRental(userId);
            if (cursor.moveToFirst()) {
                int rentalId = cursor.getInt(0);
                String startTime = cursor.getString(3);
                int duration = calculateDuration(startTime, endTime);
                double amount = duration * 0.1; // $0.1 per minute
                if (db.endRental(rentalId, endTime, duration, amount)) {
                    Toast.makeText(this, "Rental ended. Cost: $" + amount, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to end rental", Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();
        });
    }

    private void loadBikeInfo() {
        Cursor cursor = db.getAvailableBikes("");
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(0) == bikeId) {
                    String type = cursor.getString(1);
                    String station = cursor.getString(5);
                    String iconUrl = cursor.getString(4);
                    bikeInfoTextView.setText("Bike: " + type + "\nStation: " + station);
                    Picasso.get().load(iconUrl).into(bikeIconImageView);
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void checkRentalStatus(int userId) {
        Cursor reservationCursor = db.getActiveReservation(userId);
        Cursor rentalCursor = db.getActiveRental(userId);
        if (rentalCursor.moveToFirst()) {
            rentalStatusTextView.setText("Active Rental: " + rentalCursor.getString(6) + " at " + rentalCursor.getString(7));
            reserveButton.setEnabled(false);
            rentButton.setEnabled(false);
            endRentalButton.setEnabled(true);
        } else if (reservationCursor.moveToFirst()) {
            rentalStatusTextView.setText("Reserved until: " + reservationCursor.getString(4));
            reserveButton.setEnabled(false);
            rentButton.setEnabled(true);
            endRentalButton.setEnabled(false);
        } else {
            rentalStatusTextView.setText("No active rental or reservation");
            reserveButton.setEnabled(true);
            rentButton.setEnabled(false);
            endRentalButton.setEnabled(false);
        }
        reservationCursor.close();
        rentalCursor.close();
    }

    private int calculateDuration(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            long diff = end.getTime() - start.getTime();
            return (int) (diff / (1000 * 60)); // Minutes
        } catch (Exception e) {
            return 0;
        }
    }
}