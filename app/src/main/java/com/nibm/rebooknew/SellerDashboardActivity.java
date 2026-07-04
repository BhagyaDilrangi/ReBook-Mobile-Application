package com.nibm.rebooknew;

import android.content.Intent; // IMPORTANT: Must be imported
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SellerDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        // Profile Drawer Trigger
        findViewById(R.id.imgProfile).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        // 1. Dashboard Card Listeners
        findViewById(R.id.cardAddMaterial).setOnClickListener(v -> {
            if (isUserVerified()) {
                startActivity(new Intent(this, AddMaterialActivity.class));
            } else {
                Toast.makeText(this, "Account must be verified!", Toast.LENGTH_LONG).show();
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

            if (id == R.id.nav_add) startActivity(new Intent(SellerDashboardActivity.this, AddMaterialActivity.class));
            else if (id == R.id.nav_inventory) startActivity(new Intent(SellerDashboardActivity.this, MyListingsActivity.class));
            else if (id == R.id.nav_requests) startActivity(new Intent(SellerDashboardActivity.this, SalesRequestsActivity.class));
            else if (id == R.id.nav_history_sales) startActivity(new Intent(SellerDashboardActivity.this, SalesHistoryActivity.class));
            else if (id == R.id.nav_history_borrow) startActivity(new Intent(SellerDashboardActivity.this, BorrowHistoryActivity.class));
            else if (id == R.id.nav_notifications) startActivity(new Intent(SellerDashboardActivity.this, NotificationsActivity.class));
            else if (id == R.id.nav_chat) startActivity(new Intent(SellerDashboardActivity.this, ChatActivity.class));
            else if (id == R.id.nav_leaderboard) startActivity(new Intent(SellerDashboardActivity.this, LeaderboardActivity.class));
            else if (id == R.id.nav_profile) startActivity(new Intent(SellerDashboardActivity.this, EditProfileActivity.class));
            else if (id == R.id.nav_logout) {
                // Implement Logout Logic Here
                Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private boolean isUserVerified() {
        return true;
    }
}