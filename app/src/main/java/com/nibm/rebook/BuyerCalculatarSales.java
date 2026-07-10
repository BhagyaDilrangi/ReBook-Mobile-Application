package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BuyerCalculatarSales extends AppCompatActivity {

    Button btncalgetitemsell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_calculatar_sales);

        btncalgetitemsell=findViewById(R.id.btncalgetitemsell);

        btncalgetitemsell.setOnClickListener(view -> {
            startActivity(new Intent(BuyerCalculatarSales.this, BuyerSelling.class));
        });
    }
}