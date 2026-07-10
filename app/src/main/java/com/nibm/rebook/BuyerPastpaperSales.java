package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BuyerPastpaperSales extends AppCompatActivity {

    Button btn_pastpaper_sell_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_pastpaper_sales);

        btn_pastpaper_sell_get=findViewById(R.id.btn_pastpaper_sell_get);

        btn_pastpaper_sell_get.setOnClickListener(view -> {
            startActivity(new Intent(BuyerPastpaperSales.this, BuyerSelling.class));
        });
    }
}