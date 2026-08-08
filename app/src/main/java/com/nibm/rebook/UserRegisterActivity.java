package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

    EditText etFirstName, etLastName, etEmail, etTele, etPassword, etConfirmPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
<<<<<<< HEAD
=======

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
>>>>>>> 4862004 (Implement successful admin login)

        mAuth = FirebaseAuth.getInstance();
        // Updated to use the default instance which follows google-services.json
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        etFirstName = findViewById(R.id.edit_regi_firstname);
        etLastName = findViewById(R.id.edit_regi_lastname);
        etEmail = findViewById(R.id.edit_regi_email);
        etTele = findViewById(R.id.edit_regi_tele);
        etPassword = findViewById(R.id.edit_regi_passward);
<<<<<<< HEAD
        etConfirmPassword = findViewById(R.id.edit_regi_conformedpassward);
=======
        etConfirmPassword = findViewById(R.id.edit_regi_confirmedpassword);
        cbSellerRole = findViewById(R.id.cb_remember_me);
>>>>>>> 4862004 (Implement successful admin login)
        btnRegister = findViewById(R.id.regi_btnregister);

        btnRegister.setOnClickListener(v -> {
            String fName = etFirstName.getText().toString().trim();
            String lName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String tele = etTele.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confPass = etConfirmPassword.getText().toString().trim();

<<<<<<< HEAD
=======
            boolean isSeller = cbSellerRole.isChecked();
            String userRole = isSeller ? "Seller" : "Buyer";

>>>>>>> 4862004 (Implement successful admin login)
            if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || tele.isEmpty() || password.isEmpty() || confPass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("firstName", fName);
                            userData.put("lastName", lName);
                            userData.put("email", email);
                            userData.put("telephone", tele);
<<<<<<< HEAD
=======
                            userData.put("role", userRole);
>>>>>>> 4862004 (Implement successful admin login)

                            mDatabase.child(uid).setValue(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        new AlertDialog.Builder(this)
                                                .setTitle("Success")
                                                .setMessage("Thank you for registering!")
                                                .setPositiveButton("OK", (dialog, which) -> {
<<<<<<< HEAD
                                                    startActivity(new Intent(UserRegisterActivity.this, UserLoginActivity.class));
=======
                                                    Intent intent;
                                                    if (isSeller) {
                                                        intent = new Intent(UserRegisterActivity.this, SellerDashboardActivity.class);
                                                    } else {
                                                        intent = new Intent(UserRegisterActivity.this, BuyerHome.class);
                                                    }
                                                    startActivity(intent);
>>>>>>> 4862004 (Implement successful admin login)
                                                    finish();
                                                })
                                                .show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Database Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            Toast.makeText(this, "Auth Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
