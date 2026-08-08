package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private boolean isVerified = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        // Check user verification status from Firebase
        checkUserVerification();

        // Profile Drawer Trigger
        findViewById(R.id.imgProfile).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        // 1. Dashboard Card Listeners
        findViewById(R.id.cardAddMaterial).setOnClickListener(v -> {
            if (isVerified) {
                startActivity(new Intent(this, AddMaterialActivity.class));
            } else {
                Toast.makeText(this, "Account must be verified by Admin!", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.cardMyListings).setOnClickListener(v ->
                startActivity(new Intent(this, MyListingsActivity.class)));

        findViewById(R.id.cardRequests).setOnClickListener(v ->
                startActivity(new Intent(this, SalesRequestsActivity.class)));

        findViewById(R.id.cardRatings).setOnClickListener(v ->
                startActivity(new Intent(this, SellerFeedbackActivity.class)));

        // 2. Navigation Drawer Logic
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_add) {
                if (isVerified) startActivity(new Intent(SellerDashboardActivity.this, AddMaterialActivity.class));
                else Toast.makeText(this, "Account not verified", Toast.LENGTH_SHORT).show();
            }
            else if (id == R.id.nav_inventory) startActivity(new Intent(SellerDashboardActivity.this, MyListingsActivity.class));
            else if (id == R.id.nav_requests) startActivity(new Intent(SellerDashboardActivity.this, SalesRequestsActivity.class));
            else if (id == R.id.nav_history_sales) startActivity(new Intent(SellerDashboardActivity.this, SalesHistoryActivity.class));
            else if (id == R.id.nav_history_borrow) startActivity(new Intent(SellerDashboardActivity.this, BorrowHistoryActivity.class));
            else if (id == R.id.nav_notifications) startActivity(new Intent(SellerDashboardActivity.this, NotificationsActivity.class));
            else if (id == R.id.nav_chat) startActivity(new Intent(SellerDashboardActivity.this, ChatActivity.class));
            else if (id == R.id.nav_leaderboard) startActivity(new Intent(SellerDashboardActivity.this, LeaderboardActivity.class));
            else if (id == R.id.nav_profile) startActivity(new Intent(SellerDashboardActivity.this, EditProfileActivity.class));
            else if (id == R.id.nav_logout) {
                mAuth.signOut();
                startActivity(new Intent(SellerDashboardActivity.this, RoleSelectionActivity.class));
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void checkUserVerification() {
        if (mAuth.getCurrentUser() == null) return;
        
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String status = snapshot.child("status").getValue(String.class);
                            isVerified = "Verified".equalsIgnoreCase(status);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}