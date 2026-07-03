package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Buyer_CalculatarSales extends AppCompatActivity {

    Button btncalgetitemsell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_calculatar_sales);

        btncalgetitemsell=findViewById(R.id.btncalgetitemsell);

        btncalgetitemsell.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_CalculatarSales.this, User_Buyer_Selling.class));
        });
    }
}