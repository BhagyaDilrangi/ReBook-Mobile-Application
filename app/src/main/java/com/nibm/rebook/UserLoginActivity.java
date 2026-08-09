package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserLoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN_DEBUG";
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private FirebaseAuth mAuth;
    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btnlogin);
        txtRegister = findViewById(R.id.txtregister);

        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Attempting login for: " + email);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Auth successful, checking database...");
                            checkUserRoleAndVerification();
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Log.e(TAG, "Login Failed: " + error);
                            Toast.makeText(this, "Login Failed: " + error, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void checkUserRoleAndVerification() {
        if (mAuth.getCurrentUser() == null) return;

        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance(DATABASE_URL).getReference("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String role = snapshot.child("role").getValue(String.class);
                            String status = snapshot.child("status").getValue(String.class);
                            Boolean isVerified = snapshot.child("isVerified").getValue(Boolean.class);

                            Log.d(TAG, "User role: " + role + ", Status: " + status + ", IsVerified: " + isVerified);

                            // Admin role bypasses verification checks
                            if ("Admin".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
                                navigateToDashboard(DashboardActivity.class);
                                return;
                            }

                            // Check both status string ("Verified") or boolean flag (true) for robustness
                            boolean verifiedCheck = ("Verified".equalsIgnoreCase(status)) || (isVerified != null && isVerified);

                            if (verifiedCheck) {
                                if ("Seller".equalsIgnoreCase(role) || "SELLER".equalsIgnoreCase(role)) {
                                    navigateToDashboard(SellerDashboardActivity.class);
                                } else {
                                    navigateToDashboard(BuyerHome.class);
                                }
                            } else {
                                // Account is still pending approval
                                mAuth.signOut();
                                Toast.makeText(UserLoginActivity.this, "Your account is pending admin verification.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.e(TAG, "User profile not found in database for UID: " + uid);
                            mAuth.signOut();
                            Toast.makeText(UserLoginActivity.this, "User profile not found. Please contact support.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e(TAG, "Database error: " + error.getMessage());
                        Toast.makeText(UserLoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToDashboard(Class<?> targetActivity) {
        Intent intent = new Intent(UserLoginActivity.this, targetActivity);
        startActivity(intent);
        finish();
    }
}