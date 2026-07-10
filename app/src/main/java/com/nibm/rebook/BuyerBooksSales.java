package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BuyerBooksSales extends AppCompatActivity {

    Button btnBook_sell_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_books_sales);

    btnBook_sell_get=findViewById(R.id.btnBook_sell_get);

    btnBook_sell_get.setOnClickListener(view -> {
        startActivity(new Intent(BuyerBooksSales.this, BuyerSelling.class));
    });
    }
}