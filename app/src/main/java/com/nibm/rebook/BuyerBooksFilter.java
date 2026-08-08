package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BuyerBooksFilter extends AppCompatActivity {

    Button btndonationsfilter,btnsalesfilter,btnborowfilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_books_filter);

        btndonationsfilter=findViewById(R.id.btndonationsfilter);
        btnborowfilter=findViewById(R.id.btnborowfilter);
        btndonationsfilter=findViewById(R.id.btndonationsfilter);

        btnborowfilter.setOnClickListener(view -> {
            startActivity(new Intent(BuyerBooksFilter.this, BuyerBooks.class));
        });

        btnsalesfilter.setOnClickListener(view -> {
            startActivity(new Intent(BuyerBooksFilter.this, BuyerBooksSales.class));
        });

        btnsalesfilter.setOnClickListener(view -> {
            startActivity(new Intent(BuyerBooksFilter.this, BuyerBooksDonate.class));
        });
    }
}