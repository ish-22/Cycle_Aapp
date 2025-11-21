package com.example.citycyclerentalsnew;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startButton);
        Button adminButton = findViewById(R.id.adminButton); // New button

        startButton.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        adminButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminDashboardActivity.class));
            // Optional: finish() if you don't want to return to MainActivity
        });
    }
}