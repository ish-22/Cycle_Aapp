package com.example.citycyclerentalsnew;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, phoneEditText, passwordEditText, paymentEditText;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        paymentEditText = findViewById(R.id.paymentEditText);
        Button registerButton = findViewById(R.id.registerButton);
        db = new DatabaseHelper(this);

        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String paymentInfo = paymentEditText.getText().toString().trim();
            String regDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            if (!validateInputs(name, email, phone, password, paymentInfo)) return;

            if (db.registerUser(email, password, name, phone, paymentInfo, "user", regDate)) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String name, String email, String phone, String password, String paymentInfo) {
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            return false;
        }
        if (phone.isEmpty() || phone.length() < 10) {
            phoneEditText.setError("Enter a valid phone number");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return false;
        }
        if (paymentInfo.isEmpty() || paymentInfo.length() < 12) {
            paymentEditText.setError("Enter valid payment info (min 12 digits)");
            return false;
        }
        return true;
    }
}