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
            // Pass the role if we came from RoleSelection
            intent.putExtra("USER_ROLE", getIntent().getStringExtra("USER_ROLE"));
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
                            checkUserRole();
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Log.e(TAG, "Login Failed: " + error);
                            Toast.makeText(this, "Login Failed: " + error, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void checkUserRole() {
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance(DATABASE_URL).getReference("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String role = snapshot.child("role").getValue(String.class);
                            Log.d(TAG, "User role found: " + role);

                            Intent intent;
                            if ("BUYER".equalsIgnoreCase(role)) {
                                intent = new Intent(UserLoginActivity.this, BuyerHome.class);
                            } else if ("SELLER".equalsIgnoreCase(role)) {
                                intent = new Intent(UserLoginActivity.this, SellerDashboardActivity.class);
                            } else if ("ADMIN".equalsIgnoreCase(role)) {
                                intent = new Intent(UserLoginActivity.this, DashboardActivity.class);
                            } else {
                                Log.e(TAG, "Invalid role in database: " + role);
                                Toast.makeText(UserLoginActivity.this, "Invalid account role", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "User profile not found in database for UID: " + uid);
                            Toast.makeText(UserLoginActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e(TAG, "Database error: " + error.getMessage());
                        Toast.makeText(UserLoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
