package com.nibm.rebook;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnUpdatePassword;
    private CheckBox showPassword;
    private TextView txtBackToLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // Matched with your google-services.json project URL
    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference("users");

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        showPassword = findViewById(R.id.showPassword);
        txtBackToLogin = findViewById(R.id.txtBackToLogin);

        // Show / Hide password toggle logic
        showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etNewPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etConfirmPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                etNewPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etConfirmPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            etNewPassword.setSelection(etNewPassword.length());
            etConfirmPassword.setSelection(etConfirmPassword.length());
        });

        // Handle password update trigger
        btnUpdatePassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword)) {
                etNewPassword.setError("New password is required");
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                etConfirmPassword.setError("Please confirm your password");
                return;
            }

            if (newPassword.length() < 6) {
                etNewPassword.setError("Password must be at least 6 characters");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match");
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                String uid = user.getUid();

                // 1. Update password in Firebase Authentication
                user.updatePassword(newPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // 2. Synchronize and update password value inside Realtime Database
                                mDatabase.child(uid).child("password").setValue(newPassword)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(ForgotPasswordActivity.this,
                                                    "Password updated successfully in Auth & Database!",
                                                    Toast.LENGTH_LONG).show();
                                            mAuth.signOut(); // Force re-login with new credentials
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(ForgotPasswordActivity.this,
                                                    "Auth updated, but database sync failed: " + e.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        });
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Error: " + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(ForgotPasswordActivity.this,
                        "Error: No authenticated user found. Please log in again.",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Navigate back to log in screen
        txtBackToLogin.setOnClickListener(v -> finish());
    }
}