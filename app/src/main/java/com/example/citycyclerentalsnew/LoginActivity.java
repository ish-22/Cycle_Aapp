package com.example.citycyclerentalsnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private DatabaseHelper db;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerLink = findViewById(R.id.registerLink);
        db = new DatabaseHelper(this);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!validateInputs(email, password)) return;

            Cursor cursor = db.loginUser(email, password);
            if (cursor.moveToFirst()) {
                int userId = cursor.getInt(0);
                String role = cursor.getString(6);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("userId", userId);
                editor.putString("role", role);
                editor.apply();
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                if ("admin".equals(role)) {
                    startActivity(new Intent(this, AdminDashboardActivity.class));
                } else {
                    startActivity(new Intent(this, BikeListActivity.class));
                }
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        });

        registerLink.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }
}