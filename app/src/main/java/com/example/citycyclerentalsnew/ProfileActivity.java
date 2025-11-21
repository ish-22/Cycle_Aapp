package com.example.citycyclerentalsnew;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private EditText nameEditText, phoneEditText, paymentEditText;
    private DatabaseHelper db;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        paymentEditText = findViewById(R.id.paymentEditText);
        Button updateButton = findViewById(R.id.updateButton);
        db = new DatabaseHelper(this);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        int userId = prefs.getInt("userId", -1);
        loadProfile(userId);

        updateButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String paymentInfo = paymentEditText.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || paymentInfo.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Update profile logic (requires modification in DatabaseHelper)
                Toast.makeText(this, "Profile update not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProfile(int userId) {
        Cursor cursor = db.loginUser("", ""); // Placeholder; improve with proper query
        if (cursor.moveToFirst() && cursor.getInt(0) == userId) {
            nameEditText.setText(cursor.getString(3));
            phoneEditText.setText(cursor.getString(4));
            paymentEditText.setText(cursor.getString(5));
        }
        cursor.close();
    }
}