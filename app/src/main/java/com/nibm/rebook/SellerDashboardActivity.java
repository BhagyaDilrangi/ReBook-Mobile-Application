package com.nibm.rebook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellerDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private boolean isVerified = false;
    private FirebaseAuth mAuth;
    private TextView txtStudentName, txtContributionScore;
    private ImageView imgProfileDrawerTrigger;

    // Slide-Show Preview Placeholder components & ticker elements
    private View slideShowViewPlaceholder;
    private TextView txtSlideShowMessage;
    private final Handler slideHandler = new Handler(Looper.getMainLooper());
    private final List<String> slideShowTexts = new ArrayList<>();
    private int slideIndex = 0;
    private Runnable slideRunnable;

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @SuppressLint("MissingInflatedId")
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

        // Initialize UI Elements
        txtStudentName = findViewById(R.id.txtStudentName);
        txtContributionScore = findViewById(R.id.txtContributionScore);
        imgProfileDrawerTrigger = findViewById(R.id.imgProfile);
        slideShowViewPlaceholder = findViewById(R.id.slideShowViewPlaceholder);
        txtSlideShowMessage = findViewById(R.id.txtSlideShowMessage); // Ensure textview component exists inside placeholder if used

        // Initialize default slideshow banner notices
        slideShowTexts.add("Welcome to ReBook Platform Hub");
        slideShowTexts.add("Tip: Keep your inventory listings active to boost your score!");
        slideShowTexts.add("Check Sales Requests for pending buyer orders.");

        setupSlideShowTicker();

        // Fetch dynamic logged-in user profile data, verification status, and contribution score
        fetchUserDataAndContributionScore();

        // Profile Drawer Trigger
        imgProfileDrawerTrigger.setOnClickListener(v ->
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

        // 2. Fully Functional Navigation Drawer Logic mapped to all menu elements
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // Admin Items Mapping
            if (id == R.id.nav_students) {
                startActivity(new Intent(SellerDashboardActivity.this, StudentActivity.class));
            }
            else if (id == R.id.nav_materials) {
                startActivity(new Intent(SellerDashboardActivity.this, MaterialActivity.class));
            }
            else if (id == R.id.nav_reports) {
                startActivity(new Intent(SellerDashboardActivity.this, ReportActivity.class));
            }
            else if (id == R.id.nav_transactions) {
                startActivity(new Intent(SellerDashboardActivity.this, TransactionActivity.class));
            }
            else if (id == R.id.nav_feedback) {
                startActivity(new Intent(SellerDashboardActivity.this, FeedbackActivity.class));
            }
            // Seller Items Mapping
            else if (id == R.id.nav_add) {
                if (isVerified) {
                    startActivity(new Intent(SellerDashboardActivity.this, AddMaterialActivity.class));
                } else {
                    Toast.makeText(this, "Account not verified", Toast.LENGTH_SHORT).show();
                }
            }
            else if (id == R.id.nav_inventory) {
                startActivity(new Intent(SellerDashboardActivity.this, MyListingsActivity.class));
            }
            else if (id == R.id.nav_requests) {
                startActivity(new Intent(SellerDashboardActivity.this, SalesRequestsActivity.class));
            }
            else if (id == R.id.nav_history_sales) {
                startActivity(new Intent(SellerDashboardActivity.this, SalesHistoryActivity.class));
            }
            else if (id == R.id.nav_history_borrow) {
                startActivity(new Intent(SellerDashboardActivity.this, BorrowHistoryActivity.class));
            }
            else if (id == R.id.nav_notifications) {
                startActivity(new Intent(SellerDashboardActivity.this, NotificationsActivity.class));
            }
            else if (id == R.id.nav_chat) {
                startActivity(new Intent(SellerDashboardActivity.this, ChatActivity.class));
            }
            else if (id == R.id.nav_profile) {
                startActivity(new Intent(SellerDashboardActivity.this, EditProfileActivity.class));
            }
            // Shared Items Mapping
            else if (id == R.id.nav_leaderboard) {
                startActivity(new Intent(SellerDashboardActivity.this, LeaderboardActivity.class));
            }
            else if (id == R.id.nav_logout) {
                mAuth.signOut();
                startActivity(new Intent(SellerDashboardActivity.this, RoleSelectionActivity.class));
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupSlideShowTicker() {
        slideRunnable = new Runnable() {
            @Override
            public void run() {
                if (!slideShowTexts.isEmpty() && txtSlideShowMessage != null) {
                    txtSlideShowMessage.setText(slideShowTexts.get(slideIndex));
                    slideIndex = (slideIndex + 1) % slideShowTexts.size();
                }
                slideHandler.postDelayed(this, 4000); // Rotate every 4 seconds
            }
        };
        slideHandler.postDelayed(slideRunnable, 1000);
    }

    private void fetchUserDataAndContributionScore() {
        if (mAuth.getCurrentUser() == null) return;

        String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance(DATABASE_URL).getReference();

        // 1. Fetch User Profile Data (Name & Verification Status)
        dbRef.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.child("status").getValue(String.class);
                    Boolean isVerifiedBool = snapshot.child("isVerified").getValue(Boolean.class);
                    isVerified = "Verified".equalsIgnoreCase(status) || (isVerifiedBool != null && isVerifiedBool);

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

        // 2. Fetch Live Contribution Score based on total materials added by this user
        dbRef.child("materials").orderByChild("sellerId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int materialCount = (int) snapshot.getChildrenCount();
                        int contributionScore = materialCount * 10; // Calculating score based on uploaded count

                        if (txtContributionScore != null) {
                            txtContributionScore.setText("Contribution Score: " + contributionScore);
                        }

                        // Feed live inventory metrics dynamically into the slideshow rotator
                        if (materialCount > 0) {
                            slideShowTexts.add("Active Inventory: You have " + materialCount + " items listed.");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (slideHandler != null && slideRunnable != null) {
            slideHandler.removeCallbacks(slideRunnable);
        }
    }
}