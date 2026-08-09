package com.nibm.rebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginBtn;
    CheckBox showPassword;
    TextView txtForgotPassword;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth with try-catch for safety
        try {
            auth = FirebaseAuth.getInstance();
        } catch (Exception e) {
            Toast.makeText(this, "Firebase Initialization Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        showPassword = findViewById(R.id.showPassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        // Show / Hide password toggle
        showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                if (isChecked) {
                    password.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                            android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                password.setSelection(password.length());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loginBtn.setOnClickListener(v -> validateLogin());

        // Redirect to Forgot Password activity
        if (txtForgotPassword != null) {
            txtForgotPassword.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Unable to open Forgot Password screen", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void validateLogin() {
        try {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            // Field Validations
            if (TextUtils.isEmpty(user)) {
                username.setError("Email is required");
                username.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(pass)) {
                password.setError("Password required");
                password.requestFocus();
                return;
            }

            // Hardcoded Admin Backdoor & Auto-Database Save with Exception Protection
            if (user.equalsIgnoreCase("admin@rebook.com") && pass.equals("admin123")) {
                try {
                    DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference("users").child("admin123");
                    adminRef.child("email").setValue("admin@rebook.com");
                    adminRef.child("firstName").setValue("System");
                    adminRef.child("lastName").setValue("Admin");
                    adminRef.child("password").setValue("admin123");
                    adminRef.child("role").setValue("Admin");
                } catch (Exception dbEx) {
                    // Fail silently on DB sync if offline, but still permit admin entry
                    dbEx.printStackTrace();
                }

                Toast.makeText(LoginActivity.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                finish();
                return;
            }

            // Standard Firebase Authentication with Detailed Exception Handling
            if (auth == null) {
                Toast.makeText(LoginActivity.this, "Authentication service is unavailable", Toast.LENGTH_LONG).show();
                return;
            }

            auth.signInWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(task -> {
                        try {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,
                                        "Login Successful",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this,
                                        DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String errorMsg = "Authentication Failed";
                                if (task.getException() != null && task.getException().getMessage() != null) {
                                    errorMsg = task.getException().getMessage();
                                }
                                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Error processing login: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}