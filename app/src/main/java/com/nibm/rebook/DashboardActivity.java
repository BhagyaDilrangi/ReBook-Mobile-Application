package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    CardView cardStudents, cardMaterials, cardReports,
            cardTransactions, cardFeedback, cardLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        cardStudents = findViewById(R.id.cardStudents);
        cardMaterials = findViewById(R.id.cardMaterials);
        cardReports = findViewById(R.id.cardReports);
        cardTransactions = findViewById(R.id.cardTransactions);
        cardFeedback = findViewById(R.id.cardFeedback);
        cardLeaderboard = findViewById(R.id.cardLeaderboard);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Dashboard Card Clicks
        cardStudents.setOnClickListener(v ->
                startActivity(new Intent(this, StudentActivity.class)));

        cardMaterials.setOnClickListener(v ->
                startActivity(new Intent(this, MaterialActivity.class)));

        cardReports.setOnClickListener(v ->
                startActivity(new Intent(this, ReportActivity.class)));

        cardTransactions.setOnClickListener(v ->
                startActivity(new Intent(this, TransactionActivity.class)));

        cardFeedback.setOnClickListener(v ->
                startActivity(new Intent(this, FeedbackActivity.class)));

        cardLeaderboard.setOnClickListener(v ->
                startActivity(new Intent(this, LeaderboardActivity.class)));

        // Handle Back Press using OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_students) {

            startActivity(new Intent(this, StudentActivity.class));

        } else if (id == R.id.nav_materials) {

            startActivity(new Intent(this, MaterialActivity.class));

        } else if (id == R.id.nav_reports) {

            startActivity(new Intent(this, ReportActivity.class));

        } else if (id == R.id.nav_transactions) {

            startActivity(new Intent(this, TransactionActivity.class));

        } else if (id == R.id.nav_feedback) {

            startActivity(new Intent(this, FeedbackActivity.class));

        } else if (id == R.id.nav_leaderboard) {

            startActivity(new Intent(this, LeaderboardActivity.class));

        } else if (id == R.id.nav_logout) {

            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
