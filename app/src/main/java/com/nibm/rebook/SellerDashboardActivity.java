package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
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
    private TextView txtStudentName;

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

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

        // Initialize the student/user name TextView (Ensure ID matches your XML layout file)
        txtStudentName = findViewById(R.id.txtStudentName);

        // Fetch user profile data (Name & Verification status) from Firebase
        fetchUserData();

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

    private void fetchUserData() {
        if (mAuth.getCurrentUser() == null) return;

        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance(DATABASE_URL)
                .getReference("users").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // 1. Extract and set verification status
                            String status = snapshot.child("status").getValue(String.class);
                            Boolean isVerifiedBool = snapshot.child("isVerified").getValue(Boolean.class);
                            isVerified = "Verified".equalsIgnoreCase(status) || (isVerifiedBool != null && isVerifiedBool);

                            // 2. Extract first and last name to display in the student name TextView
                            String firstName = snapshot.child("firstName").getValue(String.class);
                            String lastName = snapshot.child("lastName").getValue(String.class);

                            if (txtStudentName != null) {
                                if (firstName != null && lastName != null) {
                                    txtStudentName.setText(firstName + " " + lastName);
                                } else if (firstName != null) {
                                    txtStudentName.setText(firstName);
                                } else {
                                    txtStudentName.setText("User");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SellerDashboardActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}