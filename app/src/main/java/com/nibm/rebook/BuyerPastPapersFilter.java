package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BuyerPastPapersFilter extends AppCompatActivity {

    Button btn_pastpaper_sellfilter,btn_pastpaper_donationsfilter,btn_pastpaper_borowfilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_past_papers_filter);

            btn_pastpaper_borowfilter=findViewById(R.id.btn_pastpaper_borowfilter);
            btn_pastpaper_donationsfilter=findViewById(R.id.btn_pastpaper_donationsfilter);
            btn_pastpaper_borowfilter=findViewById(R.id.btn_pastpaper_borowfilter);

            btn_pastpaper_borowfilter.setOnClickListener(view -> {
                startActivity(new Intent(BuyerPastPapersFilter.this, BuyerPastPapers.class));
            });

            btn_pastpaper_sellfilter.setOnClickListener(view -> {
                startActivity(new Intent(BuyerPastPapersFilter.this, BuyerPastpaperSales.class));
            });

            btn_pastpaper_donationsfilter.setOnClickListener(view -> {
                startActivity(new Intent(BuyerPastPapersFilter.this, BuyerPastPaperDonate.class));

            });



    }
}