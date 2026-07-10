package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class BuyerHome extends AppCompatActivity {

    Button btnCalculator, btnPastPapers, btnNotes, btnBooks;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView imgMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        imgMenu = findViewById(R.id.imgMenu);
        btnCalculator = findViewById(R.id.btnCalculator);
        btnPastPapers = findViewById(R.id.btnPastPapers);
        btnNotes = findViewById(R.id.btnNotes);
        btnBooks = findViewById(R.id.btnBooks);

        imgMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        btnCalculator.setOnClickListener(view -> startActivity(new Intent(BuyerHome.this, BuyerCalculaterFilter.class)));
        btnPastPapers.setOnClickListener(view -> startActivity(new Intent(BuyerHome.this, BuyerPastPapersFilter.class)));
        btnNotes.setOnClickListener(view -> startActivity(new Intent(BuyerHome.this, BuyerNotesFilter.class)));
        btnBooks.setOnClickListener(view -> startActivity(new Intent(BuyerHome.this, BuyerBooksFilter.class)));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_account) {
                startActivity(new Intent(BuyerHome.this, BuyerProfile.class));
            } else if (id == R.id.nav_favourites) {
                startActivity(new Intent(BuyerHome.this, BuyerSavedItems.class));
            } else if (id == R.id.nav_request_list) {
                startActivity(new Intent(BuyerHome.this, BuyerRequestItems.class));
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(BuyerHome.this, BuyerPaymentHistory.class));
            } else if (id == R.id.nav_review) {
                startActivity(new Intent(BuyerHome.this, BuyerReviewPage.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }
            }
        });
    }
}
