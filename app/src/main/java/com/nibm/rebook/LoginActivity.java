package com.nibm.rebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginBtn;
    CheckBox showPassword;
    TextView txtForgotPassword; // Added reference for Forgot Password
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        showPassword = findViewById(R.id.showPassword);
        txtForgotPassword = findViewById(R.id.txtForgotPassword); // Make sure this matches your layout XML ID

        // Show / Hide password
        showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                password.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                password.setInputType(android.text.InputType.TYPE_CLASS_TEXT |
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            password.setSelection(password.length());
        });

        loginBtn.setOnClickListener(v -> validateLogin());

        // Redirect to Forgot Password activity
        if (txtForgotPassword != null) {
            txtForgotPassword.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            });
        }
    }

    private void validateLogin() {

        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(user)) {
            username.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            password.setError("Password required");
            return;
        }
        auth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(LoginActivity.this,
                                "Login Successful",
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this,
                                DashboardActivity.class);

                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(LoginActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                });
    }
}