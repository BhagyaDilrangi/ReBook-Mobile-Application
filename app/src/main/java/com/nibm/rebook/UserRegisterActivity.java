package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterActivity extends AppCompatActivity {

    private static final String TAG = "UserRegisterActivity";
    private EditText etFirstName, etLastName, etEmail, etTele, etPassword, etConfirmPassword;
    private CheckBox cbSellerRole;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        try {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference("users");
        } catch (Exception e) {
            Log.e(TAG, "Firebase Initialization Error", e);
        }

        etFirstName = findViewById(R.id.edit_regi_firstname);
        etLastName = findViewById(R.id.edit_regi_lastname);
        etEmail = findViewById(R.id.edit_regi_email);
        etTele = findViewById(R.id.edit_regi_tele);
        etPassword = findViewById(R.id.edit_regi_password);
        etConfirmPassword = findViewById(R.id.edit_regi_confirmedpassword);
        cbSellerRole = findViewById(R.id.cb_remember_me);
        btnRegister = findViewById(R.id.regi_btnregister);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        try {
            String fName = etFirstName.getText().toString().trim();
            String lName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String tele = etTele.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confPass = etConfirmPassword.getText().toString().trim();

            if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || tele.isEmpty() || password.isEmpty() || confPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                etPassword.setError("Minimum 6 characters required");
                etPassword.requestFocus();
                return;
            }

            btnRegister.setEnabled(false);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        try {
                            if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                                String uid = mAuth.getCurrentUser().getUid();

                                String role = (cbSellerRole != null && cbSellerRole.isChecked()) ? "Seller" : "Buyer";

                                Map<String, Object> user = new HashMap<>();
                                user.put("uid", uid);
                                user.put("firstName", fName);
                                user.put("lastName", lName);
                                user.put("email", email);
                                user.put("telephone", tele);
                                user.put("role", role);
                                user.put("isVerified", false); // Boolean status
                                user.put("status", "Pending"); // String status matching dashboard controllers

                                mDatabase.child(uid).setValue(user)
                                        .addOnSuccessListener(aVoid -> {
                                            mAuth.signOut();
                                            showSuccessDialog();
                                        })
                                        .addOnFailureListener(e -> {
                                            btnRegister.setEnabled(true);
                                            Toast.makeText(UserRegisterActivity.this, "Database Write Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        });
                            } else {
                                btnRegister.setEnabled(true);
                                String error = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                                Toast.makeText(UserRegisterActivity.this, "Auth Error: " + error, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            btnRegister.setEnabled(true);
                            Toast.makeText(UserRegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            btnRegister.setEnabled(true);
            Toast.makeText(this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Registration successful! Please wait for admin verification before logging in.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    startActivity(new Intent(UserRegisterActivity.this, UserLoginActivity.class));
                    finish();
                })
                .show();
    }
}