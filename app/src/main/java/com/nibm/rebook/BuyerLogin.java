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

public class BuyerLogin extends AppCompatActivity {

    private static final String TAG = "LOGIN_DEBUG";
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView txtRegister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btnlogin);
        txtRegister = findViewById(R.id.txtregister);

        txtRegister.setOnClickListener(v ->
                startActivity(new Intent(BuyerLogin.this, BuyerRegister.class)));

        btnLogin.setOnClickListener(view -> {
            Log.d(TAG, "Login button clicked!");
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Log.d(TAG, "Validation failed: Fields are empty");
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Attempting Firebase Sign-In for email: " + email);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "Firebase Auth Successful! User UID: " + uid);

                            Log.d(TAG, "Fetching data from Realtime Database...");
                            FirebaseDatabase.getInstance().getReference("users").child(uid)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            Log.d(TAG, "Database onDataChange triggered");
                                            if (snapshot.exists()) {
                                                Log.d(TAG, "User Profile Found! Redirecting to Home...");
                                                startActivity(new Intent(BuyerLogin.this, BuyerHome.class));
                                                finish();
                                            } else {
                                                Log.d(TAG, "Database Error: Profile not found for UID " + uid);
                                                Toast.makeText(BuyerLogin.this, "Profile not found", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            Log.d(TAG, "Database onCancelled: " + error.getMessage());
                                            Toast.makeText(BuyerLogin.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Firebase Auth Failed: " + task.getException().getMessage());
                            Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
