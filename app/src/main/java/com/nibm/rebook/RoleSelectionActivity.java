package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class RoleSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        MaterialCardView cardAdmin = findViewById(R.id.cardAdmin);
        MaterialCardView cardUser = findViewById(R.id.cardUser);

        // Clicking Admin redirects to Admin Login Activity
        cardAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
            intent.putExtra("USER_ROLE", "ADMIN");
            startActivity(intent);
        });

        // Clicking User (Buyer / Seller) redirects to User Login Activity
        cardUser.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, UserLoginActivity.class);
            intent.putExtra("USER_ROLE", "USER");
            startActivity(intent);
        });
    }
}