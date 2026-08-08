package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BuyerPastPapersFilter extends AppCompatActivity {

    Button btnSales, btnDonations, btnBorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_past_papers_filter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnBorrow = findViewById(R.id.btn_pastpaper_borowfilter);
        btnDonations = findViewById(R.id.btn_pastpaper_donationsfilter);
        btnSales = findViewById(R.id.btn_pastpaper_sellfilter);

        btnBorrow.setOnClickListener(view -> {
            startActivity(new Intent(BuyerPastPapersFilter.this, BuyerPastPapers.class));
        });

        btnSales.setOnClickListener(view -> {
            startActivity(new Intent(BuyerPastPapersFilter.this, BuyerPastpaperSales.class));
        });

        btnDonations.setOnClickListener(view -> {
            startActivity(new Intent(BuyerPastPapersFilter.this, BuyerPastPaperDonate.class));
        });
    }
}